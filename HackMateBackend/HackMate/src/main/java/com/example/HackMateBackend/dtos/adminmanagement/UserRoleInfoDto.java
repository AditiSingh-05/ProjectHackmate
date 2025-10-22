package com.example.HackMateBackend.dtos.adminmanagement;

import com.example.HackMateBackend.data.enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleInfoDto {
    private String email;
    private Roles role;
    private boolean emailVerified;
    private boolean profileSetup;
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;
}

