package com.example.HackMateBackend.dtos;

import com.example.HackMateBackend.data.enums.RegistrationStatus;
import com.example.HackMateBackend.data.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

public class HackathonDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateHackathonRequestDto {
        @NotBlank(message = "Title is required")
        @Size(min = 5, max = 200, message = "Title must be between 5 and 200 characters")
        private String title;

        @NotBlank(message = "Description is required")
        @Size(min = 20, max = 2000, message = "Description must be between 20 and 2000 characters")
        private String description;

        @NotBlank(message = "Registration link is required")
        private String registrationLink;

        @NotNull(message = "Deadline is required")
        @Future(message = "Deadline must be in the future")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime deadline;

        private String posterUrl;
        private List<String> tags;
        private String organizer;
        private String location;
        private String prizePool;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime eventStartDate;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime eventEndDate;

        private Integer maxTeamSize;
        private Integer minTeamSize;
        private String originalMessage;
        private String contactEmail;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateHackathonResponseDto {
        private boolean success;
        private String message;
        private Long hackathonId;
        private Status status;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime submittedAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HackathonListItemDto {
        private Long hackathonId;
        private String title;
        private String description;
        private String posterUrl;
        private List<String> tags;
        private String organizer;
        private String location;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime deadline;

        private String prizePool;
        private long viewCount;
        private long registrationCount;
        private long teamCount;
        private boolean isRegistered;
        private boolean isStarred;
        private String urgencyLevel;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime postedAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HackathonDetailsResponseDto {
        private Long hackathonId;
        private String title;
        private String description;
        private String registrationLink;
        private String posterUrl;
        private List<String> tags;
        private String organizer;
        private String location;
        private String prizePool;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime deadline;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime eventStartDate;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime eventEndDate;

        private Integer maxTeamSize;
        private Integer minTeamSize;
        private String contactEmail;
        private long viewCount;
        private long registrationCount;
        private long teamCount;
        private String status;
        private boolean isRegistered;
        private boolean isStarred;
        private boolean isExpired;
        private String urgencyLevel;
        private String postedBy;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime postedAt;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime approvedAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HackathonFilterRequestDto {
        private String search;
        private List<String> tags;
        private RegistrationStatus status;
        private String urgencyLevel;
        private String organizer;
        private String location;
        private boolean showExpired = false;
        private String sortBy = "deadline";
        private String sortDirection = "asc";
        private int page = 0;
        private int size = 20;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HackathonListResponseDto {
        private List<HackathonListItemDto> hackathons;
        private int currentPage;
        private int totalPages;
        private long totalElements;
        private boolean hasNext;
        private boolean hasPrevious;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegistrationToggleRequestDto {
        @NotNull(message = "Hackathon ID is required")
        private Long hackathonId;
        private boolean register;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegistrationToggleResponseDto {
        private boolean success;
        private String message;
        private boolean isRegistered;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime actionAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StarToggleRequestDto {
        @NotNull(message = "Hackathon ID is required")
        private Long hackathonId;
        private boolean star;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StarToggleResponseDto {
        private boolean success;
        private String message;
        private boolean isStarred;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime actionAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AIExtractionRequestDto {
        @NotBlank(message = "Message text is required for extraction")
        @Size(min = 50, message = "Message must be at least 50 characters for proper extraction")
        private String messageText;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AIExtractionResponseDto {
        private boolean success;
        private String extractedTitle;
        private String extractedDescription;
        private String extractedRegistrationLink;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime extractedDeadline;

        private List<String> extractedTags;
        private String extractedOrganizer;
        private String extractedLocation;
        private String extractedPrizePool;
        private double confidenceScore;
        private String errorMessage;
    }
}
