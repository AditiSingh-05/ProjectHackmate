package com.example.hackmatefrontendfolder.data.remote

import com.example.hackmatefrontendfolder.domain.model.hackathon.*
import com.example.hackmatefrontendfolder.domain.model.profile.*
import com.example.hackmatefrontendfolder.domain.model.user.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    //Authentication APIs

    @POST("api/auth/signup")
    suspend fun signup(@Body request: SignupRequest): Response<SignupResponse>

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("api/auth/change-password")
    suspend fun changePassword(@Body request: ChangePasswordRequest): Response<ChangePasswordResponse>

    @POST("api/auth/forgot-password")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequest): Response<ForgotPasswordResponse>

    @POST("api/auth/reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<ResetPasswordResponse>

    @GET("api/auth/reset-password")
    suspend fun validateResetToken(@Query("token") token: String): Response<TokenValidationResponse>

    @POST("api/auth/verify-email")
    suspend fun verifyEmail(@Body request: EmailVerificationRequest): Response<EmailVerificationResponse>

    @GET("api/auth/verify-email")
    suspend fun verifyEmailGet(@Query("token") token: String): Response<EmailVerificationResponse>

    @POST("api/auth/resend-verification")
    suspend fun resendEmailVerification(@Query("email") email: String): Response<EmailVerificationResponse>

    @GET("api/auth/profile-setup-status")
    suspend fun getProfileSetupStatus(): Response<ProfileSetupStatusResponse>

    @GET("api/auth/check-email")
    suspend fun checkEmailExists(@Query("email") email: String): Response<EmailExistenceResponse>

    @GET("api/auth/health")
    suspend fun authHealthCheck(): Response<String>

    @GET("api/auth/check-email-verification")
    suspend fun isEmailVerified(@Query("email") email: String?): Response<EmailVerificationStatusResponse>


    //Profile Management APIs

    @POST("api/profiles/setup")
    suspend fun setupProfile(@Body request: ProfileSetupRequest): Response<ProfileSetupResponse>

    @PUT("api/profiles")
    suspend fun updateProfile(@Body request: ProfileUpdateRequest): Response<ProfileUpdateResponse>

    @GET("api/profiles/me")
    suspend fun getMyProfile(): Response<PrivateProfileResponse>

    @GET("api/profiles/{userId}")
    suspend fun getPublicProfile(@Path("userId") userId: Long): Response<PublicProfileResponse>

    @POST("api/profiles/reviews")
    suspend fun addReview(@Body request: AddReviewRequest): Response<AddReviewResponse>

    @GET("api/profiles/health")
    suspend fun profileHealthCheck(): Response<String>


    //hackathon management APIs

    @GET("api/hackathons/feed")
    suspend fun getPublicHackathonFeed(

        @Query("search") search: String? = null,
        @Query("tags") tags: List<String>? = null,
        @Query("status") status: String? = null,
        @Query("urgencyLevel") urgencyLevel: String? = null,
        @Query("organizer") organizer: String? = null,
        @Query("location") location: String? = null,
        @Query("showExpired") showExpired: Boolean = false,
        @Query("sortBy") sortBy: String? = null,
        @Query("sortDirection") sortDirection: String = "asc",
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): Response<HackathonListResponse>

    @GET("api/hackathons/{id}")
    suspend fun getHackathonDetails(@Path("id") hackathonId: Long): Response<HackathonDetailsResponse>

    @POST("api/hackathons")
    suspend fun createHackathon(@Body request: CreateHackathonRequest): Response<CreateHackathonResponse>

    @POST("api/hackathons/register")
    suspend fun toggleHackathonRegistration(@Body request: RegistrationToggleRequest): Response<RegistrationToggleResponse>

    @POST("api/hackathons/star")
    suspend fun toggleHackathonStar(@Body request: StarToggleRequest): Response<StarToggleResponse>

    @GET("api/hackathons/my-registered")
    suspend fun getMyRegisteredHackathons(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): Response<HackathonListResponse>

    @GET("api/hackathons/my-starred")
    suspend fun getMyStarredHackathons(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): Response<HackathonListResponse>

    @POST("api/hackathons/ai-extract")
    suspend fun extractHackathonData(@Body request: AIExtractionRequest): Response<AIExtractionResponse>

    @GET("api/hackathons/health")
    suspend fun hackathonHealthCheck(): Response<String>


}