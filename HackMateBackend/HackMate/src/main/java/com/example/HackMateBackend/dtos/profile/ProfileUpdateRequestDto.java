package com.example.HackMateBackend.dtos.profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileUpdateRequestDto {
    private String fullName;
    private String bio;
    private String college;
    private String year;
    private String githubProfile;
    private String linkedinProfile;
    private String portfolioUrl;
    private String mainSkill;
}

