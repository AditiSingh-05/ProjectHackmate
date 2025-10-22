package com.example.HackMateBackend.services.interfaces;

import com.example.HackMateBackend.dtos.notification.*;
import com.example.HackMateBackend.data.entities.JoinRequest;
import com.example.HackMateBackend.data.entities.Hackathon;

public interface NotificationService {

    // Notification management
    NotificationListResponseDto getNotifications(Long userId, int page, int size);
    MarkAsReadResponseDto markAsRead(MarkAsReadRequestDto request, Long userId);
    MarkAllAsReadResponseDto markAllAsRead(Long userId);
    DeleteNotificationResponseDto deleteNotification(DeleteNotificationRequestDto request, Long userId);

    // Notification preferences
    NotificationPreferencesDto getNotificationPreferences(Long userId);
    UpdateNotificationPreferencesResponseDto updateNotificationPreferences(
            UpdateNotificationPreferencesRequestDto request, Long userId);

    // Statistics
    NotificationStatsDto getNotificationStats(Long userId);

    // System notification creation
    void createJoinRequestNotification(JoinRequest joinRequest);
    void createJoinRequestResponseNotification(JoinRequest joinRequest, boolean accepted);
    void createHackathonDeadlineNotification(Hackathon hackathon, Long userId, int hoursRemaining);
    void createTeamUpdateNotification(Long teamId, String message);

    // Cleanup
    void cleanupExpiredNotifications();
}