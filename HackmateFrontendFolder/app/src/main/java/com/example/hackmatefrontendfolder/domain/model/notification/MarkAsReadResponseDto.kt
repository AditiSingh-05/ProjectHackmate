package com.example.hackmatefrontendfolder.domain.model.notification

data class MarkAsReadResponseDto(
    val success: Boolean,
    val message: String,
    val readAt : String
)
