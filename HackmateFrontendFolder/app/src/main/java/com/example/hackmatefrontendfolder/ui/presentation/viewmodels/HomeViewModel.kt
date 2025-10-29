package com.example.hackmatefrontendfolder.ui.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hackmatefrontendfolder.domain.model.hackathon.*
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
class HomeViewModel @Inject constructor(
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

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _showExpired = MutableStateFlow(false)
    val showExpired: StateFlow<Boolean> = _showExpired.asStateFlow()

    private val _sortBy = MutableStateFlow("deadline")
    val sortBy: StateFlow<String> = _sortBy.asStateFlow()

    private val _sortDirection = MutableStateFlow("asc")
    val sortDirection: StateFlow<String> = _sortDirection.asStateFlow()

    private val _currentPage = MutableStateFlow(0)
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()

    private val _pageSize = MutableStateFlow(20)
    val pageSize: StateFlow<Int> = _pageSize.asStateFlow()

    private val _hackathonsState = MutableStateFlow<UIState<HackathonListResponse>>(UIState.Empty)
    val hackathonsState: StateFlow<UIState<HackathonListResponse>> = _hackathonsState.asStateFlow()

    private val _registrationToggleState = MutableStateFlow<UIState<RegistrationToggleResponse>>(UIState.Empty)
    val registrationToggleState: StateFlow<UIState<RegistrationToggleResponse>> = _registrationToggleState.asStateFlow()

    private val _starToggleState = MutableStateFlow<UIState<StarToggleResponse>>(UIState.Empty)
    val starToggleState: StateFlow<UIState<StarToggleResponse>> = _starToggleState.asStateFlow()

    private val _hackathonsList = MutableStateFlow<List<Hackathon>>(emptyList())
    val hackathonsList: StateFlow<List<Hackathon>> = _hackathonsList.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    init {
        loadHackathons()
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun updateShowExpired(show: Boolean) {
        _showExpired.value = show
    }

    fun updateSortBy(sort: String) {
        _sortBy.value = sort
    }

    fun updateSortDirection(direction: String) {
        _sortDirection.value = direction
    }

    fun loadHackathons(
        search: String = _searchQuery.value,
        showExpired: Boolean = _showExpired.value,
        sortBy: String = _sortBy.value,
        sortDirection: String = _sortDirection.value,
        page: Int = _currentPage.value,
        size: Int = _pageSize.value
    ) {
        viewModelScope.launch {
            _hackathonsState.value = UIState.Loading
            try {
                val request = HackathonFilterRequest(
                    search = search.ifBlank { null },
                    tags = null,
                    status = null,
                    urgencyLevel = null,
                    organizer = null,
                    location = null,
                    showExpired = showExpired.takeIf { it } == true,
                    sortBy = sortBy.ifBlank { null },
                    sortDirection = sortDirection.trim().uppercase(),
                    page = page,
                    size = size
                )
                val response = hackathonRepository.getPublicHackathons(request)
                if (response.isSuccessful) {
                    _hackathonsState.value = UIState.Success(response.body()!!)
                    response.body()?.let { data ->
                        _hackathonsList.value = data.hackathons
                    }
                } else {
                    _hackathonsState.value = UIState.Error(response.message() ?: "Unknown error")
                }
            } catch (e: Exception) {
                _hackathonsState.value = UIState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun searchHackathons(query: String) {
        updateSearchQuery(query)
        _currentPage.value = 0
        loadHackathons(search = query)
    }

    fun refreshHackathons() {
        viewModelScope.launch {
            _isRefreshing.value = true
            _currentPage.value = 0
            loadHackathons()
            _isRefreshing.value = false
        }
    }

    fun loadNextPage() {
        val currentState = _hackathonsState.value
        if (currentState is UIState.Success) {
            currentState.data.let { data ->
                if (data.hasNext) {
                    _currentPage.value += 1
                    loadHackathons()
                }
            }
        }
    }

    fun toggleRegistration(hackathonId: Long, register: Boolean) {
        viewModelScope.launch {
            _registrationToggleState.value = UIState.Loading
            try {
                val request = RegistrationToggleRequest(
                    hackathonId = hackathonId,
                    register = register
                )
                val response = hackathonRepository.toggleHackathonRegistration(request)

                if (response.isSuccessful) {
                    _registrationToggleState.value = UIState.Success(response.body()!!)
                    updateHackathonRegistrationStatus(hackathonId, register)
                } else {
                    _registrationToggleState.value = UIState.Error(response.message() ?: "Unknown error")
                }
            } catch (e: Exception) {
                _registrationToggleState.value = UIState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun toggleStar(hackathonId: Long, star: Boolean) {
        viewModelScope.launch {
            _starToggleState.value = UIState.Loading
            try {
                val request = StarToggleRequest(
                    hackathonId = hackathonId,
                    star = star
                )
                val response = hackathonRepository.toggleHackathonStar(request)

                if (response.isSuccessful) {
                    _starToggleState.value = UIState.Success(response.body()!!)
                    updateHackathonStarStatus(hackathonId, star)
                } else {
                    _starToggleState.value = UIState.Error(response.message() ?: "Unknown error")
                }
            } catch (e: Exception) {
                _starToggleState.value = UIState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    private fun updateHackathonRegistrationStatus(hackathonId: Long, isRegistered: Boolean) {
        val updatedList = _hackathonsList.value.map { hackathon ->
            if (hackathon.hackathonId == hackathonId) {
                hackathon.copy(
                    isRegistered = isRegistered,
                    registrationCount = if (isRegistered)
                        hackathon.registrationCount + 1
                    else
                        (hackathon.registrationCount - 1).coerceAtLeast(0)
                )
            } else {
                hackathon
            }
        }
        _hackathonsList.value = updatedList
    }

    private fun updateHackathonStarStatus(hackathonId: Long, isStarred: Boolean) {
        val updatedList = _hackathonsList.value.map { hackathon ->
            if (hackathon.hackathonId == hackathonId) {
                hackathon.copy(isStarred = isStarred)
            } else {
                hackathon
            }
        }
        _hackathonsList.value = updatedList
    }

    fun applyFilters(
        showExpired: Boolean? = null,
        sortBy: String? = null,
        sortDirection: String? = null
    ) {
        showExpired?.let { _showExpired.value = it }
        sortBy?.let { _sortBy.value = it }
        sortDirection?.let { _sortDirection.value = it }
        _currentPage.value = 0
        loadHackathons()
    }

    fun resetFilters() {
        _searchQuery.value = ""
        _showExpired.value = false
        _sortBy.value = "deadline"
        _sortDirection.value = "asc"
        _currentPage.value = 0
        loadHackathons()
    }

    fun resetRegistrationState() {
        _registrationToggleState.value = UIState.Empty
    }

    fun resetStarState() {
        _starToggleState.value = UIState.Empty
    }
}