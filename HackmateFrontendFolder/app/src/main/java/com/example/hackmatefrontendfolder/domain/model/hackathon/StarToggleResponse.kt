package com.example.hackmatefrontendfolder.domain.model.hackathon


data class StarToggleResponse(
    val success: Boolean,
    val message: String,
    val isStarred: Boolean,
    val actionAt: String?
)

