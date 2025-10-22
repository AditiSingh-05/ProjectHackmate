package com.example.HackMateBackend.dtos.adminmanagement;

import com.example.HackMateBackend.data.enums.Roles;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeUserRoleRequestDto {
    @NotNull(message = "User email is required")
    private String email;

    @NotNull(message = "Role is required")
    private Roles newRole;
}

