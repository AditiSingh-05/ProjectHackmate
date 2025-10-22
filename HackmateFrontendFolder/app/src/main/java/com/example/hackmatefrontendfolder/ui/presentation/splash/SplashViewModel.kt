package com.example.hackmatefrontendfolder.ui.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hackmatefrontendfolder.data.local.UserSessionManager
import com.example.hackmatefrontendfolder.data.repository.AuthRepository
import com.example.hackmatefrontendfolder.navigation.AppScreens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userSessionManager: UserSessionManager,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _navigationDestination = MutableStateFlow<String?>(null)
    val navigationDestination: StateFlow<String?> = _navigationDestination

    fun checkUserState() {
        viewModelScope.launch {
            delay(1500)

            try {
                val token = userSessionManager.token.first()

                if (token.isNullOrEmpty()) {
                    _navigationDestination.value = AppScreens.Auth
                    return@launch
                }

                val response = authRepository.getProfileSetupStatus()

                if (response.isSuccessful) {
                    val profileStatus = response.body()

                    if (profileStatus != null) {
                        val isProfileComplete = profileStatus.isProfileSetup || profileStatus.completionPercentage >= 100

                        if (isProfileComplete) {
                            _navigationDestination.value = AppScreens.Home
                        } else {
                            _navigationDestination.value = AppScreens.ProfileSetup
                        }
                    } else {
                        _navigationDestination.value = AppScreens.Auth
                    }
                } else {
                    if (response.code() == 403) {
                        _navigationDestination.value = AppScreens.EmailVerification
                    } else {
                        userSessionManager.clearSession()
                        _navigationDestination.value = AppScreens.Auth
                    }
                }
            } catch (e: Exception) {
                userSessionManager.clearSession()
                _navigationDestination.value = AppScreens.Auth
            }
        }
    }
}
