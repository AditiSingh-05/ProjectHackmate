package com.example.hackmatefrontendfolder.domain.model.user

data class ForgotPasswordResponse(
    val success: Boolean,
    val message: String,
    val resetEmailSent: String?
)