package com.example.HackMateBackend.dtos.profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrivateProfileResponseDto {
    private Long userId;
    private String email;
    private String fullName;
    private String bio;
    private int hackathonsParticipated;
    private int hackathonsWon;
    private String college;
    private String year;
    private String githubProfile;
    private String linkedinProfile;
    private String portfolioUrl;
    private String avatarId;
    private String mainSkill;
    private double averageRating;
    private int totalReviews;
    private List<ReviewDto> recentReviews;
}

