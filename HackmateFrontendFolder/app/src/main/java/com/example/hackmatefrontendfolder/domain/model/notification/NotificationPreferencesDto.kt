package com.example.hackmatefrontendfolder.domain.model.notification

data class NotificationPreferencesDto(

    val emailNotifications : Boolean,
    val pushNotifications : Boolean,
    val hackathonDeadlineReminders : Boolean,
    val joinRequestUpdates : Boolean,
    val teamUpdates : Boolean,
    val generalAnnouncements : Boolean,
    val reminderHoursBefore : Int

)
