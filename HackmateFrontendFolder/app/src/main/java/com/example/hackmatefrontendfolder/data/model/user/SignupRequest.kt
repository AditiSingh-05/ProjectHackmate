package com.example.hackmatefrontendfolder.data.model.user

data class SignupRequest (
    val email : String,
    val password : String,
    val confirmPassword : String,
)