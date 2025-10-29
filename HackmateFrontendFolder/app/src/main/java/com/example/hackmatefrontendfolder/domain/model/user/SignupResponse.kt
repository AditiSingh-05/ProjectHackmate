package com.example.hackmatefrontendfolder.domain.model.user

data class SignupResponse(
    val userId: Long,
    val email: String,
    val message : String,
    val role : String,
    val isEmailVerified : Boolean,
    val verificationEmailSent : Boolean
)
