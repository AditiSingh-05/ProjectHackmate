package com.example.hackmatefrontendfolder.domain.model.notification

import java.time.LocalDateTime

data class DeleteNotificationResponseDto(
    val success: Boolean,
    val message: String,
    val deletedAt : LocalDateTime
)
