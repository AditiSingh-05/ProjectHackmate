package com.example.hackmatefrontendfolder.domain.model.profile

data class PrivateProfileResponse(
    val userId: Long,
    val email: String,
    val fullName: String,
    val bio: String?,
    val hackathonsParticipated: Int,
    val hackathonsWon: Int,
    val college: String,
    val year: String,
    val githubProfile: String?,
    val linkedinProfile: String?,
    val portfolioUrl: String?,
    val avatarId: String?,
    val mainSkill: String?,
    val skills: List<String>,
    val averageRating: Double,
    val totalReviews: Int,
    val recentReviews: List<Review>
)

