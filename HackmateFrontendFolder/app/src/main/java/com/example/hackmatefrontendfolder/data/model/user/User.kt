package com.example.hackmatefrontendfolder.data.model.user

data class User(
    val id : Long,
    val email : String,
    val role : String,
    val emailVerified : Boolean,
    val profileSetup : Boolean,
    val createdAt : String,
    val lastLoginAt : String?
)
