package com.example.hackmatefrontendfolder.data.model.user

data class ForgotPasswordResponse(
    val success: Boolean,
    val message: String,
    val resetEmailSent: String?
)