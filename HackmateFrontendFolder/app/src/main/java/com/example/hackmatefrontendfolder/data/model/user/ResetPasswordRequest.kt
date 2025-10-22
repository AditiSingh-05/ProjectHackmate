package com.example.hackmatefrontendfolder.data.model.user

data class ResetPasswordRequest(
    val resetToken: String,
    val newPassword: String,
    val confirmPassword: String
)

