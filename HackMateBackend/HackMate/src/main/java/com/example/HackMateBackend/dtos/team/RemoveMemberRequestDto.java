package com.example.HackMateBackend.dtos.team;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RemoveMemberRequestDto {
    @NotNull(message = "Team ID is required")
    private Long teamId;

    @NotNull(message = "User ID is required")
    private Long userIdToRemove;

    private String reason;
}

