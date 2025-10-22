package com.example.HackMateBackend.dtos.authentication;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenRequestDto {
    @NotBlank(message = "Refresh token is required")
    private String refreshToken;
}

