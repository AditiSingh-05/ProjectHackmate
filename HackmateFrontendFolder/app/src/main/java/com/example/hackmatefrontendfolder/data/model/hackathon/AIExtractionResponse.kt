package com.example.hackmatefrontendfolder.data.model.hackathon


data class AIExtractionResponse(
    val success: Boolean,
    val extractedTitle: String?,
    val extractedDescription: String?,
    val extractedRegistrationLink: String?,
    val extractedDeadline: String?,
    val extractedTags: List<String>?,
    val extractedOrganizer: String?,
    val extractedLocation: String?,
    val extractedPrizePool: String?,
    val confidenceScore: Double,
    val errorMessage: String?
)

