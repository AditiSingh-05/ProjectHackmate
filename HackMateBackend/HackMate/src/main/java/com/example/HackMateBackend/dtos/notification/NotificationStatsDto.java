package com.example.HackMateBackend.dtos.notification;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationStatsDto {
    private int totalNotifications;
    private int unreadCount;
    private int todayCount;
    private int weekCount;
    private int urgentCount;
    private int highPriorityCount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastNotificationAt;
}

