package com.example.hackmatefrontendfolder.domain.model.user

data class ResetPasswordRequest(
    val resetToken: String,
    val newPassword: String,
    val confirmPassword: String
)

