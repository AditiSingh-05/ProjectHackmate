package com.example.HackMateBackend.dtos.notification;

import com.example.HackMateBackend.data.enums.NotificationType;
import com.example.HackMateBackend.data.enums.Priority;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationItemDto {
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

