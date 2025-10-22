
package com.example.hackmatefrontendfolder.data.model.profile

data class AddReviewRequest(
    val userIdToReview: Long,
    val rating: Int,
    val comment: String? = null,
    val hackathonId: Long? = null
)
