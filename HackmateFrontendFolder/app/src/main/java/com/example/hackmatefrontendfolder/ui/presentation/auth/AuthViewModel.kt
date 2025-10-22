@file:Suppress("unused")

package com.example.hackmatefrontendfolder.ui.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hackmatefrontendfolder.data.repository.AuthRepository
import com.example.hackmatefrontendfolder.data.model.user.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword

    private val _currentPassword = MutableStateFlow<String?>(null)
    val currentPassword: StateFlow<String?> = _currentPassword

    private val _newPassword = MutableStateFlow<String?>(null)
    val newPassword: StateFlow<String?> = _newPassword

    private val _confirmNewPassword = MutableStateFlow<String?>(null)
    val confirmNewPassword: StateFlow<String?> = _confirmNewPassword

    private val _resetToken = MutableStateFlow<String?>(null)
    val resetToken: StateFlow<String?> = _resetToken

    private val _verificationToken = MutableStateFlow<String?>(null)
    val verificationToken: StateFlow<String?> = _verificationToken

    private val _resendEmail = MutableStateFlow<String?>(null)
    val resendEmail: StateFlow<String?> = _resendEmail

    sealed class NavigationEvent {
        object Idle : NavigationEvent()
        object NavigateToEmailVerification : NavigationEvent()
        object NavigateToProfileSetup : NavigationEvent()
        object NavigateToHome : NavigationEvent()
    }

    private val _navigationEvent = MutableStateFlow<NavigationEvent>(NavigationEvent.Idle)
    val navigationEvent: StateFlow<NavigationEvent> = _navigationEvent.asStateFlow()

    private val _showVerificationDialog = MutableStateFlow(false)
    val showVerificationDialog: StateFlow<Boolean> = _showVerificationDialog.asStateFlow()

    fun updateEmail(value: String) {
        _email.value = value
    }

    fun updatePassword(value: String) {
        _password.value = value
    }

    fun updateConfirmPassword(value: String) {
        _confirmPassword.value = value
    }

    fun clearAuthFields() {
        _email.value = ""
        _password.value = ""
        _confirmPassword.value = ""
    }

    fun resetNavigationEvent() {
        _navigationEvent.value = NavigationEvent.Idle
    }

    fun dismissVerificationDialog() {
        _showVerificationDialog.value = false
    }

    sealed class Resource<out T> {
        object Idle : Resource<Nothing>()
        object Loading : Resource<Nothing>()
        data class Success<T>(val data: T?) : Resource<T>()
        data class Error(val message: String?) : Resource<Nothing>()
    }

    // Signup
    private val _signupState = MutableStateFlow<Resource<SignupResponse>>(Resource.Idle)
    val signupState: StateFlow<Resource<SignupResponse>> = _signupState.asStateFlow()

    fun signup(email: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            _signupState.value = Resource.Loading
            try {
                val response: Response<SignupResponse> = authRepository.signup(email, password, confirmPassword)
                if (response.isSuccessful) {
                    _signupState.value = Resource.Success(response.body())
                    _showVerificationDialog.value = true
                } else {
                    _signupState.value = Resource.Error(response.message())
                }
            } catch (e: Exception) {
                _signupState.value = Resource.Error(e.localizedMessage)
            }
        }
    }

    fun resetSignupState() {
        _signupState.value = Resource.Idle
    }

    // Login
    private val _loginState = MutableStateFlow<Resource<LoginResponse>>(Resource.Idle)
    val loginState: StateFlow<Resource<LoginResponse>> = _loginState.asStateFlow()

    private val _isProcessingLogin = MutableStateFlow(false)
    val isProcessingLogin: StateFlow<Boolean> = _isProcessingLogin.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = Resource.Loading
            _isProcessingLogin.value = true
            try {
                val response = authRepository.login(email, password)
                if (response.isSuccessful) {
                    _loginState.value = Resource.Success(response.body())
                    response.body()?.let { loginResponse ->
                        handlePostLoginNavigation(loginResponse, email)
                    }
                } else {
                    _loginState.value = Resource.Error(response.message())
                }
            } catch (e: Exception) {
                _loginState.value = Resource.Error(e.localizedMessage)
            } finally {
                _isProcessingLogin.value = false
            }
        }
    }

    fun resetLoginState() {
        _loginState.value = Resource.Idle
    }

    private suspend fun handlePostLoginNavigation(loginResponse: LoginResponse, emailInput: String) {
        val emailToCheck = loginResponse.email ?: emailInput

        val isVerified = try {
            authRepository.isEmailVerified(emailToCheck)
        } catch (e: Exception) {
            false
        }

        if (!isVerified) {
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

    private val _forgotState = MutableStateFlow<Resource<ForgotPasswordResponse>>(Resource.Idle)
    val forgotState: StateFlow<Resource<ForgotPasswordResponse>> = _forgotState.asStateFlow()

    fun forgotPassword(email: String) {
        viewModelScope.launch {
            _forgotState.value = Resource.Loading
            try {
                val response = authRepository.forgotPassword(email)
                if (response.isSuccessful) _forgotState.value = Resource.Success(response.body())
                else _forgotState.value = Resource.Error(response.message())
            } catch (e: Exception) {
                _forgotState.value = Resource.Error(e.localizedMessage)
            }
        }
    }

    private val _resetState = MutableStateFlow<Resource<ResetPasswordResponse>>(Resource.Idle)
    val resetState: StateFlow<Resource<ResetPasswordResponse>> = _resetState.asStateFlow()

    fun resetPassword(token: String, newPassword: String, confirmPassword: String) {
        viewModelScope.launch {
            _resetState.value = Resource.Loading
            try {
                val response = authRepository.resetPassword(token, newPassword, confirmPassword)
                if (response.isSuccessful) _resetState.value = Resource.Success(response.body())
                else _resetState.value = Resource.Error(response.message())
            } catch (e: Exception) {
                _resetState.value = Resource.Error(e.localizedMessage)
            }
        }
    }

    private val _changePasswordState = MutableStateFlow<Resource<ChangePasswordResponse>>(Resource.Idle)
    val changePasswordState: StateFlow<Resource<ChangePasswordResponse>> = _changePasswordState.asStateFlow()

    fun changePassword(currentPassword: String, newPassword: String, confirmNewPassword: String) {
        viewModelScope.launch {
            _changePasswordState.value = Resource.Loading
            try {
                val response = authRepository.changePassword(currentPassword, newPassword, confirmNewPassword)
                if (response.isSuccessful) _changePasswordState.value = Resource.Success(response.body())
                else _changePasswordState.value = Resource.Error(response.message())
            } catch (e: Exception) {
                _changePasswordState.value = Resource.Error(e.localizedMessage)
            }
        }
    }

    private val _verifyEmailState = MutableStateFlow<Resource<EmailVerificationResponse>>(Resource.Idle)
    val verifyEmailState: StateFlow<Resource<EmailVerificationResponse>> = _verifyEmailState.asStateFlow()

    fun verifyEmail(token: String) {
        viewModelScope.launch {
            _verifyEmailState.value = Resource.Loading
            try {
                val response = authRepository.emailVerification(token)
                if (response.isSuccessful) _verifyEmailState.value = Resource.Success(response.body())
                else _verifyEmailState.value = Resource.Error(response.message())
            } catch (e: Exception) {
                _verifyEmailState.value = Resource.Error(e.localizedMessage)
            }
        }
    }

    private val _resendVerificationState = MutableStateFlow<Resource<EmailVerificationResponse>>(Resource.Idle)
    val resendVerificationState: StateFlow<Resource<EmailVerificationResponse>> = _resendVerificationState.asStateFlow()

    fun resendVerification(email: String) {
        viewModelScope.launch {
            _resendVerificationState.value = Resource.Loading
            try {
                val response = authRepository.resendVerification(email)
                if (response.isSuccessful) _resendVerificationState.value = Resource.Success(response.body())
                else _resendVerificationState.value = Resource.Error(response.message())
            } catch (e: Exception) {
                _resendVerificationState.value = Resource.Error(e.localizedMessage)
            }
        }
    }

    private val _emailExistenceState = MutableStateFlow<Resource<EmailExistenceResponse>>(Resource.Idle)
    val emailExistenceState: StateFlow<Resource<EmailExistenceResponse>> = _emailExistenceState.asStateFlow()

    fun checkEmailExists(email: String) {
        viewModelScope.launch {
            _emailExistenceState.value = Resource.Loading
            try {
                val response = authRepository.checkEmailExists(email)
                if (response.isSuccessful) _emailExistenceState.value = Resource.Success(response.body())
                else _emailExistenceState.value = Resource.Error(response.message())
            } catch (e: Exception) {
                _emailExistenceState.value = Resource.Error(e.localizedMessage)
            }
        }
    }

    // Profile setup status
    private val _profileSetupStatus = MutableStateFlow<Resource<ProfileSetupStatusResponse>>(Resource.Idle)
    val profileSetupStatus: StateFlow<Resource<ProfileSetupStatusResponse>> = _profileSetupStatus.asStateFlow()

    fun getProfileSetupStatus() {
        viewModelScope.launch {
            _profileSetupStatus.value = Resource.Loading
            try {
                val response = authRepository.getProfileSetupStatus()
                if (response.isSuccessful) _profileSetupStatus.value = Resource.Success(response.body())
                else _profileSetupStatus.value = Resource.Error(response.message())
            } catch (e: Exception) {
                _profileSetupStatus.value = Resource.Error(e.localizedMessage)
            }
        }
    }

    // Health check
    private val _healthState = MutableStateFlow<Resource<String>>(Resource.Idle)
    val healthState: StateFlow<Resource<String>> = _healthState.asStateFlow()

    fun authHealthCheck() {
        viewModelScope.launch {
            _healthState.value = Resource.Loading
            try {
                val response = authRepository.authHealthCheck()
                if (response.isSuccessful) _healthState.value = Resource.Success(response.body())
                else _healthState.value = Resource.Error(response.message())
            } catch (e: Exception) {
                _healthState.value = Resource.Error(e.localizedMessage)
            }
        }
    }

    // Email verification status check
    private val _emailVerificationStatusState = MutableStateFlow<Resource<EmailVerificationStatusResponse>>(Resource.Idle)
    val emailVerificationStatusState: StateFlow<Resource<EmailVerificationStatusResponse>> = _emailVerificationStatusState.asStateFlow()

    fun checkEmailVerificationStatus(email: String) {
        viewModelScope.launch {
            _emailVerificationStatusState.value = Resource.Loading
            try {
                val response = authRepository.apiService.isEmailVerified(email)
                if (response.isSuccessful) {
                    _emailVerificationStatusState.value = Resource.Success(response.body())
                    // If verified, navigate to profile setup
                    response.body()?.let { statusResponse ->
                        if (statusResponse.verified) {
                            _showVerificationDialog.value = false
                            _navigationEvent.value = NavigationEvent.NavigateToProfileSetup
                        }
                    }
                } else {
                    _emailVerificationStatusState.value = Resource.Error(response.message())
                }
            } catch (e: Exception) {
                _emailVerificationStatusState.value = Resource.Error(e.localizedMessage)
            }
        }
    }

    fun resetEmailVerificationStatusState() {
        _emailVerificationStatusState.value = Resource.Idle
    }

    // Validation
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
}