package com.example.hackmatefrontendfolder.domain.model.notification

data class UpdateNotificationPreferencesRequestDto(
    val emailNotifications : Boolean,
    val pushNotifications : Boolean,
    val hackathonDeadlineReminders : Boolean,
    val joinRequestUpdates : Boolean,
    val teamUpdates : Boolean,
    val generalAnnouncements : Boolean,
    val reminderHoursBefore : Int
)
