package com.example.hackmatefrontendfolder.ui.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hackmatefrontendfolder.domain.model.hackathon.CreateHackathonRequest
import com.example.hackmatefrontendfolder.domain.model.hackathon.CreateHackathonResponse
import com.example.hackmatefrontendfolder.data.repository.HackathonRepository
import com.example.hackmatefrontendfolder.utils.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class CreateHackathonViewModel @Inject constructor(
    private val hackathonRepository: HackathonRepository
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

    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title

    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description

    private val _registrationLink = MutableStateFlow("")
    val registrationLink: StateFlow<String> = _registrationLink

    private val _deadline = MutableStateFlow("")
    val deadline: StateFlow<String> = _deadline

    private val _posterUrl = MutableStateFlow("")
    val posterUrl: StateFlow<String> = _posterUrl

    private val _tags = MutableStateFlow<List<String>>(emptyList())
    val tags: StateFlow<List<String>> = _tags

    private val _organizer = MutableStateFlow("")
    val organizer: StateFlow<String> = _organizer

    private val _location = MutableStateFlow("")
    val location: StateFlow<String> = _location

    private val _prizePool = MutableStateFlow("")
    val prizePool: StateFlow<String> = _prizePool

    private val _eventStartDate = MutableStateFlow("")
    val eventStartDate: StateFlow<String> = _eventStartDate

    private val _eventEndDate = MutableStateFlow("")
    val eventEndDate: StateFlow<String> = _eventEndDate

    private val _maxTeamSize = MutableStateFlow("")
    val maxTeamSize: StateFlow<String> = _maxTeamSize

    private val _minTeamSize = MutableStateFlow("")
    val minTeamSize: StateFlow<String> = _minTeamSize

    private val _originalMessage = MutableStateFlow("")
    val originalMessage: StateFlow<String> = _originalMessage

    private val _contactEmail = MutableStateFlow("")
    val contactEmail: StateFlow<String> = _contactEmail

    private val _createHackathonState = MutableStateFlow<UIState<CreateHackathonResponse>>(UIState.Empty)
    val createHackathonState: StateFlow<UIState<CreateHackathonResponse>> = _createHackathonState.asStateFlow()

    fun createHackathon(
        title: String,
        description: String,
        registrationLink: String,
        deadline: String,
        posterUrl: String = "",
        tags: List<String> = emptyList(),
        organizer: String = "",
        location: String = "",
        prizePool: String = "",
        eventStartDate: String = "",
        eventEndDate: String = "",
        maxTeamSize: String = "",
        minTeamSize: String = "",
        originalMessage: String = "",
        contactEmail: String = ""
    ) {
        val titleValue = title.trim()
        val descriptionValue = description.trim()
        val registrationLinkValue = registrationLink.trim()
        val deadlineValue = deadline.trim()

        if (titleValue.isEmpty()) {
            _createHackathonState.value = UIState.Error("Title is required")
            return
        }
        if (descriptionValue.isEmpty()) {
            _createHackathonState.value = UIState.Error("Description is required")
            return
        }
        if (registrationLinkValue.isEmpty()) {
            _createHackathonState.value = UIState.Error("Registration link is required")
            return
        }
        if (deadlineValue.isEmpty()) {
            _createHackathonState.value = UIState.Error("Deadline is required")
            return
        }

        val request = CreateHackathonRequest(
            title = titleValue,
            description = descriptionValue,
            registrationLink = registrationLinkValue,
            deadline = deadlineValue,
            posterUrl = if (posterUrl.isBlank()) null else posterUrl,
            tags = tags,
            organizer = if (organizer.isBlank()) null else organizer,
            location = if (location.isBlank()) null else location,
            prizePool = if (prizePool.isBlank()) null else prizePool,
            eventStartDate = if (eventStartDate.isBlank()) null else eventStartDate,
            eventEndDate = if (eventEndDate.isBlank()) null else eventEndDate,
            maxTeamSize = maxTeamSize.toIntOrNull(),
            minTeamSize = minTeamSize.toIntOrNull(),
            originalMessage = if (originalMessage.isBlank()) null else originalMessage,
            contactEmail = if (contactEmail.isBlank()) null else contactEmail
        )

        executeWithUIState(_createHackathonState) {
            hackathonRepository.createHackathon(request)
        }
    }

    fun resetCreateHackathonState() {
        _createHackathonState.value = UIState.Empty
    }

}