package com.example.HackMateBackend.dtos.team;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTeamRequestDto {
    @NotNull(message = "Team ID is required")
    private Long teamId;

    private String teamName;
    private String description;
    private List<String> rolesNeeded;
    private List<String> skillsNeeded;
    private String contactEmail;
    private String contactPhone;
    private String discordServer;
    private String whatsappGroup;
    private String linkedinGroup;
    private String externalGroupLink;
    private boolean isPublic;
    private boolean autoAccept;
}

