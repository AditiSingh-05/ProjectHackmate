package com.example.hackmatefrontendfolder.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.hackmatefrontendfolder.ui.presentation.auth.AuthScreen
import com.example.hackmatefrontendfolder.ui.presentation.auth.EmailVerificationScreen
import com.example.hackmatefrontendfolder.ui.presentation.auth.ForgotPasswordScreen
import com.example.hackmatefrontendfolder.ui.presentation.auth.ResetPasswordScreen
import com.example.hackmatefrontendfolder.ui.presentation.home.HomeScreen
import com.example.hackmatefrontendfolder.ui.presentation.profile.ProfileSetupScreen
import com.example.hackmatefrontendfolder.ui.presentation.profile.ProfileScreen
import com.example.hackmatefrontendfolder.ui.presentation.splash.SplashScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = AppScreens.Splash
    ) {
        // Splash Screen - Entry point for all users
        composable(AppScreens.Splash) {
            SplashScreen(
                onNavigate = { destination ->
                    navController.navigate(destination) {
                        popUpTo(AppScreens.Splash) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        // Auth Screen - Login/Signup
        composable(AppScreens.Auth) {
            AuthScreen(
                onNavigateToEmailVerification = {
                    navController.navigate(AppScreens.EmailVerification) {
                        popUpTo(AppScreens.Auth) {
                            inclusive = true
                        }
                    }
                },
                onNavigateToProfileSetup = {
                    navController.navigate(AppScreens.ProfileSetup) {
                        popUpTo(AppScreens.Auth) {
                            inclusive = true
                        }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(AppScreens.Home) {
                        popUpTo(AppScreens.Auth) {
                            inclusive = true
                        }
                    }
                },
                onNavigateToForgotPassword = {
                    navController.navigate(AppScreens.ForgotPassword)
                },

            )
        }

        // Email Verification Screen
        composable(AppScreens.EmailVerification) {
            EmailVerificationScreen(
                onEmailVerified = {
                    navController.navigate(AppScreens.ProfileSetup) {
                        popUpTo(AppScreens.EmailVerification) {
                            inclusive = true
                        }
                    }
                },
                onBackToAuth = {
                    navController.navigate(AppScreens.Auth) {
                        popUpTo(AppScreens.EmailVerification) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // Profile Setup Screen
        composable(AppScreens.ProfileSetup) {
            ProfileSetupScreen(
                onProfileComplete = {
                    navController.navigate(AppScreens.Home) {
                        popUpTo(AppScreens.ProfileSetup) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // Home Screen
        composable(AppScreens.Home) {
            HomeScreen(
                onProfileClick = {
                    navController.navigate(AppScreens.Profile)
                }
            )
        }

        // Profile Screen
        composable(AppScreens.Profile) {
            ProfileScreen(
                onNavigateToHome = {
                    navController.navigate(AppScreens.Home) {
                        popUpTo(AppScreens.Profile) {
                            inclusive = true
                        }
                    }
                },
                onNavigateToTeams = {
                },
                onNavigateToRegistered = {
                },
                onEditProfile = {
                },
                onLogout = {
                    navController.navigate(AppScreens.Auth) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // Forgot Password Screen
        composable(AppScreens.ForgotPassword) {
            ForgotPasswordScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToResetPassword = {
                    navController.navigate(AppScreens.ResetPassword)
                }
            )
        }

        // Reset Password Screen
        composable(AppScreens.ResetPassword) {
            ResetPasswordScreen(
                onPasswordReset = {
                    navController.navigate(AppScreens.Auth) {
                        popUpTo(AppScreens.ForgotPassword) {
                            inclusive = true
                        }
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
