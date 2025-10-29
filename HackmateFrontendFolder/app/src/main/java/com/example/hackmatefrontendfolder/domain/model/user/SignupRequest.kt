package com.example.hackmatefrontendfolder.domain.model.user

data class SignupRequest (
    val email : String,
    val password : String,
    val confirmPassword : String,
)