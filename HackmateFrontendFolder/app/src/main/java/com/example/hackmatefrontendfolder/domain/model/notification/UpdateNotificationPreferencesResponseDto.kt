package com.example.hackmatefrontendfolder.domain.model.notification

import java.time.LocalDateTime

data class UpdateNotificationPreferencesResponseDto(
    val success: Boolean,
    val message: String,
    val preferencesUpdatedAt: LocalDateTime
)
