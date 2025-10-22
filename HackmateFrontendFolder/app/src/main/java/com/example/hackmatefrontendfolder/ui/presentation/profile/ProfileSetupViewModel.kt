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
class ProfileSetupViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    sealed class Resource<out T> {
        object Idle : Resource<Nothing>()
        object Loading : Resource<Nothing>()
        data class Success<T>(val data: T?) : Resource<T>()
        data class Error(val message: String?) : Resource<Nothing>()
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

    private val _setupState = MutableStateFlow<Resource<ProfileSetupResponse>>(Resource.Idle)
    val setupState: StateFlow<Resource<ProfileSetupResponse>> = _setupState.asStateFlow()

    fun setupProfile(request: ProfileSetupRequest) {
        viewModelScope.launch {
            _setupState.value = Resource.Loading
            try {
                val response: Response<ProfileSetupResponse> = profileRepository.setupProfile(request)
                if (response.isSuccessful) _setupState.value = Resource.Success(response.body())
                else _setupState.value = Resource.Error(response.message())
            } catch (e: Exception) {
                _setupState.value = Resource.Error(e.localizedMessage)
            }
        }
    }

    private val _updateState = MutableStateFlow<Resource<ProfileUpdateResponse>>(Resource.Idle)
    val updateState: StateFlow<Resource<ProfileUpdateResponse>> = _updateState.asStateFlow()

    fun updateProfile(request: ProfileUpdateRequest) {
        viewModelScope.launch {
            _updateState.value = Resource.Loading
            try {
                val response = profileRepository.updateProfile(request)
                if (response.isSuccessful) _updateState.value = Resource.Success(response.body())
                else _updateState.value = Resource.Error(response.message())
            } catch (e: Exception) {
                _updateState.value = Resource.Error(e.localizedMessage)
            }
        }
    }

}
