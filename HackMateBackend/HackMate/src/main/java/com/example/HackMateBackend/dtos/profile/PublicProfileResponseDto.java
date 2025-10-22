package com.example.HackMateBackend.dtos.profile;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicProfileResponseDto {
    private Long userId;
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
    private List<ReviewDto> publicReviews;
    private List<String> badges;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime joinedAt;
}

