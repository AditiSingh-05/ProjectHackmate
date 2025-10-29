package com.example.hackmatefrontendfolder.domain.model.hackathon


data class HackathonListResponse(
    val hackathons: List<Hackathon>,
    val currentPage: Int,
    val totalPages: Int,
    val totalElements: Long,
    val hasNext: Boolean,
    val hasPrevious: Boolean
)

