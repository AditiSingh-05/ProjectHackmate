package com.example.hackmatefrontendfolder.ui.presentation.createhackathon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hackmatefrontendfolder.data.api.ApiService
import com.example.hackmatefrontendfolder.data.model.hackathon.CreateHackathonRequest
import com.example.hackmatefrontendfolder.data.model.hackathon.CreateHackathonResponse
import com.example.hackmatefrontendfolder.data.repository.HackathonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class CreateHackathonViewModel @Inject constructor(
    private val hackathonRepository: HackathonRepository
) : ViewModel(){

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

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    private val _createHackathonResponse = MutableStateFlow<Response<CreateHackathonResponse>?>(null)
    val createHackathonResponse: StateFlow<Response<CreateHackathonResponse>?> = _createHackathonResponse

    private val _hackathonListResponse = MutableStateFlow<Response<Any>?>(null)
    val hackathonListResponse: StateFlow<Response<Any>?> = _hackathonListResponse

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
        _isLoading.value = true
        _errorMessage.value = ""

        val titleValue = title.trim()
        val descriptionValue = description.trim()
        val registrationLinkValue = registrationLink.trim()
        val deadlineValue = deadline.trim()

        if (titleValue.isEmpty()) {
            _errorMessage.value = "Title is required"
            _isLoading.value = false
            return
        }
        if (descriptionValue.isEmpty()) {
            _errorMessage.value = "Description is required"
            _isLoading.value = false
            return
        }
        if (registrationLinkValue.isEmpty()) {
            _errorMessage.value = "Registration link is required"
            _isLoading.value = false
            return
        }
        if (deadlineValue.isEmpty()) {
            _errorMessage.value = "Deadline is required"
            _isLoading.value = false
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

        viewModelScope.launch {
            try {
                val response = hackathonRepository.createHackathon(request)
                _createHackathonResponse.value = response
                if (!response.isSuccessful) {
                    _errorMessage.value = "Error: ${response.code()} ${response.message()}"
                } else if (response.body()?.success == false) {
                    _errorMessage.value = response.body()?.message ?: "Unknown error"
                }
            } catch (ex: Exception) {
                _errorMessage.value = ex.message ?: "Unknown exception occurred"
                _createHackathonResponse.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getPublicHackathons() {
        // Implementation for fetching public hackathons will go here
    }

    fun getHackathonDetails() {
        // Implementation for fetching hackathon details will go here
    }

    fun toggleHackathonRegistration() {
        // Implementation for toggling hackathon registration will go here
    }

    fun getMyRegisteredHackathons() {
        // Implementation for fetching user's registered hackathons will go here
    }
    fun getMyStarredHackathons() {
        // Implementation for fetching user's starred hackathons will go here
    }

}

