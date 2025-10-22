package com.example.hackmatefrontendfolder.data.model.hackathon


data class CreateHackathonResponse(
    val success: Boolean,
    val message: String,
    val hackathonId: Long?,
    val status: String?,
    val submittedAt: String?
)
