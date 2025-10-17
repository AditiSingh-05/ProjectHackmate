package com.example.HackMateBackend.services.implementations;

import com.example.HackMateBackend.dtos.AuthenticationDto.*;
import com.example.HackMateBackend.data.enums.Roles;
import com.example.HackMateBackend.data.entities.User;
import com.example.HackMateBackend.jwtauth.JwtUtils;
import com.example.HackMateBackend.repositories.UserRepository;
import com.example.HackMateBackend.services.interfaces.EmailService;
import com.example.HackMateBackend.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private  UserRepository userRepository;
    @Autowired

    private PasswordEncoder passwordEncoder;
    @Autowired

    private  AuthenticationManager authenticationManager;
    @Autowired

    private  JwtUtils jwtUtils;
    @Autowired

    private EmailService emailService;

    private static final Logger log = LoggerFactory.getLogger(CustomUserDetailService.class);

    @Override
    public SignupResponseDto registerUser(SignupRequestDto signupRequest) {
        log.info("AuthenticationVerification: 🚀 Starting user registration process for email: {}", signupRequest.getEmail());

        // Check if user already exists
        log.info("AuthenticationVerification: Checking if user already exists with email: {}", signupRequest.getEmail());
        if (userRepository.existsByEmailIgnoreCase(signupRequest.getEmail())) {
            log.warn("AuthenticationVerification: ❌ User already exists with email: {}", signupRequest.getEmail());
            throw new RuntimeException("User with this email already exists");
        }
        log.info("AuthenticationVerification: ✅ Email is available for registration");

        // Validate password confirmation
        log.info("AuthenticationVerification: Validating password confirmation");
        if (!signupRequest.getPassword().equals(signupRequest.getConfirmPassword())) {
            log.warn("AuthenticationVerification: ❌ Password confirmation mismatch for email: {}", signupRequest.getEmail());
            throw new RuntimeException("Passwords do not match");
        }
        log.info("AuthenticationVerification: ✅ Password confirmation validated");

        // Create new user
        log.info("AuthenticationVerification: Creating new user entity");
        User user = new User();
        user.setEmail(signupRequest.getEmail().toLowerCase());
        log.info("AuthenticationVerification: Email set to: {}", user.getEmail());

        log.info("AuthenticationVerification: Encoding password");
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        log.info("AuthenticationVerification: Password encoded successfully");

        user.setRole(Roles.USER); // Default role
        user.setEmailVerified(false);
        user.setProfileSetup(false);
        log.info("AuthenticationVerification: User defaults set - Role: {}, EmailVerified: {}, ProfileSetup: {}",
                user.getRole(), user.isEmailVerified(), user.isProfileSetup());

        // Generate email verification token
        log.info("AuthenticationVerification: Generating email verification token");
        String verificationToken = UUID.randomUUID().toString();
        user.setEmailVerificationToken(verificationToken);
        log.info("AuthenticationVerification: ✅ Email verification token generated: {}", verificationToken);

        // Save user
        log.info("AuthenticationVerification: Attempting to save user to database");
        try {
            User savedUser = userRepository.save(user);
            log.info("AuthenticationVerification: ✅ User saved successfully with ID: {} and email: {}",
                    savedUser.getId(), savedUser.getEmail());
            log.info("AuthenticationVerification: Saved user verification token: {}", savedUser.getEmailVerificationToken());
        } catch (Exception e) {
            log.error("AuthenticationVerification: ❌ Failed to save user to database", e);
            throw new RuntimeException("Failed to save user: " + e.getMessage(), e);
        }

        User savedUser = userRepository.findByEmailIgnoreCase(signupRequest.getEmail())
                .orElseThrow(() -> {
                    log.error("AuthenticationVerification: ❌ User not found after saving - this should never happen");
                    return new RuntimeException("User not found after saving");
                });

        // Send verification email
        log.info("AuthenticationVerification: 📧 Starting email verification process");
        log.info("AuthenticationVerification: Email service instance: {}", emailService.getClass().getSimpleName());
        log.info("AuthenticationVerification: About to call emailService.sendEmailVerification with:");
        log.info("AuthenticationVerification: - To: {}", savedUser.getEmail());
        log.info("AuthenticationVerification: - Token: {}", savedUser.getEmailVerificationToken());

        try {
            emailService.sendEmailVerification(
                    savedUser.getEmail(),
                    savedUser.getEmailVerificationToken()
            );
            log.info("AuthenticationVerification: ✅ Email service call completed successfully");
        } catch (Exception e) {
            log.error("AuthenticationVerification: ❌ Email service call failed");
            log.error("AuthenticationVerification: Exception type: {}", e.getClass().getSimpleName());
            log.error("AuthenticationVerification: Exception message: {}", e.getMessage());
            log.error("AuthenticationVerification: Full stack trace:", e);
            // Don't throw exception here - user is registered, just email failed
        }

        log.info("AuthenticationVerification: Creating response DTO");
        SignupResponseDto response = new SignupResponseDto(
                savedUser.getId(),
                savedUser.getEmail(),
                "User registered successfully. Please check your email for verification.",
                savedUser.getRole(),
                savedUser.isEmailVerified(),
                "Verification email sent to " + savedUser.getEmail()
        );

        log.info("AuthenticationVerification: 🎉 Registration process completed for user ID: {} with email: {}",
                savedUser.getId(), savedUser.getEmail());

        return response;
    }

    @Override
    public LoginResponseDto loginUser(LoginRequestDto loginRequest) {

        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            // Generate JWT token
            String token = jwtUtils.generateJwtToken(authentication);

            // Get user details
            User user = userRepository.findByEmailIgnoreCase(loginRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Update last login
            user.updateLastLogin();
            userRepository.save(user);

            log.info("User logged in successfully: {}", user.getEmail());

            return new LoginResponseDto(
                    token,
                    user.getId(),
                    user.getEmail(),
                    user.getRole(),
                    user.isProfileSetup(),
                    user.isEmailVerified()
            );

        } catch (Exception e) {
            throw new RuntimeException("Invalid email or password");
        }
    }

    @Override
    public ChangePasswordResponseDto changePassword(Long userId, ChangePasswordRequestDto request) {
        log.info("Changing password for user ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Verify current password
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        // Validate new password confirmation
        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new RuntimeException("New passwords do not match");
        }

        // Update password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        log.info("Password changed successfully for user ID: {}", userId);

        return new ChangePasswordResponseDto(
                true,
                "Password changed successfully",
                LocalDateTime.now()
        );
    }

    @Override
    public ForgotPasswordResponseDto forgotPassword(ForgotPasswordRequestDto request) {

        Optional<User> userOpt = userRepository.findByEmailIgnoreCase(request.getEmail());

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // Generate reset token (valid for 24 hours)
            String resetToken = UUID.randomUUID().toString();
            user.setPasswordResetToken(resetToken, 24);
            userRepository.save(user);

            // Send reset email
            try {
                emailService.sendPasswordResetEmail(user.getEmail(), resetToken);
                log.info("Password reset email sent to: {}", user.getEmail());
            } catch (Exception e) {
                log.error("Failed to send password reset email to: {}", user.getEmail(), e);
            }
        } else {
            // Don't reveal if email exists for security
        }

        return new ForgotPasswordResponseDto(
                true,
                "If the email exists, you will receive a password reset link",
                "Password reset instructions sent"
        );
    }

    @Override
    public ResetPasswordResponseDto resetPassword(ResetPasswordRequestDto request) {
        log.info("Resetting password with token");

        // Validate password confirmation
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match");
        }

        User user = userRepository.findByPasswordResetToken(request.getResetToken())
                .orElseThrow(() -> new RuntimeException("Invalid or expired reset token"));

        // Check if token is still valid
        if (!user.isPasswordResetTokenValid()) {
            throw new RuntimeException("Reset token has expired");
        }

        // Update password and clear reset token
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.clearPasswordResetToken();
        userRepository.save(user);

        log.info("Password reset successfully for user: {}", user.getEmail());

        return new ResetPasswordResponseDto(
                true,
                "Password reset successfully"
        );
    }

    @Override
    public EmailVerificationResponseDto verifyEmail(EmailVerificationRequestDto request) {
        log.info("Verifying email with token");

        User user = userRepository.findByEmailVerificationToken(request.getVerificationToken())
                .orElseThrow(() -> new RuntimeException("Invalid verification token"));

        if (user.isEmailVerified()) {
            throw new RuntimeException("Email is already verified");
        }

        // Verify email
        user.verifyEmail();
        userRepository.save(user);

        log.info("Email verified successfully for user: {}", user.getEmail());

        return new EmailVerificationResponseDto(
                true,
                "Email verified successfully",
                LocalDateTime.now()
        );
    }

    @Override
    public EmailVerificationResponseDto resendEmailVerification(String email) {
        log.info("Resending email verification for: {}", email);

        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.isEmailVerified()) {
            throw new RuntimeException("Email is already verified");
        }

        // Generate new verification token
        user.setEmailVerificationToken(UUID.randomUUID().toString());
        userRepository.save(user);

        // Send verification email
        try {
            emailService.sendEmailVerification(user.getEmail(), user.getEmailVerificationToken());
            log.info("Email verification resent to: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to resend verification email to: {}", user.getEmail(), e);
            throw new RuntimeException("Failed to send verification email");
        }

        return new EmailVerificationResponseDto(
                true,
                "Verification email sent successfully",
                LocalDateTime.now()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public ProfileSetupStatusDto getProfileSetupStatus(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        int completionPercentage = calculateProfileCompletion(user);

        return new ProfileSetupStatusDto(
                user.isProfileSetup(),
                completionPercentage
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean emailExists(String email) {
        return userRepository.existsByEmailIgnoreCase(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean validateResetToken(String token) {
        Optional<User> userOpt = userRepository.findByPasswordResetToken(token);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return user.isPasswordResetTokenValid();
        }
        return false;
    }

    @Override
    public void updateLastLogin(Long userId) {
        userRepository.updateLastLoginTime(userId, LocalDateTime.now());
        log.debug("Updated last login time for user ID: {}", userId);
    }


    private int calculateProfileCompletion(User user) {
        if (!user.isEmailVerified()) return 20;
        if (!user.isProfileSetup()) return 50;
        return 100;
    }
}
