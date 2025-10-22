package com.example.hackmatefrontendfolder.ui.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hackmatefrontendfolder.data.model.profile.*
import com.example.hackmatefrontendfolder.data.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    sealed class Resource<out T> {
        object Idle : Resource<Nothing>()
        object Loading : Resource<Nothing>()
        data class Success<T>(val data: T?) : Resource<T>()
        data class Error(val message: String?) : Resource<Nothing>()
    }

    private val _myProfileState = MutableStateFlow<Resource<PrivateProfileResponse>>(Resource.Idle)
    val myProfileState: StateFlow<Resource<PrivateProfileResponse>> = _myProfileState.asStateFlow()

    fun loadMyProfile() {
        viewModelScope.launch {
            _myProfileState.value = Resource.Loading
            try {
                val response: Response<PrivateProfileResponse> = profileRepository.getMyProfile()
                if (response.isSuccessful) _myProfileState.value = Resource.Success(response.body())
                else _myProfileState.value = Resource.Error(response.message())
            } catch (e: Exception) {
                _myProfileState.value = Resource.Error(e.localizedMessage)
            }
        }
    }

    private val _publicProfileState = MutableStateFlow<Resource<PublicProfileResponse>>(Resource.Idle)
    val publicProfileState: StateFlow<Resource<PublicProfileResponse>> = _publicProfileState.asStateFlow()

    fun loadPublicProfile(userId: Long) {
        viewModelScope.launch {
            _publicProfileState.value = Resource.Loading
            try {
                val response = profileRepository.getPublicProfile(userId)
                if (response.isSuccessful) _publicProfileState.value = Resource.Success(response.body())
                else _publicProfileState.value = Resource.Error(response.message())
            } catch (e: Exception) {
                _publicProfileState.value = Resource.Error(e.localizedMessage)
            }
        }
    }

    private val _addReviewState = MutableStateFlow<Resource<AddReviewResponse>>(Resource.Idle)
    val addReviewState: StateFlow<Resource<AddReviewResponse>> = _addReviewState.asStateFlow()

    fun addReview(request: AddReviewRequest) {
        viewModelScope.launch {
            _addReviewState.value = Resource.Loading
            try {
                val response = profileRepository.addReview(request)
                if (response.isSuccessful) _addReviewState.value = Resource.Success(response.body())
                else _addReviewState.value = Resource.Error(response.message())
            } catch (e: Exception) {
                _addReviewState.value = Resource.Error(e.localizedMessage)
            }
        }
    }
}
