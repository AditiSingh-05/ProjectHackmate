package com.example.hackmatefrontendfolder.data.model.profile

data class ProfileUpdateRequest(
    val fullName: String? = null,
    val bio: String? = null,
    val college: String? = null,
    val year: String? = null,
    val githubProfile: String? = null,
    val linkedinProfile: String? = null,
    val portfolioUrl: String? = null,
    val mainSkill: String? = null,
    val skills: List<String>? = null
)

