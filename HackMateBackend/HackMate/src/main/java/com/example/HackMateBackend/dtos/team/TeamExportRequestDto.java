package com.example.HackMateBackend.dtos.team;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamExportRequestDto {
    @NotNull(message = "Team ID is required")
    private Long teamId;

    private String format = "PDF"; // PDF, CSV
    private boolean includeContactInfo = true;
    private boolean includeHackathonDetails = true;
}

