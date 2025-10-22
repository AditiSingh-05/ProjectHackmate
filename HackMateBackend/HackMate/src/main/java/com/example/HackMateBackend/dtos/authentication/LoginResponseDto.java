package com.example.HackMateBackend.dtos.authentication;

import com.example.HackMateBackend.data.enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {
    private String token;
    private Long userId;
    private String email;
    private Roles role;
    private boolean isProfileSetup;
    private boolean isEmailVerified;
}

