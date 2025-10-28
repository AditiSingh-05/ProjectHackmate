package com.example.HackMateBackend.controllers;

import com.example.HackMateBackend.data.enums.Roles;
import com.example.HackMateBackend.dtos.authentication.*;
import com.example.HackMateBackend.services.implementations.CustomUserDetailService.UserPrincipal;
import com.example.HackMateBackend.services.interfaces.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> registerUser(@Valid @RequestBody SignupRequestDto signupRequest) {
        try {
            SignupResponseDto response = userService.registerUser(signupRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new SignupResponseDto(null, signupRequest.getEmail(), e.getMessage(),
                            Roles.USER, false, null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> loginUser(@Valid @RequestBody LoginRequestDto loginRequest) {
        try {
            LoginResponseDto response = userService.loginUser(loginRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponseDto(null, null, null, null, false, false));
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<ChangePasswordResponseDto> changePassword(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody ChangePasswordRequestDto request) {

        try {
            ChangePasswordResponseDto response = userService.changePassword(userPrincipal.getId(), request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ChangePasswordResponseDto(false, e.getMessage(), null));
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ForgotPasswordResponseDto> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequestDto request) {

        try {
            ForgotPasswordResponseDto response = userService.forgotPassword(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ForgotPasswordResponseDto(false, "Failed to process request", null));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ResetPasswordResponseDto> resetPassword(
            @Valid @RequestBody ResetPasswordRequestDto request) {

        try {
            ResetPasswordResponseDto response = userService.resetPassword(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ResetPasswordResponseDto(false, e.getMessage()));
        }
    }

    @GetMapping("/reset-password")
    public ResponseEntity<Map<String, Object>> resetPasswordGet(@RequestParam("token") String token) {
        try {
            boolean isValidToken = userService.validateResetToken(token);

            if (isValidToken) {
                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "Valid reset token. Use POST /api/auth/reset-password with this token to reset your password.",
                        "token", token,
                        "instructions", "Send a POST request to /api/auth/reset-password with JSON body: " +
                                "{\"resetToken\":\"" + token + "\", \"newPassword\":\"your_new_password\", " +
                                "\"confirmPassword\":\"your_new_password\"}"
                ));
            } else {
                return ResponseEntity.badRequest()
                        .body(Map.of("success", false, "message", "Invalid or expired reset token"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "Invalid or expired reset token"));
        }
    }

    @PostMapping("/verify-email")
    public ResponseEntity<EmailVerificationResponseDto> verifyEmail(
            @Valid @RequestBody EmailVerificationRequestDto request) {

        try {
            EmailVerificationResponseDto response = userService.verifyEmail(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new EmailVerificationResponseDto(false, e.getMessage(), null));
        }
    }

    @GetMapping("/verify-email")
    public ResponseEntity<EmailVerificationResponseDto> verifyEmailGet(@RequestParam("token") String token) {
        try {
            EmailVerificationRequestDto request = new EmailVerificationRequestDto(token);
            EmailVerificationResponseDto response = userService.verifyEmail(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            String message = e.getMessage();

            if ("Invalid verification token".equals(message)) {
                return ResponseEntity.badRequest()
                        .body(new EmailVerificationResponseDto(false, "Invalid or expired verification token", null));
            } else if ("Email is already verified".equals(message)) {
                return ResponseEntity.badRequest()
                        .body(new EmailVerificationResponseDto(false, "Email is already verified", null));
            } else {
                return ResponseEntity.internalServerError()
                        .body(new EmailVerificationResponseDto(false,
                                message != null ? message : "Internal server error", null));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new EmailVerificationResponseDto(false,
                            e.getMessage() != null ? e.getMessage() : "Internal server error", null));
        }
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<EmailVerificationResponseDto> resendEmailVerification(@RequestParam("email") String email) {
        try {
            EmailVerificationResponseDto response = userService.resendEmailVerification(email);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new EmailVerificationResponseDto(false, e.getMessage(), null));
        }
    }

    @GetMapping("/profile-setup-status")
    public ResponseEntity<ProfileSetupStatusDto> getProfileSetupStatus(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        try {
            ProfileSetupStatusDto response = userService.getProfileSetupStatus(userPrincipal.getId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ProfileSetupStatusDto(false, 0));
        }
    }

    @GetMapping("/check-email")
    public ResponseEntity<EmailExistenceResponse> checkEmailExists(@RequestParam("email") String email) {
        boolean exists = userService.emailExists(email);
        return ResponseEntity.ok(new EmailExistenceResponse(exists));
    }

    @GetMapping("/check-email-verification")
    public ResponseEntity<EmailVerificationStatusResponse> checkEmailVerification(@RequestParam("email") String email) {
        try {
            boolean isVerified = userService.isEmailVerified(email);
            return ResponseEntity.ok(new EmailVerificationStatusResponse(isVerified, email));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new EmailVerificationStatusResponse(false, email, "Failed to check verification status"));
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Authentication service is running");
    }

    public record EmailExistenceResponse(boolean exists) {}

    public record EmailVerificationStatusResponse(boolean verified, String email, String message) {
        public EmailVerificationStatusResponse(boolean verified, String email) {
            this(verified, email, verified ? "Email is verified" : "Email is not verified");
        }
    }
}