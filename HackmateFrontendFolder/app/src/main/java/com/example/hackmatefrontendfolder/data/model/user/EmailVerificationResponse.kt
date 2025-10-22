package com.example.hackmatefrontendfolder.data.model.user

data class EmailVerificationResponse(
    val success: Boolean,
    val message: String,
    val verifiedAt: String?
)

