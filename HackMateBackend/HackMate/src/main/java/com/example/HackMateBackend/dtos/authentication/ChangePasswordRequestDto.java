package com.example.HackMateBackend.dtos.authentication;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequestDto {
    @NotBlank(message = "Current password is required")
    private String currentPassword;

    @NotBlank(message = "New password is required")
    @Size(min = 6, max = 20, message = "New password must be between 6 and 20 characters")
    private String newPassword;

    @NotBlank(message = "Confirm new password is required")
    private String confirmNewPassword;
}

