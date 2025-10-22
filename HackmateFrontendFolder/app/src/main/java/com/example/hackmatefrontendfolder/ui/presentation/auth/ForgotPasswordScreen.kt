package com.example.hackmatefrontendfolder.ui.presentation.auth

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.hackmatefrontendfolder.ui.theme.AppColors
import kotlinx.coroutines.launch
import com.example.hackmatefrontendfolder.R

@Composable
fun ForgotPasswordScreen(
    onNavigateBack: () -> Unit = {},
    onNavigateToResetPassword: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var isSuccess by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf<String?>(null) }

    val focusManager = LocalFocusManager.current

    val systemInDarkMode = isSystemInDarkTheme()

    // Simulate sending reset email
    suspend fun sendResetEmail() {
        isLoading = true
        kotlinx.coroutines.delay(2000)

        val isValid = email.contains("@")
        if (isValid) {
            isSuccess = true
            isLoading = false
            kotlinx.coroutines.delay(3000)
            isSuccess = false
        } else {
            isError = true
            isLoading = false
            kotlinx.coroutines.delay(2000)
            isError = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = AppColors.background)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.emailicon),
                contentDescription = "Email Icon",
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "An email has been sent to your address with instructions to reset your password.",
                style = MaterialTheme.typography.bodyMedium,
                color = AppColors.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            AuthComponents.TextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = null
                },
                label = "Email",
                icon = Icons.Default.Email,
                keyboardType = KeyboardType.Email,
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                isError = emailError != null,
                errorMessage = emailError
            )
            Spacer(modifier = Modifier.height(24.dp))
            AuthComponents.Button(
                text = "Send Reset Email",
                onClick = {
                    if (!email.contains("@")) {
                        emailError = "Please enter a valid email"
                    } else {
                        kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main)
                            .launch {
                                sendResetEmail()
                            }
                    }
                },
                isLoading = isLoading,
                isSuccess = isSuccess,
                isError = isError
            )

            AnimatedVisibility(
                visible = isSuccess,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = AppColors.success.copy(alpha = 0.1f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "✓ Reset email sent! Check your inbox.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = AppColors.success,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            TextButton(
                onClick = { /* Navigate back to login */ }
            ) {
                Text(
                    text = "Back to Login",
                    color = AppColors.secondary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }

}
