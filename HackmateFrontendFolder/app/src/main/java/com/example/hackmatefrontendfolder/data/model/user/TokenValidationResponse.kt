package com.example.hackmatefrontendfolder.data.model.user

data class TokenValidationResponse(
    val success: Boolean,
    val message: String,
    val token: String?,
    val instructions: String?
)

