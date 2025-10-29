package com.example.hackmatefrontendfolder.domain.model.hackathon


data class RegistrationToggleResponse(
    val success: Boolean,
    val message: String,
    val isRegistered: Boolean,
    val actionAt: String?
)

