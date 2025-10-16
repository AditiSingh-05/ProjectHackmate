package com.example.HackMateBackend.dtos;

import com.example.HackMateBackend.data.enums.Roles;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


public class AuthenticationDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginRequestDto {
        @NotBlank(message = "Email is required")
        @Email(message = "Please provide a valid email address")
        private String email;

        @NotBlank(message = "Password is required")
        @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters")
        private String password;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignupRequestDto {
        @NotBlank(message = "Email is required")
        @Email(message = "Please provide a valid email address")
        private String email;

        @NotBlank(message = "Password is required")
        @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters")
        private String password;

        @NotBlank(message = "Confirm password is required")
        private String confirmPassword;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginResponseDto {
        private String token;
        private Long userId;
        private String email;
        private Roles role;
        private boolean isProfileSetup;
        private boolean isEmailVerified;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignupResponseDto {
        private Long userId;
        private String email;
        private String message;
        private Roles role;
        private boolean isEmailVerified;
        private String verificationEmailSent;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChangePasswordRequestDto {
        @NotBlank(message = "Current password is required")
        private String currentPassword;

        @NotBlank(message = "New password is required")
        @Size(min = 6, max = 20, message = "New password must be between 6 and 20 characters")
        private String newPassword;

        @NotBlank(message = "Confirm new password is required")
        private String confirmNewPassword;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChangePasswordResponseDto {
        private boolean success;
        private String message;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime changedAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ForgotPasswordRequestDto {
        @NotBlank(message = "Email is required")
        @Email(message = "Please provide a valid email address")
        private String email;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ForgotPasswordResponseDto {
        private boolean success;
        private String message;
        private String resetEmailSent;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResetPasswordRequestDto {
        @NotBlank(message = "Reset token is required")
        private String resetToken;

        @NotBlank(message = "New password is required")
        @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters")
        private String newPassword;

        @NotBlank(message = "Confirm password is required")
        private String confirmPassword;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResetPasswordResponseDto {
        private boolean success;
        private String message;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RefreshTokenRequestDto {
        @NotBlank(message = "Refresh token is required")
        private String refreshToken;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RefreshTokenResponseDto {
        private String accessToken;
        private String refreshToken;
        private String tokenType = "Bearer";

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime refreshedAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmailVerificationRequestDto {
        @NotBlank(message = "Verification token is required")
        private String verificationToken;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmailVerificationResponseDto {
        private boolean success;
        private String message;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime verifiedAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProfileSetupStatusDto {
        private boolean isProfileSetup;
        private int completionPercentage;
    }
}