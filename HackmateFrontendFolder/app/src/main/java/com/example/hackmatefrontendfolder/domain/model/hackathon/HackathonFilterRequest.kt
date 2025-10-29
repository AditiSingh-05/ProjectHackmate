package com.example.hackmatefrontendfolder.domain.model.hackathon

data class HackathonFilterRequest(
    val search: String?,
    val tags: List<String>?,
    val status: String?,
    val urgencyLevel: String?,
    val organizer: String?,
    val location: String?,
    val showExpired: Boolean,
    val sortBy: String?,
    val sortDirection: String,
    val page: Int,
    val size: Int
)
