package com.example.hackmatefrontendfolder.ui.presentation.auth

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.hackmatefrontendfolder.ui.theme.AppColors
import kotlinx.coroutines.launch
import com.example.hackmatefrontendfolder.R

@Composable
fun ResetPasswordScreen(
    onPasswordReset: () -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var isSuccess by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }

    var newPasswordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }

    val focusManager = LocalFocusManager.current

    // Simulate password reset
    suspend fun resetPassword() {
        isLoading = true
        kotlinx.coroutines.delay(2000)

        val isValid = newPassword.length >= 6 && newPassword == confirmPassword
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
                painter = painterResource(id = R.drawable.lockicon),
                contentDescription = "Email Icon",
            )
            Spacer(modifier = Modifier.height(16.dp))


            // Instructions
            Text(
                text = "Create a new password for your account.",
                style = MaterialTheme.typography.bodyMedium,
                color = AppColors.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))

            // New Password Field
            AuthComponents.TextField(
                value = newPassword,
                onValueChange = {
                    newPassword = it
                    newPasswordError = null
                },
                label = "New Password",
                icon = Icons.Default.Lock,
                isPassword = true,
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                isError = newPasswordError != null,
                errorMessage = newPasswordError
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Confirm Password Field
            AuthComponents.TextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    confirmPasswordError = null
                },
                label = "Confirm New Password",
                icon = Icons.Default.Lock,
                isPassword = true,
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                isError = confirmPasswordError != null,
                errorMessage = confirmPasswordError
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Reset Button
            AuthComponents.Button(
                text = "Reset Password",
                onClick = {
                    var hasError = false

                    if (newPassword.length < 6) {
                        newPasswordError = "Password must be at least 6 characters"
                        hasError = true
                    }

                    if (newPassword != confirmPassword) {
                        confirmPasswordError = "Passwords do not match"
                        hasError = true
                    }

                    if (!hasError) {
                        kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main)
                            .launch {
                                resetPassword()
                            }
                    }
                },
                isLoading = isLoading,
                isSuccess = isSuccess,
                isError = isError
            )

            // Success Message
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
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "✓ Password reset successfully!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = AppColors.success,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "You can now login with your new password.",
                            style = MaterialTheme.typography.bodySmall,
                            color = AppColors.success.copy(alpha = 0.8f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Back to Login
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
