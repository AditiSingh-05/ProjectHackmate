package com.example.hackmatefrontendfolder.data.model.hackathon


data class RegistrationToggleResponse(
    val success: Boolean,
    val message: String,
    val isRegistered: Boolean,
    val actionAt: String?
)

