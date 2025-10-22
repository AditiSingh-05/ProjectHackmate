package com.example.HackMateBackend.dtos.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateNotificationPreferencesRequestDto {
    private boolean emailNotifications;
    private boolean pushNotifications;
    private boolean hackathonDeadlineReminders;
    private boolean joinRequestUpdates;
    private boolean teamUpdates;
    private boolean generalAnnouncements;
    private int reminderHoursBefore;
}

