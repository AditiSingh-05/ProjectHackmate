package com.example.hackmatefrontendfolder.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.hackmatefrontendfolder.ui.presentation.screens.auth.AuthScreen
import com.example.hackmatefrontendfolder.ui.presentation.screens.auth.EmailVerificationScreen
import com.example.hackmatefrontendfolder.ui.presentation.screens.auth.ForgotPasswordScreen
import com.example.hackmatefrontendfolder.ui.presentation.screens.auth.ResetPasswordScreen
import com.example.hackmatefrontendfolder.ui.presentation.screens.hackathondetails.HackathonDetailsScreen
import com.example.hackmatefrontendfolder.ui.presentation.screens.home.HomeScreen
import com.example.hackmatefrontendfolder.ui.presentation.screens.profile.ProfileSetupScreen
import com.example.hackmatefrontendfolder.ui.presentation.screens.profile.ProfileScreen
import com.example.hackmatefrontendfolder.ui.presentation.screens.teams.TeamsScreen
import com.example.hackmatefrontendfolder.ui.presentation.screens.registered.RegisteredScreen
import com.example.hackmatefrontendfolder.ui.presentation.screens.splash.SplashScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = AppScreens.Splash
    ) {
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
                }
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
                onHackathonClick = { hackathonId ->
                    navController.navigate("${AppScreens.HackathonDetails}/$hackathonId")
                },
                onProfileClick = {
                    navController.navigate(AppScreens.Profile)
                },
                onTeamsClick = {
                    navController.navigate(AppScreens.Teams)
                },
                onRegisteredClick = {
                    navController.navigate(AppScreens.Registered)
                }
            )
        }

        // Hackathon Details Screen
        composable("${AppScreens.HackathonDetails}/{hackathonId}") { backStackEntry ->
            val hackathonId = backStackEntry.arguments?.getString("hackathonId")?.toLongOrNull() ?: 0L
            HackathonDetailsScreen(
                hackathonId = hackathonId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Teams Screen
        composable(AppScreens.Teams) {
            TeamsScreen(
                onNavigateToHome = {
                    navController.navigate(AppScreens.Home) {
                        popUpTo(AppScreens.Home) { inclusive = true }
                    }
                },
                onNavigateToProfile = {
                    navController.navigate(AppScreens.Profile)
                },
                onNavigateToRegistered = {
                    navController.navigate(AppScreens.Registered)
                }
            )
        }

        // Registered Screen
        composable(AppScreens.Registered) {
            RegisteredScreen(
                onNavigateToHome = {
                    navController.navigate(AppScreens.Home) {
                        popUpTo(AppScreens.Home) { inclusive = true }
                    }
                },
                onNavigateToTeams = {
                    navController.navigate(AppScreens.Teams)
                },
                onNavigateToProfile = {
                    navController.navigate(AppScreens.Profile)
                }
            )
        }

        // Profile Screen
        composable(AppScreens.Profile) {
            ProfileScreen(
                onNavigateToHome = {
                    navController.navigate(AppScreens.Home) {
                        popUpTo(AppScreens.Home) { inclusive = true }
                    }
                },
                onNavigateToTeams = {
                    navController.navigate(AppScreens.Teams)
                },
                onNavigateToRegistered = {
                    navController.navigate(AppScreens.Registered)
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
