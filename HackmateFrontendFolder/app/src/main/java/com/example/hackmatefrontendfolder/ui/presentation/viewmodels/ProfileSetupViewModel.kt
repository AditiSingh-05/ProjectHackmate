package com.example.hackmatefrontendfolder.ui.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hackmatefrontendfolder.domain.model.profile.ProfileSetupRequest
import com.example.hackmatefrontendfolder.domain.model.profile.ProfileSetupResponse
import com.example.hackmatefrontendfolder.domain.model.profile.ProfileUpdateRequest
import com.example.hackmatefrontendfolder.domain.model.profile.ProfileUpdateResponse
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
class ProfileSetupViewModel @Inject constructor(
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

    private val _fullName = MutableStateFlow<String?>(null)
    val fullName: StateFlow<String?> = _fullName

    private val _bio = MutableStateFlow<String?>(null)
    val bio: StateFlow<String?> = _bio

    private val _hackathonsParticipated = MutableStateFlow<String?>(null)
    val hackathonsParticipated: StateFlow<String?> = _hackathonsParticipated

    private val _hackathonsWon = MutableStateFlow<String?>(null)
    val hackathonsWon: StateFlow<String?> = _hackathonsWon

    private val _college = MutableStateFlow<String?>(null)
    val college: StateFlow<String?> = _college

    private val _year = MutableStateFlow<String?>(null)
    val year: StateFlow<String?> = _year

    private val _githubProfile = MutableStateFlow<String?>(null)
    val githubProfile: StateFlow<String?> = _githubProfile

    private val _linkedinProfile = MutableStateFlow<String?>(null)
    val linkedinProfile: StateFlow<String?> = _linkedinProfile

    private val _portfolioUrl = MutableStateFlow<String?>(null)
    val portfolioUrl: StateFlow<String?> = _portfolioUrl

    private val _avatarId = MutableStateFlow<String?>(null)
    val avatarId: StateFlow<String?> = _avatarId

    private val _mainSkill = MutableStateFlow<String?>(null)
    val mainSkill: StateFlow<String?> = _mainSkill

    private val _skills = MutableStateFlow<String?>(null)
    val skills: StateFlow<String?> = _skills

    private val _setupState = MutableStateFlow<UIState<ProfileSetupResponse>>(UIState.Empty)
    val setupState: StateFlow<UIState<ProfileSetupResponse>> = _setupState.asStateFlow()

    private val _updateState = MutableStateFlow<UIState<ProfileUpdateResponse>>(UIState.Empty)
    val updateState: StateFlow<UIState<ProfileUpdateResponse>> = _updateState.asStateFlow()

    fun setupProfile(request: ProfileSetupRequest) {
        executeWithUIState(_setupState) {
            profileRepository.setupProfile(request)
        }
    }

    fun updateProfile(request: ProfileUpdateRequest) {
        executeWithUIState(_updateState) {
            profileRepository.updateProfile(request)
        }
    }

}