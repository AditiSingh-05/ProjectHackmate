package com.example.hackmatefrontendfolder.ui.presentation.screens.profile

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.hackmatefrontendfolder.domain.model.profile.PrivateProfileResponse
import com.example.hackmatefrontendfolder.ui.presentation.common.BottomNavigation
import com.example.hackmatefrontendfolder.ui.presentation.viewmodels.ProfileViewModel
import com.example.hackmatefrontendfolder.ui.theme.AppColors
import com.example.hackmatefrontendfolder.utils.UIState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToTeams: () -> Unit = {},
    onNavigateToRegistered: () -> Unit = {},
    onEditProfile: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val myProfileState by viewModel.myProfileState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadMyProfile()
    }

    Scaffold(
        topBar = {
            ProfileComponents.ProfileTopBar(
                onNotificationClick = {}
            )
        },
        bottomBar = {
            BottomNavigation(
                selectedItem = 3,
                onItemSelected = { index ->
                    when (index) {
                        0 -> onNavigateToHome()
                        1 -> onNavigateToTeams()
                        2 -> onNavigateToRegistered()
                    }
                }
            )
        },
        containerColor = AppColors.background
    ) { paddingValues ->
        ProfileComponents.ProfileBackground {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (myProfileState) {
                    is UIState.Loading -> {
                        ProfileComponents.LoadingState()
                    }
                    is UIState.Success -> {
                        val profile = (myProfileState as UIState.Success).data
                        ProfileContent(
                            profile = profile,
                            onEditProfile = onEditProfile,
                            onLogout = onLogout
                        )
                    }
                    is UIState.Error -> {
                        val errorMessage = (myProfileState as UIState.Error).message
                        ProfileComponents.ErrorState(
                            message = errorMessage,
                            onRetry = { viewModel.loadMyProfile() }
                        )
                    }
                    else -> {}
                }
            }
        }
    }
}

@Composable
private fun ProfileContent(
    profile: PrivateProfileResponse,
    onEditProfile: () -> Unit,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileComponents.ProfileHeader(profile = profile, onEditProfile = onEditProfile)

        Spacer(modifier = Modifier.height(24.dp))

        ProfileComponents.StatsSection(profile = profile)

        Spacer(modifier = Modifier.height(24.dp))

        ProfileComponents.AboutSection(profile = profile)

        Spacer(modifier = Modifier.height(24.dp))

        ProfileComponents.SkillsSection(skills = profile.skills, mainSkill = profile.mainSkill)

        Spacer(modifier = Modifier.height(24.dp))

        ProfileComponents.SocialLinksSection(profile = profile)

        Spacer(modifier = Modifier.height(24.dp))

        ProfileComponents.ReviewsSection(
            reviews = profile.recentReviews,
            averageRating = profile.averageRating,
            totalReviews = profile.totalReviews
        )

        Spacer(modifier = Modifier.height(24.dp))

        ProfileComponents.LogoutButton(onLogout = onLogout)

        Spacer(modifier = Modifier.height(32.dp))
    }
}
