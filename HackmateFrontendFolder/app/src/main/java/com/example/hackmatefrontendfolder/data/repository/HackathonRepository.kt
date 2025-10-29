@file:Suppress("unused")

package com.example.hackmatefrontendfolder.data.repository
import com.example.hackmatefrontendfolder.data.remote.ApiService
import com.example.hackmatefrontendfolder.domain.model.hackathon.*
import retrofit2.Response
import javax.inject.Inject

class HackathonRepository @Inject constructor(
    private val apiService : ApiService
){
    suspend fun createHackathon(request: CreateHackathonRequest): Response<CreateHackathonResponse> {
        return apiService.createHackathon(request)
    }

    suspend fun getPublicHackathons(request: HackathonFilterRequest): Response<HackathonListResponse> {
        return apiService.getPublicHackathonFeed(
            search = request.search,
            tags = request.tags,
            status = request.status,
            urgencyLevel = request.urgencyLevel,
            organizer = request.organizer,
            location = request.location,
            showExpired = request.showExpired,
            sortBy = request.sortBy,
            sortDirection = request.sortDirection,
            page = request.page,
            size = request.size
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
