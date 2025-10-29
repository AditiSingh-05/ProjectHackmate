package com.example.hackmatefrontendfolder.domain.model.notification

data class NotificationStatsDto(
    val totalNotifications : Int,
    val unreadCount : Int,
    val todayCount : Int,
    val weekCount : Int,
    val urgentCount : Int,
    val highPriorityCount : Int,
    val lastNotificationAt : String
)
