package com.example.hackmatefrontendfolder.data.remote

import android.util.Log
import com.example.hackmatefrontendfolder.data.local.UserSessionManager
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val userSessionManager: UserSessionManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking {
            userSessionManager.token.firstOrNull()
        }
        val originalRequest = chain.request()
        Log.d("AuthInterceptor", "Request URL: ${originalRequest.url}")
        Log.d("AuthInterceptor", "Token: $token")

        return if (token != null && token.isNotBlank()) {
            val newRequest = originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            chain.proceed(newRequest)
        } else {
            chain.proceed(originalRequest)
        }
    }

}