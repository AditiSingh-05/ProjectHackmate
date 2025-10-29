package com.example.hackmatefrontendfolder.domain.model.profile

data class Review(
    val reviewId: Long,
    val reviewerName: String,
    val reviewerAvatarId: String?,
    val rating: Int,
    val comment: String?,
    val hackathonName: String?,
    val reviewedAt: String
)

