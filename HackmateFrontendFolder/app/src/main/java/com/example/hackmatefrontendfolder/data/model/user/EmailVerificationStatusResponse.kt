package com.example.hackmatefrontendfolder.data.model.user

data class EmailVerificationStatusResponse(
    val verified: Boolean,
    val email: String,
    val message: String
)
