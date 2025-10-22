package com.example.hackmatefrontendfolder.ui.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hackmatefrontendfolder.data.model.hackathon.*
import com.example.hackmatefrontendfolder.data.repository.HackathonRepository
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

    sealed class Resource<out T> {
        object Idle : Resource<Nothing>()
        object Loading : Resource<Nothing>()
        data class Success<T>(val data: T?) : Resource<T>()
        data class Error(val message: String?) : Resource<Nothing>()
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

    private val _hackathonsState = MutableStateFlow<Resource<HackathonListResponse>>(Resource.Idle)
    val hackathonsState: StateFlow<Resource<HackathonListResponse>> = _hackathonsState.asStateFlow()

    private val _registrationToggleState = MutableStateFlow<Resource<RegistrationToggleResponse>>(Resource.Idle)
    val registrationToggleState: StateFlow<Resource<RegistrationToggleResponse>> = _registrationToggleState.asStateFlow()

    private val _starToggleState = MutableStateFlow<Resource<StarToggleResponse>>(Resource.Idle)
    val starToggleState: StateFlow<Resource<StarToggleResponse>> = _starToggleState.asStateFlow()

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
            _hackathonsState.value = Resource.Loading
            try {
                val response: Response<HackathonListResponse> = hackathonRepository.getPublicHackathons(
                    search = search,
                    showExpired = showExpired,
                    sortBy = sortBy,
                    sortDirection = sortDirection,
                    page = page,
                    size = size
                )
                if (response.isSuccessful) {
                    _hackathonsState.value = Resource.Success(response.body())
                    response.body()?.let { data ->
                        _hackathonsList.value = data.hackathons
                    }
                } else {
                    _hackathonsState.value = Resource.Error(response.message())
                }
            } catch (e: Exception) {
                _hackathonsState.value = Resource.Error(e.localizedMessage)
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
        if (currentState is Resource.Success) {
            currentState.data?.let { data ->
                if (data.hasNext) {
                    _currentPage.value += 1
                    loadHackathons()
                }
            }
        }
    }

    fun toggleRegistration(hackathonId: Long, register: Boolean) {
        viewModelScope.launch {
            _registrationToggleState.value = Resource.Loading
            try {
                val request = RegistrationToggleRequest(
                    hackathonId = hackathonId,
                    register = register
                )
                val response: Response<RegistrationToggleResponse> =
                    hackathonRepository.toggleHackathonRegistration(request)

                if (response.isSuccessful) {
                    _registrationToggleState.value = Resource.Success(response.body())
                    // Update local hackathon list
                    updateHackathonRegistrationStatus(hackathonId, register)
                } else {
                    _registrationToggleState.value = Resource.Error(response.message())
                }
            } catch (e: Exception) {
                _registrationToggleState.value = Resource.Error(e.localizedMessage)
            }
        }
    }

    fun toggleStar(hackathonId: Long, star: Boolean) {
        viewModelScope.launch {
            _starToggleState.value = Resource.Loading
            try {
                val request = StarToggleRequest(
                    hackathonId = hackathonId,
                    star = star
                )
                val response: Response<StarToggleResponse> =
                    hackathonRepository.toggleHackathonStar(request)

                if (response.isSuccessful) {
                    _starToggleState.value = Resource.Success(response.body())
                    updateHackathonStarStatus(hackathonId, star)
                } else {
                    _starToggleState.value = Resource.Error(response.message())
                }
            } catch (e: Exception) {
                _starToggleState.value = Resource.Error(e.localizedMessage)
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
        _registrationToggleState.value = Resource.Idle
    }

    fun resetStarState() {
        _starToggleState.value = Resource.Idle
    }
}