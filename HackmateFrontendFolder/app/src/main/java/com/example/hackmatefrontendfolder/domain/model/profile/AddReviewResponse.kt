package com.example.hackmatefrontendfolder.domain.model.profile

data class AddReviewResponse(
    val success: Boolean,
    val message: String,
    val reviewId: Long?,
    val reviewedAt: String?
)

