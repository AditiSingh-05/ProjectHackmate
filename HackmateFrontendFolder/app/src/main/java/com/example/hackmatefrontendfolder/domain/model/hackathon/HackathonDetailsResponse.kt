package com.example.hackmatefrontendfolder.domain.model.hackathon


data class HackathonDetailsResponse(
    val hackathonId: Long,
    val title: String,
    val description: String,
    val registrationLink: String,
    val posterUrl: String?,
    val tags: List<String>,
    val organizer: String?,
    val location: String?,
    val prizePool: String?,
    val deadline: String,
    val eventStartDate: String?,
    val eventEndDate: String?,
    val maxTeamSize: Int?,
    val minTeamSize: Int?,
    val contactEmail: String?,
    val viewCount: Long,
    val registrationCount: Long,
    val teamCount: Long,
    val status: String,
    val isRegistered: Boolean,
    val isStarred: Boolean,
    val isExpired: Boolean,
    val urgencyLevel: String,
    val postedBy: String,
    val postedAt: String,
    val approvedAt: String?
)