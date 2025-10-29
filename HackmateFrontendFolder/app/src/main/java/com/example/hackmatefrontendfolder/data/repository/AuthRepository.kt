@file:Suppress("unused")

package com.example.hackmatefrontendfolder.data.repository
import com.example.hackmatefrontendfolder.data.local.UserSessionManager
import com.example.hackmatefrontendfolder.data.remote.ApiService
import com.example.hackmatefrontendfolder.domain.model.user.*;
import retrofit2.Response
import javax.inject.Inject


class AuthRepository @Inject constructor(
    val apiService: ApiService,
    private val userSessionManager: UserSessionManager

) {

    suspend fun signup(
        request : SignupRequest
    ) : Response<SignupResponse> {
        val response = apiService.signup(request)
        if(response.isSuccessful){
            userSessionManager.saveEmail(request.email)
        }
        return response
    }

    suspend fun login(
        request : LoginRequest
    ) : Response<LoginResponse> {
        return apiService.login(request)
    }

    suspend fun changePassword(request: ChangePasswordRequest): Response<ChangePasswordResponse> {
        return apiService.changePassword(request)
    }

    suspend fun forgotPassword(request: ForgotPasswordRequest): Response<ForgotPasswordResponse> {
        return apiService.forgotPassword(request)
    }

    suspend fun resetPassword(request: ResetPasswordRequest): Response<ResetPasswordResponse> {
        return apiService.resetPassword(request)
    }

    suspend fun emailVerification(request: EmailVerificationRequest): Response<EmailVerificationResponse> {
        return apiService.verifyEmail(request)
    }

    suspend fun emailVerificationGet(token: String): Response<EmailVerificationResponse> {
        return apiService.verifyEmailGet(token)
    }

    suspend fun validateResetToken(token: String): Response<TokenValidationResponse> {
        return apiService.validateResetToken(token)
    }

    suspend fun resendVerification(email: String): Response<EmailVerificationResponse> {
        return apiService.resendEmailVerification(email)
    }

    suspend fun getProfileSetupStatus(): Response<ProfileSetupStatusResponse> {
        return apiService.getProfileSetupStatus()
    }

    suspend fun checkEmailExists(email: String): Response<EmailExistenceResponse> {
        return apiService.checkEmailExists(email)
    }

    suspend fun authHealthCheck(): Response<String> {
        return apiService.authHealthCheck()
    }

    suspend fun isEmailVerified(email: String): Boolean {
        val response = apiService.isEmailVerified(email)
        return (if (response.isSuccessful) response.body() ?: false else false) as Boolean
    }
}
