package com.example.hackmatefrontendfolder.domain.model.hackathon

data class Hackathon(
    val hackathonId: Long,
    val title: String,
    val description: String,
    val registrationLink: String,
    val deadline: String,
    val posterUrl: String?,
    val tags: List<String>,
    val organizer: String?,
    val location: String?,
    val prizePool: String?,
    val viewCount: Long,
    val registrationCount: Long,
    val teamCount: Long,
    val isRegistered: Boolean,
    val isStarred: Boolean,
    val urgencyLevel: String,
    val postedAt: String
)
