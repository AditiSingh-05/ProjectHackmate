package com.example.hackmatefrontendfolder.domain.model.user

data class EmailVerificationStatusResponse(
    val verified: Boolean,
    val email: String,
    val message: String
)
