package com.example.HackMateBackend.dtos.profile;



import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileSetupRequestDto {
    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;

    private String bio;

    @Min(value = 0, message = "Hackathons participated cannot be negative")
    private int hackathonsParticipated = 0;

    @Min(value = 0, message = "Hackathons won cannot be negative")
    private int hackathonsWon = 0;

    @NotBlank(message = "College is required")
    private String college;

    @NotBlank(message = "Academic year is required")
    private String year;

    private String githubProfile;
    private String linkedinProfile;
    private String portfolioUrl;
    private String avatarId;
    private String mainSkill;
}