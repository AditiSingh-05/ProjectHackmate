package com.example.hackmatefrontendfolder.data.model.user

data class ChangePasswordRequest(
    val currentPassword: String,
    val newPassword: String,
    val confirmNewPassword: String
)