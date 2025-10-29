package com.example.hackmatefrontendfolder.data.remote

import com.google.gson.annotations.SerializedName
import retrofit2.Response

sealed class ApiResponse<T> {
    data class Success<T>(val data: T) : ApiResponse<T>()
    data class Error<T>(val code: Int, val message: String) : ApiResponse<T>()
    data class Exception<T>(val e: Throwable) : ApiResponse<T>()
}


data class ErrorResponse(
    @SerializedName("success") val success: Boolean = false,
    @SerializedName("message") val message: String,
    @SerializedName("fieldErrors") val fieldErrors: Map<String, String>? = null,
    @SerializedName("timestamp") val timestamp: String? = null,
    @SerializedName("status") val status: Int? = null
)

data class SuccessResponse<T>(
    @SerializedName("success") val success: Boolean = true,
    @SerializedName("data") val data: T? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("timestamp") val timestamp: String? = null
)


suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): ApiResponse<T> {
    return try {
        val response = apiCall()
        if (response.isSuccessful) {
            response.body()?.let { body ->
                ApiResponse.Success(body)
            } ?: ApiResponse.Error(response.code(), "Empty response body")
        } else {
            ApiResponse.Error(response.code(), response.message())
        }
    } catch (e: Exception) {
        ApiResponse.Exception(e)
    }
}

data class PageInfo(
    @SerializedName("currentPage") val currentPage: Int,
    @SerializedName("totalPages") val totalPages: Int,
    @SerializedName("totalElements") val totalElements: Long,
    @SerializedName("hasNext") val hasNext: Boolean,
    @SerializedName("hasPrevious") val hasPrevious: Boolean,
    @SerializedName("size") val size: Int
)