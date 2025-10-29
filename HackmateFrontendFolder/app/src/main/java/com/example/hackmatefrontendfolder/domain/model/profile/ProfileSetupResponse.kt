package com.example.hackmatefrontendfolder.domain.model.profile

data class ProfileSetupResponse(
    val success: Boolean,
    val message: String,
    val userId: Long?,
    val fullName: String?,
    val profileCompletionPercentage: Int
)

