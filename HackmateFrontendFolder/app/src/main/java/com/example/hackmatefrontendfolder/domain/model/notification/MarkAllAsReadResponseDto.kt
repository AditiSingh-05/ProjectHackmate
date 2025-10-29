package com.example.hackmatefrontendfolder.domain.model.notification

data class MarkAllAsReadResponseDto(
    val success: Boolean,
    val message: String,
    val markedCount : Int,
    val readAt : String
)

