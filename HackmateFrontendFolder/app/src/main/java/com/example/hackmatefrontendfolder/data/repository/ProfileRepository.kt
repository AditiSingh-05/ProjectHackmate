@file:Suppress("unused")
package com.example.hackmatefrontendfolder.data.repository

import com.example.hackmatefrontendfolder.data.api.ApiService
import com.example.hackmatefrontendfolder.data.model.profile.ProfileSetupRequest
import com.example.hackmatefrontendfolder.data.model.profile.ProfileSetupResponse
import com.example.hackmatefrontendfolder.data.model.profile.ProfileUpdateRequest
import com.example.hackmatefrontendfolder.data.model.profile.ProfileUpdateResponse
import com.example.hackmatefrontendfolder.data.model.profile.PrivateProfileResponse
import com.example.hackmatefrontendfolder.data.model.profile.PublicProfileResponse
import com.example.hackmatefrontendfolder.data.model.profile.AddReviewRequest
import com.example.hackmatefrontendfolder.data.model.profile.AddReviewResponse
import retrofit2.Response
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun setupProfile(request: ProfileSetupRequest): Response<ProfileSetupResponse> {
        return apiService.setupProfile(request)
    }

    suspend fun updateProfile(request: ProfileUpdateRequest): Response<ProfileUpdateResponse> {
        return apiService.updateProfile(request)
    }

    suspend fun getMyProfile(): Response<PrivateProfileResponse> {
        return apiService.getMyProfile()
    }

    suspend fun getPublicProfile(userId: Long): Response<PublicProfileResponse> {
        return apiService.getPublicProfile(userId)
    }

    suspend fun addReview(request: AddReviewRequest): Response<AddReviewResponse> {
        return apiService.addReview(request)
    }

    suspend fun profileHealthCheck(): Response<String> {
        return apiService.profileHealthCheck()
    }
}

