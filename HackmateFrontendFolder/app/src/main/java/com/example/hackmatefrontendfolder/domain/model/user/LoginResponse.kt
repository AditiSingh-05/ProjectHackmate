package com.example.hackmatefrontendfolder.domain.model.user

data class LoginResponse(
    val token: String?,
    val userId: Long?,
    val email: String?,
    val role: String?,
    val isProfileSetup: Boolean,
    val isEmailVerified: Boolean
)
