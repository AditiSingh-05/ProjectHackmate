package com.example.hackmatefrontendfolder.domain.model.user

data class ChangePasswordRequest(
    val currentPassword: String,
    val newPassword: String,
    val confirmNewPassword: String
)