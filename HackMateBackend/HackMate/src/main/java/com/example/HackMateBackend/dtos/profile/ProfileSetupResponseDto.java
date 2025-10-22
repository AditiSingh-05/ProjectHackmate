package com.example.HackMateBackend.dtos.profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileSetupResponseDto {
    private boolean success;
    private String message;
    private Long userId;
    private String fullName;
    private int profileCompletionPercentage;
}

