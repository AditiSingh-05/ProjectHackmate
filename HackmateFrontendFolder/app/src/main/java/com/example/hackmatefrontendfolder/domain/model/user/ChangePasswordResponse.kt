package com.example.hackmatefrontendfolder.domain.model.user

data class ChangePasswordResponse(
    val success: Boolean,
    val message: String,
    val changedAt: String?
)