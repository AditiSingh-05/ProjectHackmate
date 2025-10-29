package com.example.hackmatefrontendfolder.domain.model.notification

data class NotificationListResponseDto(
    val notifications : List<NotificationItemDto>,
    val unreadCount : Int,
    val totalCount : Int,
    val hasMore : Boolean,
    val page : Int,
    val size : Int
)
