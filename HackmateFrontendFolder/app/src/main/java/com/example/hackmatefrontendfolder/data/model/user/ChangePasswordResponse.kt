package com.example.hackmatefrontendfolder.data.model.user

data class ChangePasswordResponse(
    val success: Boolean,
    val message: String,
    val changedAt: String?
)