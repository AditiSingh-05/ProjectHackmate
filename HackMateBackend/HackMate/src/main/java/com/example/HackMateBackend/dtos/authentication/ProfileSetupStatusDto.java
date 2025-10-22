package com.example.HackMateBackend.dtos.authentication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileSetupStatusDto {
    private boolean isProfileSetup;
    private int completionPercentage;
}

