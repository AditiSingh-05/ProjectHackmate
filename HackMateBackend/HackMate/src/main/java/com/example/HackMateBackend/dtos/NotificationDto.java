package com.example.HackMateBackend.dtos;

import com.example.HackMateBackend.data.enums.NotificationType;
import com.example.HackMateBackend.data.enums.Priority;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;

public class NotificationDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotificationItemDto {
        private Long notificationId;
        private String title;
        private String message;
        private String type; // HACKATHON_DEADLINE, JOIN_REQUEST, TEAM_UPDATE, GENERAL
        private Priority priority; // LOW, MEDIUM, HIGH, URGENT
        private boolean isRead;
        private String actionUrl; // Deep link for mobile app
        private Long relatedEntityId; // ID of related hackathon, team, etc.
        private String relatedEntityType; // HACKATHON, TEAM, JOIN_REQUEST, etc.

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createdAt;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime readAt;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime expiresAt;

        private boolean isExpired;

        public NotificationItemDto(Long id, String title, String message, NotificationType type, Priority priority, boolean read, String actionUrl, Long relatedEntityId, String relatedEntityType, LocalDateTime createdAt, LocalDateTime readAt, LocalDateTime expiresAt, boolean expired) {
            this.notificationId = id;
            this.title = title;
            this.message = message;
            this.type = type.name();
            this.priority = priority;
            this.isRead = read;
            this.actionUrl = actionUrl;
            this.relatedEntityId = relatedEntityId;
            this.relatedEntityType = relatedEntityType;
            this.createdAt = createdAt;
            this.readAt = readAt;
            this.expiresAt = expiresAt;
            this.isExpired = expired;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotificationListResponseDto {
        private List<NotificationItemDto> notifications;
        private int unreadCount;
        private int totalCount;
        private boolean hasMore;
        private int page;
        private int size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MarkAsReadRequestDto {
        @NotNull(message = "Notification ID is required")
        private Long notificationId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MarkAsReadResponseDto {
        private boolean success;
        private String message;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime readAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MarkAllAsReadResponseDto {
        private boolean success;
        private String message;
        private int markedCount;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime readAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotificationStatsDto {
        private int totalNotifications;
        private int unreadCount;
        private int todayCount;
        private int weekCount;
        private int urgentCount;
        private int highPriorityCount;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime lastNotificationAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotificationPreferencesDto {
        private boolean emailNotifications;
        private boolean pushNotifications;
        private boolean hackathonDeadlineReminders;
        private boolean joinRequestUpdates;
        private boolean teamUpdates;
        private boolean generalAnnouncements;
        private int reminderHoursBefore; // Hours before deadline to send reminder
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateNotificationPreferencesRequestDto {
        private boolean emailNotifications;
        private boolean pushNotifications;
        private boolean hackathonDeadlineReminders;
        private boolean joinRequestUpdates;
        private boolean teamUpdates;
        private boolean generalAnnouncements;
        private int reminderHoursBefore;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateNotificationPreferencesResponseDto {
        private boolean success;
        private String message;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime updatedAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteNotificationRequestDto {
        @NotNull(message = "Notification ID is required")
        private Long notificationId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteNotificationResponseDto {
        private boolean success;
        private String message;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime deletedAt;
    }
}