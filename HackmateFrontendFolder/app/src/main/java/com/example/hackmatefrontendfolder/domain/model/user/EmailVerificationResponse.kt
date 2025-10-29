package com.example.hackmatefrontendfolder.domain.model.user

data class EmailVerificationResponse(
    val success: Boolean,
    val message: String,
    val verifiedAt: String?
)

