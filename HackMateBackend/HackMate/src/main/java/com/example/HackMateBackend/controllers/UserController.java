package com.example.HackMateBackend.controllers;


import com.example.HackMateBackend.data.enums.Roles;
import com.example.HackMateBackend.dtos.AuthenticationDto.*;
import com.example.HackMateBackend.services.interfaces.UserService;
import com.example.HackMateBackend.services.implementations.CustomUserDetailService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserService userService;
    private static final Logger log = LoggerFactory.getLogger(CustomUserDetailService.class);


    @PostMapping(value = "/signup", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequestDto signupRequest) {
        log.info("AuthenticationVerification: 🎯 Signup endpoint hit for email: {}", signupRequest.getEmail());
        log.info("AuthenticationVerification: Request body received - Email: {}, Password length: {}, ConfirmPassword length: {}",
                signupRequest.getEmail(),
                signupRequest.getPassword() != null ? signupRequest.getPassword().length() : 0,
                signupRequest.getConfirmPassword() != null ? signupRequest.getConfirmPassword().length() : 0);

        try {
            log.info("AuthenticationVerification: Calling userService.registerUser");
            SignupResponseDto response = userService.registerUser(signupRequest);
            log.info("AuthenticationVerification: ✅ User service returned successful response for: {}", signupRequest.getEmail());
            log.info("AuthenticationVerification: Response - UserId: {}, EmailVerified: {}, Message: {}",
                    response.getUserId(), response.isEmailVerified(), response.getMessage());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("AuthenticationVerification: ❌ User registration failed for email: {}", signupRequest.getEmail());
            log.error("AuthenticationVerification: Controller exception type: {}", e.getClass().getSimpleName());
            log.error("AuthenticationVerification: Controller exception message: {}", e.getMessage());
            log.error("AuthenticationVerification: Full controller stack trace:", e);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new SignupResponseDto(null, signupRequest.getEmail(), e.getMessage(), Roles.USER, false, null));
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequestDto loginRequest) {
        log.info("Login request received for email: {}", loginRequest.getEmail());

        try {
            LoginResponseDto response = userService.loginUser(loginRequest);
            log.info("User login successful for email: {}", loginRequest.getEmail());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("User login failed for email: {}", loginRequest.getEmail(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponseDto(null, null, null, null, false, false));
        }
    }


    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @AuthenticationPrincipal CustomUserDetailService.UserPrincipal userPrincipal,
            @Valid @RequestBody ChangePasswordRequestDto request) {

        log.info("Change password request for user ID: {}", userPrincipal.getId());

        try {
            ChangePasswordResponseDto response = userService.changePassword(userPrincipal.getId(), request);
            log.info("Password changed successfully for user ID: {}", userPrincipal.getId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Password change failed for user ID: {}", userPrincipal.getId(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ChangePasswordResponseDto(false, e.getMessage(), null));
        }
    }


    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequestDto request) {
        log.info("Forgot password request for email: {}", request.getEmail());

        try {
            ForgotPasswordResponseDto response = userService.forgotPassword(request);
            log.info("Forgot password processed for email: {}", request.getEmail());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Forgot password failed for email: {}", request.getEmail(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ForgotPasswordResponseDto(false, "Failed to process request", null));
        }
    }


    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequestDto request) {
        log.info("Reset password request received");

        try {
            ResetPasswordResponseDto response = userService.resetPassword(request);
            log.info("Password reset successful");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Password reset failed", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResetPasswordResponseDto(false, e.getMessage()));
        }
    }

    @GetMapping("/reset-password")
    public ResponseEntity<?> resetPasswordGet(@RequestParam("token") String token) {
        log.info("Password reset via GET request with token");

        try {
            // Validate that the token exists and is not expired
            boolean isValidToken = userService.validateResetToken(token);
            if (isValidToken) {
                // Return a simple response indicating the token is valid
                // In a real application, you might redirect to a frontend page
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Valid reset token. Use POST /api/auth/reset-password with this token to reset your password.",
                    "token", token,
                    "instructions", "Send a POST request to /api/auth/reset-password with JSON body: {\"resetToken\":\"" + token + "\", \"newPassword\":\"your_new_password\", \"confirmPassword\":\"your_new_password\"}"
                ));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("success", false, "message", "Invalid or expired reset token"));
            }
        } catch (Exception e) {
            log.error("Password reset validation failed via GET", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("success", false, "message", "Invalid or expired reset token"));
        }
    }


    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@Valid @RequestBody EmailVerificationRequestDto request) {
        log.info("Email verification request received");

        try {
            EmailVerificationResponseDto response = userService.verifyEmail(request);
            log.info("Email verification successful");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Email verification failed", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new EmailVerificationResponseDto(false, e.getMessage(), null));
        }
    }

    @PostMapping("/testjson")
    public Map<String, String> testJson() {
        return Map.of("result", "ok");
    }


    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyEmailGet(@RequestParam("token") String token) {
        log.info("Email verification via GET request");

        try {
            EmailVerificationRequestDto request = new EmailVerificationRequestDto(token);
            EmailVerificationResponseDto response = userService.verifyEmail(request);
            log.info("Email verification successful via GET");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Email verification failed via GET", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new EmailVerificationResponseDto(false, e.getMessage(), null));
        }
    }


    @PostMapping("/resend-verification")
    public ResponseEntity<?> resendEmailVerification(@RequestParam("email") String email) {
        log.info("Resend email verification request for: {}", email);

        try {
            EmailVerificationResponseDto response = userService.resendEmailVerification(email);
            log.info("Email verification resent successfully to: {}", email);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Failed to resend email verification to: {}", email, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new EmailVerificationResponseDto(false, e.getMessage(), null));
        }
    }


    @GetMapping("/profile-setup-status")
    public ResponseEntity<?> getProfileSetupStatus(
            @AuthenticationPrincipal CustomUserDetailService.UserPrincipal userPrincipal) {

        log.info("Profile setup status request for user ID: {}", userPrincipal.getId());

        try {
            ProfileSetupStatusDto response = userService.getProfileSetupStatus(userPrincipal.getId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Failed to get profile setup status for user ID: {}", userPrincipal.getId(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ProfileSetupStatusDto(false, 0));
        }
    }


    @GetMapping("/check-email")
    public ResponseEntity<?> checkEmailExists(@RequestParam("email") String email) {
        log.info("Email existence check for: {}", email);

        boolean exists = userService.emailExists(email);
        return ResponseEntity.ok(new EmailExistenceResponse(exists));
    }


    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Authentication service is running");
    }


    public static class EmailExistenceResponse {
        private boolean exists;

        public EmailExistenceResponse(boolean exists) {
            this.exists = exists;
        }

        public boolean isExists() {
            return exists;
        }

        public void setExists(boolean exists) {
            this.exists = exists;
        }
    }
}
