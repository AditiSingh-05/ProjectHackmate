package com.example.hackmatefrontendfolder.ui.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hackmatefrontendfolder.domain.model.profile.*
import com.example.hackmatefrontendfolder.data.repository.ProfileRepository
import com.example.hackmatefrontendfolder.utils.UIState
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


    private fun <T> executeWithUIState(
        stateFlow: MutableStateFlow<UIState<T>>,
        block: suspend () -> Response<T>
    ) {
        viewModelScope.launch {
            stateFlow.value = UIState.Loading
            try {
                val response = block()
                if (response.isSuccessful) {
                    stateFlow.value = UIState.Success(response.body()!!)
                } else {
                    stateFlow.value = UIState.Error(response.message() ?: "Unknown error")
                }
            } catch (e: Exception) {
                stateFlow.value = UIState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    private val _myProfileState = MutableStateFlow<UIState<PrivateProfileResponse>>(UIState.Empty)
    val myProfileState: StateFlow<UIState<PrivateProfileResponse>> = _myProfileState.asStateFlow()

    fun loadMyProfile() {
        executeWithUIState(
            _myProfileState
        ) {
            profileRepository.getMyProfile()
        }
    }

    private val _publicProfileState = MutableStateFlow<UIState<PublicProfileResponse>>(UIState.Empty)
    val publicProfileState: StateFlow<UIState<PublicProfileResponse>> = _publicProfileState.asStateFlow()

    fun loadPublicProfile(userId: Long) {
        executeWithUIState(
            _publicProfileState
        ) {
            profileRepository.getPublicProfile(userId)
        }

    }

    private val _addReviewState = MutableStateFlow<UIState<AddReviewResponse>>(UIState.Empty)
    val addReviewState: StateFlow<UIState<AddReviewResponse>> = _addReviewState.asStateFlow()

    fun addReview(request: AddReviewRequest) {
        executeWithUIState(
            _addReviewState
        ) {
            profileRepository.addReview(request)
        }
    }
}