package com.example.HackMateBackend.dtos.team;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTeamRequestDto {
    @NotNull(message = "Hackathon ID is required")
    private Long hackathonId;

    @NotBlank(message = "Team name is required")
    @Size(min = 3, max = 100, message = "Team name must be between 3 and 100 characters")
    private String teamName;

    @NotBlank(message = "Team description is required")
    @Size(min = 10, max = 1000, message = "Team description must be between 10 and 1000 characters")
    private String description;

    @NotNull(message = "Maximum team size is required")
    @Min(value = 2, message = "Team must have at least 2 members")
    @Max(value = 10, message = "Team cannot have more than 10 members")
    private Integer maxSize;

    @NotNull(message = "At least one required role must be specified")
    @Size(min = 1, message = "At least one role is required")
    private List<String> rolesNeeded;

    private List<String> skillsNeeded;
    private String contactEmail;
    private String contactPhone;
    private String discordServer;
    private String whatsappGroup;
    private String linkedinGroup;
    private String externalGroupLink;
    private boolean isPublic = true;
    private boolean autoAccept = false;
}

