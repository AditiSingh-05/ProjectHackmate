package com.example.HackMateBackend.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;

public class ProfileDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProfileSetupRequestDto {
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

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProfileSetupResponseDto {
        private boolean success;
        private String message;
        private Long userId;
        private String fullName;
        private int profileCompletionPercentage;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProfileUpdateRequestDto {
        private String fullName;
        private String bio;
        private String college;
        private String year;
        private String githubProfile;
        private String linkedinProfile;
        private String portfolioUrl;
        private String mainSkill;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProfileUpdateResponseDto {
        private boolean success;
        private String message;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime updatedAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PrivateProfileResponseDto {
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

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PublicProfileResponseDto {
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

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewDto {
        private Long reviewId;
        private String reviewerName;
        private String reviewerAvatarId;

        @Min(value = 1, message = "Rating must be at least 1")
        @Max(value = 5, message = "Rating cannot exceed 5")
        private int rating;

        private String comment;
        private String hackathonName;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime reviewedAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddReviewRequestDto {
        @NotBlank(message = "User ID to review is required")
        private Long userIdToReview;

        @Min(value = 1, message = "Rating must be at least 1")
        @Max(value = 5, message = "Rating cannot exceed 5")
        private int rating;

        @Size(max = 500, message = "Comment cannot exceed 500 characters")
        private String comment;

        private Long hackathonId; // Optional: which hackathon this review is based on
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddReviewResponseDto {
        private boolean success;
        private String message;
        private Long reviewId;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime reviewedAt;
    }
}