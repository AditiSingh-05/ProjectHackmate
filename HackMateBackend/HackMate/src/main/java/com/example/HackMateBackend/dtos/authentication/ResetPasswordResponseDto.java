package com.example.HackMateBackend.dtos.authentication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordResponseDto {
    private boolean success;
    private String message;
}

