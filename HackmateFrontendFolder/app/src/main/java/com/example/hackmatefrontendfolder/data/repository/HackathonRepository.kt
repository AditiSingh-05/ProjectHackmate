@file:Suppress("unused")
package com.example.hackmatefrontendfolder.data.repository

import com.example.hackmatefrontendfolder.data.api.ApiService
import com.example.hackmatefrontendfolder.data.model.hackathon.CreateHackathonRequest
import com.example.hackmatefrontendfolder.data.model.hackathon.CreateHackathonResponse
import com.example.hackmatefrontendfolder.data.model.hackathon.HackathonDetailsResponse
import com.example.hackmatefrontendfolder.data.model.hackathon.HackathonListResponse
import com.example.hackmatefrontendfolder.data.model.hackathon.RegistrationToggleRequest
import com.example.hackmatefrontendfolder.data.model.hackathon.RegistrationToggleResponse
import com.example.hackmatefrontendfolder.data.model.hackathon.StarToggleRequest
import com.example.hackmatefrontendfolder.data.model.hackathon.StarToggleResponse
import retrofit2.Response
import javax.inject.Inject

class HackathonRepository @Inject constructor(
    private val apiService : ApiService
){
    suspend fun createHackathon(request: CreateHackathonRequest): Response<CreateHackathonResponse> {
        return apiService.createHackathon(request)
    }

    suspend fun getPublicHackathons(
        search: String = "",
        showExpired: Boolean = false,
        sortBy: String = "deadline",
        sortDirection: String = "asc",
        page: Int = 0,
        size: Int = 20
    ): Response<HackathonListResponse> {
        return apiService.getPublicHackathonFeed(
            search = search,
            showExpired = showExpired,
            sortBy = sortBy,
            sortDirection = sortDirection,
            page = page,
            size = size
        )
    }

    suspend fun getHackathonDetails(hackathonId: Long): Response<HackathonDetailsResponse> {
        return apiService.getHackathonDetails(hackathonId)
    }

    suspend fun toggleHackathonRegistration(request: RegistrationToggleRequest): Response<RegistrationToggleResponse> {
        return apiService.toggleHackathonRegistration(request)
    }

    suspend fun getMyRegisteredHackathons(
        page: Int = 0,
        size: Int = 20
    ): Response<HackathonListResponse> {
        return apiService.getMyRegisteredHackathons(
            page = page,
            size = size
        )
    }

    suspend fun toggleHackathonStar(request: StarToggleRequest): Response<StarToggleResponse> {
        return apiService.toggleHackathonStar(request)
    }

    suspend fun getMyStarredHackathons(
        page: Int = 0,
        size: Int = 20
    ): Response<HackathonListResponse> {
        return apiService.getMyStarredHackathons(
            page = page,
            size = size
        )
    }




}
