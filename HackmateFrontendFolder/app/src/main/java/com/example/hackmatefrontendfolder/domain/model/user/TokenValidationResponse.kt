package com.example.hackmatefrontendfolder.domain.model.user

data class TokenValidationResponse(
    val success: Boolean,
    val message: String,
    val token: String?,
    val instructions: String?
)

