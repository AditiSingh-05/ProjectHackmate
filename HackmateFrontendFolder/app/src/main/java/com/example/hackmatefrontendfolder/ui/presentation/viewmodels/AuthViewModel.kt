@file:Suppress("unused")

package com.example.hackmatefrontendfolder.ui.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hackmatefrontendfolder.domain.model.user.*
import com.example.hackmatefrontendfolder.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject
import com.example.hackmatefrontendfolder.utils.UIState

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {


    sealed class NavigationEvent {
        object Idle : NavigationEvent()
        object NavigateToEmailVerification : NavigationEvent()
        object NavigateToProfileSetup : NavigationEvent()
        object NavigateToHome : NavigationEvent()
    }


    private val _signupState = MutableStateFlow<UIState<SignupResponse>>(UIState.Empty)
    val signupState: StateFlow<UIState<SignupResponse>> = _signupState.asStateFlow()

    private val _loginState = MutableStateFlow<UIState<LoginResponse>>(UIState.Empty)
    val loginState: StateFlow<UIState<LoginResponse>> = _loginState.asStateFlow()

    private val _changePasswordState =
        MutableStateFlow<UIState<ChangePasswordResponse>>(UIState.Empty)
    val changePasswordState: StateFlow<UIState<ChangePasswordResponse>> =
        _changePasswordState.asStateFlow()

    private val _resetState = MutableStateFlow<UIState<ResetPasswordResponse>>(UIState.Empty)
    val resetState: StateFlow<UIState<ResetPasswordResponse>> = _resetState.asStateFlow()

    private val _navigationEvent = MutableStateFlow<NavigationEvent>(NavigationEvent.Idle)
    val navigationEvent: StateFlow<NavigationEvent> = _navigationEvent.asStateFlow()

    private val _showVerificationDialog = MutableStateFlow(false)
    val showVerificationDialog: StateFlow<Boolean> = _showVerificationDialog.asStateFlow()

    private val _verifyEmailState =
        MutableStateFlow<UIState<EmailVerificationResponse>>(UIState.Empty)
    val verifyEmailState: StateFlow<UIState<EmailVerificationResponse>> =
        _verifyEmailState.asStateFlow()

    private val _forgotPasswordState =
        MutableStateFlow<UIState<ForgotPasswordResponse>>(UIState.Empty)
    val forgotPasswordState: StateFlow<UIState<ForgotPasswordResponse>> =
        _forgotPasswordState.asStateFlow()

    private val _resendVerificationState =
        MutableStateFlow<UIState<EmailVerificationResponse>>(UIState.Empty)
    val resendVerificationState: StateFlow<UIState<EmailVerificationResponse>> =
        _resendVerificationState.asStateFlow()

    private val _emailExistenceState =
        MutableStateFlow<UIState<EmailExistenceResponse>>(UIState.Empty)
    val emailExistenceState: StateFlow<UIState<EmailExistenceResponse>> =
        _emailExistenceState.asStateFlow()

    private val _profileSetupStatus =
        MutableStateFlow<UIState<ProfileSetupStatusResponse>>(UIState.Empty)
    val profileSetupStatus: StateFlow<UIState<ProfileSetupStatusResponse>> =
        _profileSetupStatus.asStateFlow()

    private val _emailVerificationStatusState =
        MutableStateFlow<UIState<EmailVerificationStatusResponse>>(UIState.Empty)
    val emailVerificationStatusState: StateFlow<UIState<EmailVerificationStatusResponse>> =
        _emailVerificationStatusState.asStateFlow()

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


    fun signup(email: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            _signupState.value = UIState.Loading
            try {
                val request = SignupRequest(email, password, confirmPassword)
                val response = authRepository.signup(request)

                if (response.isSuccessful) {
                    _signupState.value = UIState.Success(response.body()!!)
                    _navigationEvent.value = NavigationEvent.NavigateToEmailVerification
                } else {
                    _signupState.value = UIState.Error(response.message() ?: "Signup failed")
                }
            } catch (e: Exception) {
                _signupState.value = UIState.Error(e.localizedMessage ?: "Signup failed")
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = UIState.Loading
            try {
                val request = LoginRequest(email, password)
                val response = authRepository.login(request)

                if (response.isSuccessful) {
                    val loginResponse = response.body()!!
                    _loginState.value = UIState.Success(loginResponse)
                    handlePostLoginNavigation(loginResponse, email)
                } else {
                    _loginState.value = UIState.Error(response.message() ?: "Login failed")
                }
            } catch (e: Exception) {
                _loginState.value = UIState.Error(e.localizedMessage ?: "Login failed")
            }
        }
    }

    fun resetSignupState() {
        _signupState.value = UIState.Empty
    }

    fun resetLoginState() {
        _loginState.value = UIState.Empty
    }

    private suspend fun handlePostLoginNavigation(
        loginResponse: LoginResponse,
        emailInput: String
    ) {
        if (!loginResponse.isEmailVerified) {
            _navigationEvent.value = NavigationEvent.NavigateToEmailVerification
            return
        }

        try {
            val profileStatusResponse = authRepository.getProfileSetupStatus()
            if (profileStatusResponse.isSuccessful) {
                val profileStatus = profileStatusResponse.body()
                val isProfileComplete = profileStatus?.isProfileSetup == true ||
                        (profileStatus?.completionPercentage ?: 0) >= 100

                if (isProfileComplete) {
                    _navigationEvent.value = NavigationEvent.NavigateToHome
                } else {
                    _navigationEvent.value = NavigationEvent.NavigateToProfileSetup
                }
            } else {
                _navigationEvent.value = NavigationEvent.NavigateToProfileSetup
            }
        } catch (e: Exception) {
            _navigationEvent.value = NavigationEvent.NavigateToProfileSetup
        }
    }


    fun resetNavigationEvent() {
        _navigationEvent.value = NavigationEvent.Idle
    }

    fun forgotPassword(email: String) {
        executeWithUIState(
            _forgotPasswordState
        ) {
            val request = ForgotPasswordRequest(email)
            authRepository.forgotPassword(request)
        }
    }


    fun resetPassword(token: String, newPassword: String, confirmPassword: String) {
        executeWithUIState(
            _resetState
        ) {
            val request = ResetPasswordRequest(token, newPassword, confirmPassword)
            authRepository.resetPassword(request)
        }
    }


    fun changePassword(currentPassword: String, newPassword: String, confirmNewPassword: String) {
        executeWithUIState(
            _changePasswordState
        ) {
            val request = ChangePasswordRequest(
                currentPassword = currentPassword,
                newPassword = newPassword,
                confirmNewPassword = confirmNewPassword
            )
            authRepository.changePassword(request)
        }
    }


    fun resendVerification(email: String) {
        executeWithUIState(
            _resendVerificationState
        ) {
            authRepository.resendVerification(email)
        }
    }


    fun checkEmailExists(email: String) {
        executeWithUIState(
            _emailExistenceState
        ) {
            authRepository.checkEmailExists(email)
        }

    }


    fun getProfileSetupStatus() {
        executeWithUIState(
            _profileSetupStatus
        ) {
            authRepository.getProfileSetupStatus()
        }

    }


    fun checkEmailVerificationStatus(email: String) {
        executeWithUIState(
            _emailVerificationStatusState
        ) {
            authRepository.apiService.isEmailVerified(email)
        }
    }

    fun resetEmailVerificationStatusState() {
        _emailVerificationStatusState.value = UIState.Empty
    }

    data class ValidationResult(
        val isValid: Boolean,
        val emailError: String? = null,
        val passwordError: String? = null,
        val confirmPasswordError: String? = null
    )

    fun validateSignupInput(
        email: String,
        password: String,
        confirmPassword: String
    ): ValidationResult {
        var emailError: String? = null
        var passwordError: String? = null
        var confirmPasswordError: String? = null

        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        if (!emailRegex.matches(email)) {
            emailError = "Please enter a valid email address"
        }

        if (password.length < 8) {
            passwordError = "Password must be at least 8 characters"
        } else if (!password.any { it.isUpperCase() } ||
            !password.any { it.isLowerCase() } ||
            !password.any { it.isDigit() }) {
            passwordError = "Password must contain uppercase, lowercase, and number"
        }

        if (password != confirmPassword) {
            confirmPasswordError = "Passwords do not match"
        }

        return ValidationResult(
            isValid = emailError == null && passwordError == null && confirmPasswordError == null,
            emailError = emailError,
            passwordError = passwordError,
            confirmPasswordError = confirmPasswordError
        )
    }


    fun validateLoginInput(
        email: String,
        password: String
    ): ValidationResult {
        var emailError: String? = null
        var passwordError: String? = null

        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        if (!emailRegex.matches(email)) {
            emailError = "Please enter a valid email address"
        }

        if (password.length < 8) {
            passwordError = "Password must be at least 8 characters"
        } else if (!password.any { it.isUpperCase() } ||
            !password.any { it.isLowerCase() } ||
            !password.any { it.isDigit() }) {
            passwordError = "Password must contain uppercase, lowercase, and number"
        }

        return ValidationResult(
            isValid = emailError == null && passwordError == null,
            emailError = emailError,
            passwordError = passwordError
        )
    }

    suspend fun isEmailVerified(email: String?): Boolean {
        return try {
            val response = authRepository.apiService.isEmailVerified(email)
            response.isSuccessful && (response.body()?.verified == true)
        } catch (e: Exception) {
            false
        }
    }
}