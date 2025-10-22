package com.example.hackmatefrontendfolder.ui.presentation.auth

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.hackmatefrontendfolder.R
import com.example.hackmatefrontendfolder.ui.theme.AppColors
import kotlinx.coroutines.launch

@Composable
fun EmailVerificationScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onEmailVerified: () -> Unit = {},
    onBackToAuth: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var isResending by remember { mutableStateOf(false) }

    val resendVerificationState by viewModel.resendVerificationState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(resendVerificationState) {
        when (resendVerificationState) {
            is AuthViewModel.Resource.Success -> {
                isResending = false
            }
            is AuthViewModel.Resource.Error -> {
                isResending = false
            }
            else -> {}
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
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Email Icon
            Image(
                painter = painterResource(id = R.drawable.emailicon),
                contentDescription = "Email Verification",
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Title
            Text(
                text = "Verify Your Email",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp
                ),
                color = AppColors.onBackground,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Description
            Text(
                text = "We've sent a verification email to your address. Please check your inbox and click the verification link to continue.",
                style = MaterialTheme.typography.bodyLarge,
                color = AppColors.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Resend verification email section
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = AppColors.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Didn't receive the email?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = AppColors.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Resend Button
                    Button(
                        onClick = {
                            if (email.isNotEmpty()) {
                                isResending = true
                                coroutineScope.launch {
                                    viewModel.resendVerification(email)
                                }
                            }
                        },
                        enabled = !isResending && email.isNotEmpty(),
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppColors.primary
                        )
                    ) {
                        if (isResending) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = AppColors.onPrimary,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Resend Verification Email")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Success message
            AnimatedVisibility(
                visible = resendVerificationState is AuthViewModel.Resource.Success,
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
                        text = "✓ Verification email sent! Check your inbox.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = AppColors.success,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            // Error message
            AnimatedVisibility(
                visible = resendVerificationState is AuthViewModel.Resource.Error,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = AppColors.error.copy(alpha = 0.1f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "✗ Failed to send verification email. Please try again.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = AppColors.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Back to Login Button
            TextButton(
                onClick = onBackToAuth
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    tint = AppColors.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Back to Login",
                    color = AppColors.primary,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
    }
}

