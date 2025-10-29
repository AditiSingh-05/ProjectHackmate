package com.example.hackmatefrontendfolder.domain.model.hackathon


data class CreateHackathonRequest(
    val title: String,
    val description: String,
    val registrationLink: String,
    val deadline: String,
    val posterUrl: String? = null,
    val tags: List<String> = emptyList(),
    val organizer: String? = null,
    val location: String? = null,
    val prizePool: String? = null,
    val eventStartDate: String? = null,
    val eventEndDate: String? = null,
    val maxTeamSize: Int? = null,
    val minTeamSize: Int? = null,
    val originalMessage: String? = null,
    val contactEmail: String? = null
)
