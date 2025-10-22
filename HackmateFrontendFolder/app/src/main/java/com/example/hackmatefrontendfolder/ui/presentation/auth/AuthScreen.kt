package com.example.hackmatefrontendfolder.ui.presentation.auth

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.hackmatefrontendfolder.data.local.UserSessionManager
import com.example.hackmatefrontendfolder.ui.theme.AppColors
import com.example.hackmatefrontendfolder.ui.theme.PrimaryCyan
import kotlinx.coroutines.launch

@Composable
fun AuthScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onNavigateToEmailVerification: () -> Unit = {},
    onNavigateToProfileSetup: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToForgotPassword: () -> Unit = {}
) {
    val context = LocalContext.current
    val userSessionManager = remember { UserSessionManager(context) }

    var isSignUp by remember { mutableStateOf(true) }

    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val confirmPassword by viewModel.confirmPassword.collectAsState()

    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()

    val signupState by viewModel.signupState.collectAsState()
    val loginState by viewModel.loginState.collectAsState()
    val showVerificationDialog by viewModel.showVerificationDialog.collectAsState()
    val navigationEvent by viewModel.navigationEvent.collectAsState()
    val emailVerificationStatusState by viewModel.emailVerificationStatusState.collectAsState()

    // Handle signup success - save email
    LaunchedEffect(signupState) {
        when (val state = signupState) {
            is AuthViewModel.Resource.Success -> {
                state.data?.let { response ->
                    userSessionManager.saveEmail(response.email)
                }
            }
            is AuthViewModel.Resource.Error -> {
                errorMessage = state.message ?: "Signup failed. Please try again."
            }
            else -> {}
        }
    }

    // Handle login success - save token and email
    LaunchedEffect(loginState) {
        when (val state = loginState) {
            is AuthViewModel.Resource.Success -> {
                state.data?.let { response ->
                    response.token?.let { userSessionManager.saveToken(it) }
                    response.email?.let { userSessionManager.saveEmail(it) }
                }
            }
            is AuthViewModel.Resource.Error -> {
                errorMessage = state.message ?: "Login failed. Please try again."
            }
            else -> {}
        }
    }

    // Handle navigation events from ViewModel
    LaunchedEffect(navigationEvent) {
        when (navigationEvent) {
            is AuthViewModel.NavigationEvent.NavigateToEmailVerification -> {
                userSessionManager.saveEmailVerified(false)
                onNavigateToEmailVerification()
                viewModel.resetNavigationEvent()
            }
            is AuthViewModel.NavigationEvent.NavigateToProfileSetup -> {
                userSessionManager.saveEmailVerified(true)
                userSessionManager.saveProfileSetup(false)
                onNavigateToProfileSetup()
                viewModel.resetNavigationEvent()
            }
            is AuthViewModel.NavigationEvent.NavigateToHome -> {
                userSessionManager.saveEmailVerified(true)
                userSessionManager.saveProfileSetup(true)
                onNavigateToHome()
                viewModel.resetNavigationEvent()
            }
            is AuthViewModel.NavigationEvent.Idle -> {}
        }
    }

    // Handle email verification status check result
    LaunchedEffect(emailVerificationStatusState) {
        when (val state = emailVerificationStatusState) {
            is AuthViewModel.Resource.Error -> {
                errorMessage = state.message ?: "Failed to check verification status"
            }
            else -> {}
        }
    }

    val isLoading = signupState is AuthViewModel.Resource.Loading ||
            loginState is AuthViewModel.Resource.Loading
    val isSuccess = signupState is AuthViewModel.Resource.Success ||
            loginState is AuthViewModel.Resource.Success
    val isError = signupState is AuthViewModel.Resource.Error ||
            loginState is AuthViewModel.Resource.Error
    val isCheckingVerification = emailVerificationStatusState is AuthViewModel.Resource.Loading

    // Verification dialog
    if (showVerificationDialog) {
        AlertDialog(
            modifier = Modifier.fillMaxWidth(),
            icon = {
                Icon(
                    imageVector = Icons.Default.Verified,
                    contentDescription = "Verification Email Sent",
                    tint = PrimaryCyan,
                    modifier = Modifier.size(60.dp)
                )
            },
            onDismissRequest = { viewModel.dismissVerificationDialog() },
            title = { Text("Email Sent") },
            text = {
                Text(
                    text = "A verification email has been sent to $email. Please check your inbox.",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = { viewModel.checkEmailVerificationStatus(email) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        shape = RoundedCornerShape(8.dp),
                        enabled = !isCheckingVerification
                    ) {
                        if (isCheckingVerification) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text("Continue to Verification")
                        }
                    }
                }
            }
        )
    }

    // Auto-dismiss error message
    if (errorMessage != null) {
        LaunchedEffect(errorMessage) {
            kotlinx.coroutines.delay(3000)
            errorMessage = null
        }
    }

    AuthComponents.Background {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val screenHeight = maxHeight
            val cardHeight = screenHeight * 0.78f
            val titleHeight = screenHeight * 0.22f

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(titleHeight),
                contentAlignment = Alignment.Center
            ) {}

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(cardHeight)
                    .align(Alignment.BottomCenter)
            ) {
                AuthComponents.AuthCard {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(28.dp)
                    ) {
                        Spacer(modifier = Modifier.height(5.dp))

                        AuthComponents.AuthModeToggle(
                            isSignUp = isSignUp,
                            onToggle = { newMode ->
                                isSignUp = newMode
                                if (!newMode) {
                                    viewModel.updateConfirmPassword("")
                                    confirmPasswordError = null
                                }
                                emailError = null
                                passwordError = null
                                errorMessage = null
                            }
                        )

                        AnimatedVisibility(
                            visible = errorMessage != null,
                            enter = fadeIn() + slideInVertically(),
                            exit = fadeOut() + slideOutVertically()
                        ) {
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = AppColors.error.copy(alpha = 0.1f)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                            ) {
                                Text(
                                    text = errorMessage ?: "",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = AppColors.error,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                        }

                        AuthComponents.TextField(
                            value = email,
                            onValueChange = {
                                viewModel.updateEmail(it)
                                emailError = null
                                errorMessage = null
                            },
                            label = "EMAIL ADDRESS",
                            icon = Icons.Default.Email,
                            keyboardType = KeyboardType.Email,
                            keyboardActions = KeyboardActions(
                                onNext = { focusManager.moveFocus(FocusDirection.Down) }
                            ),
                            isError = emailError != null,
                            errorMessage = emailError
                        )

                        AuthComponents.TextField(
                            value = password,
                            onValueChange = {
                                viewModel.updatePassword(it)
                                passwordError = null
                                errorMessage = null
                            },
                            label = "PASSWORD",
                            icon = Icons.Default.Lock,
                            isPassword = true,
                            keyboardActions = KeyboardActions(
                                onNext = {
                                    if (isSignUp) {
                                        focusManager.moveFocus(FocusDirection.Down)
                                    } else {
                                        focusManager.clearFocus()
                                    }
                                }
                            ),
                            isError = passwordError != null,
                            errorMessage = passwordError
                        )

                        AnimatedVisibility(
                            visible = isSignUp,
                            enter = slideInVertically(
                                animationSpec = tween(500, easing = EaseOutCubic)
                            ) + expandVertically(
                                animationSpec = tween(500, easing = EaseOutCubic)
                            ) + fadeIn(
                                animationSpec = tween(400, delayMillis = 100)
                            ),
                            exit = slideOutVertically(
                                animationSpec = tween(400, easing = EaseInCubic)
                            ) + shrinkVertically(
                                animationSpec = tween(400, easing = EaseInCubic)
                            ) + fadeOut(
                                animationSpec = tween(300)
                            )
                        ) {
                            AuthComponents.TextField(
                                value = confirmPassword,
                                onValueChange = {
                                    viewModel.updateConfirmPassword(it)
                                    confirmPasswordError = null
                                    errorMessage = null
                                },
                                label = "CONFIRM PASSWORD",
                                icon = Icons.Default.Lock,
                                isPassword = true,
                                keyboardActions = KeyboardActions(
                                    onDone = { focusManager.clearFocus() }
                                ),
                                isError = confirmPasswordError != null,
                                errorMessage = confirmPasswordError
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        AuthComponents.Button(
                            text = if (isSignUp) "CREATE ACCOUNT" else "SIGN IN",
                            onClick = {
                                if (isSignUp) {
                                    val validationResult = viewModel.validateSignupInput(
                                        email, password, confirmPassword
                                    )
                                    emailError = validationResult.emailError
                                    passwordError = validationResult.passwordError
                                    confirmPasswordError = validationResult.confirmPasswordError

                                    if (validationResult.isValid) {
                                        coroutineScope.launch {
                                            viewModel.signup(email, password, confirmPassword)
                                        }
                                    }
                                } else {
                                    val validationResult = viewModel.validateLoginInput(
                                        email, password
                                    )
                                    emailError = validationResult.emailError
                                    passwordError = validationResult.passwordError

                                    if (validationResult.isValid) {
                                        coroutineScope.launch {
                                            viewModel.login(email, password)
                                        }
                                    }
                                }
                            },
                            isLoading = isLoading || isCheckingVerification,
                            isSuccess = isSuccess,
                            isError = isError
                        )

                        AnimatedVisibility(
                            visible = !isSignUp,
                            enter = fadeIn(animationSpec = tween(500)) +
                                    slideInVertically(animationSpec = tween(500)),
                            exit = fadeOut(animationSpec = tween(300)) +
                                    slideOutVertically(animationSpec = tween(300))
                        ) {
                            TextButton(
                                onClick = { onNavigateToForgotPassword() },
                                modifier = Modifier.padding(top = 8.dp)
                            ) {
                                Text(
                                    text = "Forgot Password?",
                                    color = AppColors.Accent,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Medium,
                                        letterSpacing = 0.5.sp
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(40.dp))
                        }

                        AnimatedVisibility(
                            visible = isSignUp,
                            enter = fadeIn(animationSpec = tween(500, delayMillis = 200)),
                            exit = fadeOut(animationSpec = tween(300))
                        ) {
                            Text(
                                text = "By creating an account, you agree to our Terms of Service and Privacy Policy",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    letterSpacing = 0.3.sp
                                ),
                                color = AppColors.onSurfaceVariant.copy(alpha = 0.7f),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}