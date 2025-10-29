package com.example.hackmatefrontendfolder.domain.model.profile

data class ProfileSetupRequest(
    val fullName: String,
    val bio: String? = null,
    val hackathonsParticipated: Int = 0,
    val hackathonsWon: Int = 0,
    val college: String,
    val year: String,
    val githubProfile: String? = null,
    val linkedinProfile: String? = null,
    val portfolioUrl: String? = null,
    val avatarId: String? = null,
    val mainSkill: String? = null,
    val skills: List<String> = emptyList()
)

