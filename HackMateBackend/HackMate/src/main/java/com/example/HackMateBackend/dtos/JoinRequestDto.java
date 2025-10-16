package com.example.HackMateBackend.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;

public class JoinRequestDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SendJoinRequestDto {
        @NotNull(message = "Team ID is required")
        private Long teamId;

        @NotBlank(message = "Requested role is required")
        private String requestedRole;

        @NotNull(message = "User skills are required")
        @Size(min = 1, message = "At least one skill must be provided")
        private List<String> userSkills;

        @NotBlank(message = "Message is required")
        @Size(min = 10, max = 500, message = "Message must be between 10 and 500 characters")
        private String message;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SendJoinRequestResponseDto {
        private boolean success;
        private String message;
        private Long joinRequestId;
        private String teamName;
        private String status; // PENDING, ACCEPTED, REJECTED, EXPIRED

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime sentAt;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime expiresAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JoinRequestListItemDto {
        private Long joinRequestId;
        private Long teamId;
        private String teamName;
        private String hackathonTitle;

        // Requester information (for team leaders viewing requests)
        private Long requesterId;
        private String requesterName;
        private String requesterAvatarId;
        private String requesterCollege;
        private String requesterYear;
        private String requestedRole;
        private List<String> requesterSkills;
        private String message;

        // Matching information
        private List<String> matchingSkills;
        private int matchPercentage;

        private String status; // PENDING, ACCEPTED, REJECTED, EXPIRED

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime sentAt;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime expiresAt;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime processedAt;

        private boolean isExpired;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProcessJoinRequestDto {
        @NotNull(message = "Join request ID is required")
        private Long joinRequestId;

        @NotBlank(message = "Action is required")
        private String action; // ACCEPT, REJECT

        @Size(max = 200, message = "Response message cannot exceed 200 characters")
        private String responseMessage;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProcessJoinRequestResponseDto {
        private boolean success;
        private String message;
        private String action; // ACCEPTED, REJECTED
        private String requesterName;
        private String teamName;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime processedAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyJoinRequestsResponseDto {
        private List<MyJoinRequestItemDto> activeRequests;
        private List<MyJoinRequestItemDto> pastRequests;
        private boolean hasActiveRequest; // Can only have 1 active request
        private String nextAvailableRequestTime; // If cooldown period exists
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyJoinRequestItemDto {
        private Long joinRequestId;
        private Long teamId;
        private String teamName;
        private String hackathonTitle;
        private String requestedRole;
        private List<String> userSkills;
        private String message;
        private String status; // PENDING, ACCEPTED, REJECTED, EXPIRED
        private String responseMessage; // From team leader

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime sentAt;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime expiresAt;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime processedAt;

        private boolean canCancel;
        private boolean isExpired;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeamJoinRequestsResponseDto {
        private List<JoinRequestListItemDto> pendingRequests;
        private List<JoinRequestListItemDto> processedRequests;
        private int totalPending;
        private int totalProcessed;
        private String teamName;
        private Integer availableSlots;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CancelJoinRequestDto {
        @NotNull(message = "Join request ID is required")
        private Long joinRequestId;

        private String reason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CancelJoinRequestResponseDto {
        private boolean success;
        private String message;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime cancelledAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JoinRequestStatsDto {
        private int totalSent;
        private int totalAccepted;
        private int totalRejected;
        private int totalExpired;
        private int currentPending;
        private double acceptanceRate;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime firstRequestSent;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime lastRequestSent;
    }
}