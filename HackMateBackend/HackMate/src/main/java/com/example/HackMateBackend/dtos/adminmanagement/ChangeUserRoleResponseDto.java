package com.example.HackMateBackend.dtos.adminmanagement;

import com.example.HackMateBackend.data.enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeUserRoleResponseDto {
    private boolean success;
    private String message;
    private String email;
    private Roles previousRole;
    private Roles newRole;
    private LocalDateTime updatedAt;
}

