package com.example.hackmatefrontendfolder.ui.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hackmatefrontendfolder.data.repository.HackathonRepository
import com.example.hackmatefrontendfolder.domain.model.hackathon.HackathonDetailsResponse
import com.example.hackmatefrontendfolder.domain.model.hackathon.RegistrationToggleRequest
import com.example.hackmatefrontendfolder.domain.model.hackathon.RegistrationToggleResponse
import com.example.hackmatefrontendfolder.domain.model.hackathon.StarToggleRequest
import com.example.hackmatefrontendfolder.domain.model.hackathon.StarToggleResponse
import com.example.hackmatefrontendfolder.utils.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class HackathonDetailsViewModel @Inject constructor(
    private val hackathonRepository: HackathonRepository
) : ViewModel() {

    private val _hackathonDetailsState = MutableStateFlow<UIState<HackathonDetailsResponse>>(UIState.Empty)
    val hackathonDetailsState: StateFlow<UIState<HackathonDetailsResponse>> = _hackathonDetailsState.asStateFlow()

    private val _registrationToggleState = MutableStateFlow<UIState<RegistrationToggleResponse>>(UIState.Empty)
    val registrationToggleState: StateFlow<UIState<RegistrationToggleResponse>> = _registrationToggleState.asStateFlow()

    private val _starToggleState = MutableStateFlow<UIState<StarToggleResponse>>(UIState.Empty)
    val starToggleState: StateFlow<UIState<StarToggleResponse>> = _starToggleState.asStateFlow()

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

    fun loadHackathonDetails(hackathonId: Long) {
        executeWithUIState(_hackathonDetailsState) {
            hackathonRepository.getHackathonDetails(hackathonId)
        }
    }

    fun toggleRegistration(hackathonId: Long, register: Boolean) {
        executeWithUIState(_registrationToggleState) {
            hackathonRepository.toggleHackathonRegistration(
                RegistrationToggleRequest(hackathonId, register)
            )
        }
    }

    fun toggleStar(hackathonId: Long, star: Boolean) {
        executeWithUIState(_starToggleState) {
            hackathonRepository.toggleHackathonStar(
                StarToggleRequest(hackathonId, star)
            )
        }
    }

    fun resetRegistrationState() {
        _registrationToggleState.value = UIState.Empty
    }

    fun resetStarState() {
        _starToggleState.value = UIState.Empty
    }
}
