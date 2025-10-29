package com.example.hackmatefrontendfolder.domain.model.notification

import com.example.hackmatefrontendfolder.domain.enums.Priority
import java.time.LocalDateTime

data class NotificationItemDto (
    val notificationId : Long,
    val title : String,
    val message : String,
    val type : String,
    val priority : Priority,
    val isRead : Boolean,
    val actionURLForDeepLink : String,
    val relatedEntityId : Long,
    val relatedEntityType : String,
    val createdAt : LocalDateTime,
    val readAt : LocalDateTime,
    val expiresAt : LocalDateTime,
    val isExpired : Boolean
)