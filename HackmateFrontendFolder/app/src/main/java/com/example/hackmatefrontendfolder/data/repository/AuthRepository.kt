@file:Suppress("unused")

package com.example.hackmatefrontendfolder.data.repository

import com.example.hackmatefrontendfolder.data.api.ApiService
import com.example.hackmatefrontendfolder.data.model.user.ChangePasswordRequest
import com.example.hackmatefrontendfolder.data.model.user.ChangePasswordResponse
import com.example.hackmatefrontendfolder.data.model.user.EmailVerificationRequest
import com.example.hackmatefrontendfolder.data.model.user.EmailVerificationResponse
import com.example.hackmatefrontendfolder.data.model.user.ForgotPasswordRequest
import com.example.hackmatefrontendfolder.data.model.user.ForgotPasswordResponse
import com.example.hackmatefrontendfolder.data.model.user.LoginRequest
import com.example.hackmatefrontendfolder.data.model.user.LoginResponse
import com.example.hackmatefrontendfolder.data.model.user.ResetPasswordRequest
import com.example.hackmatefrontendfolder.data.model.user.ResetPasswordResponse
import com.example.hackmatefrontendfolder.data.model.user.SignupRequest
import com.example.hackmatefrontendfolder.data.model.user.SignupResponse
import com.example.hackmatefrontendfolder.data.model.user.TokenValidationResponse
import com.example.hackmatefrontendfolder.data.model.user.ProfileSetupStatusResponse
import com.example.hackmatefrontendfolder.data.model.user.EmailExistenceResponse
import com.example.hackmatefrontendfolder.data.model.user.EmailVerificationStatusResponse
import retrofit2.Response
import javax.inject.Inject

class AuthRepository @Inject constructor(
    val apiService: ApiService
) {

    suspend fun signup(
        email : String,
        password :String,
        confirmPassword : String,
    ) : Response<SignupResponse> {
        val request = SignupRequest(
            email = email,
            password = password,
            confirmPassword = confirmPassword
        )
        return apiService.signup(request)
    }

    suspend fun login(
        email : String,
        password :String
    ) : Response<LoginResponse> {
        val request = LoginRequest(
            email = email,
            password = password,
        )
        return apiService.login(request)
    }

    suspend fun changePassword(currentPassword: String, newPassword: String, confirmNewPassword: String): Response<ChangePasswordResponse> {
        val request = ChangePasswordRequest(currentPassword = currentPassword, newPassword = newPassword, confirmNewPassword = confirmNewPassword)
        return apiService.changePassword(request)
    }

    suspend fun forgotPassword(email: String): Response<ForgotPasswordResponse> {
        val request = ForgotPasswordRequest(email = email)
        return apiService.forgotPassword(request)
    }

    suspend fun resetPassword(token: String, newPassword: String, confirmPassword: String): Response<ResetPasswordResponse> {
        val request = ResetPasswordRequest(resetToken = token, newPassword = newPassword, confirmPassword = confirmPassword)
        return apiService.resetPassword(request)
    }

    suspend fun emailVerification(token: String): Response<EmailVerificationResponse> {
        val request = EmailVerificationRequest(verificationToken = token)
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
        return response.isSuccessful && response.body()?.verified == true
    }
}
