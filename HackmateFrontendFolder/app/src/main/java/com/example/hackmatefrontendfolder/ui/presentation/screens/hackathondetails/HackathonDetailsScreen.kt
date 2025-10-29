package com.example.hackmatefrontendfolder.ui.presentation.screens.hackathondetails

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.hackmatefrontendfolder.ui.presentation.viewmodels.HackathonDetailsViewModel
import com.example.hackmatefrontendfolder.ui.theme.AppColors
import com.example.hackmatefrontendfolder.utils.UIState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HackathonDetailsScreen(
    hackathonId: Long,
    viewModel: HackathonDetailsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val hackathonDetailsState by viewModel.hackathonDetailsState.collectAsState()
    val registrationToggleState by viewModel.registrationToggleState.collectAsState()
    val starToggleState by viewModel.starToggleState.collectAsState()

    var currentHackathon by remember { mutableStateOf<com.example.hackmatefrontendfolder.domain.model.hackathon.HackathonDetailsResponse?>(null) }
    var isRegistered by remember { mutableStateOf(false) }
    var isStarred by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadHackathonDetails(hackathonId)
    }

    LaunchedEffect(hackathonDetailsState) {
        when (hackathonDetailsState) {
            is UIState.Success -> {
                val hackathon = (hackathonDetailsState as UIState.Success).data
                currentHackathon = hackathon
                isRegistered = hackathon.isRegistered
                isStarred = hackathon.isStarred
            }
            else -> {}
        }
    }

    LaunchedEffect(registrationToggleState) {
        when (registrationToggleState) {
            is UIState.Success -> {
                val response = (registrationToggleState as UIState.Success).data
                isRegistered = response.isRegistered
                viewModel.resetRegistrationState()
            }
            else -> {}
        }
    }

    LaunchedEffect(starToggleState) {
        when (starToggleState) {
            is UIState.Success -> {
                val response = (starToggleState as UIState.Success).data
                isStarred = response.isStarred
                viewModel.resetStarState()
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hackathon Details") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppColors.background,
                    titleContentColor = AppColors.onBackground,
                    navigationIconContentColor = AppColors.onBackground
                )
            )
        },
        containerColor = AppColors.background
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (hackathonDetailsState) {
                is UIState.Loading -> {
                    HackathonDetailsComponents.LoadingState()
                }

                is UIState.Error -> {
                    val errorMessage = (hackathonDetailsState as UIState.Error).message
                    HackathonDetailsComponents.ErrorState(
                        message = errorMessage,
                        onRetry = { viewModel.loadHackathonDetails(hackathonId) }
                    )
                }

                is UIState.Success -> {
                    currentHackathon?.let { hackathon ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            HackathonDetailsComponents.HackathonPoster(hackathon.posterUrl)

                            HackathonDetailsComponents.HeaderSection(hackathon)

                            HackathonDetailsComponents.StatsSection(hackathon)

                            HackathonDetailsComponents.ActionButtons(
                                isRegistered = isRegistered,
                                isStarred = isStarred,
                                onRegisterClick = { register ->
                                    viewModel.toggleRegistration(hackathonId, register)
                                },
                                onStarClick = { star ->
                                    viewModel.toggleStar(hackathonId, star)
                                },
                                onOpenLinkClick = {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(hackathon.registrationLink))
                                    context.startActivity(intent)
                                }
                            )

                            HackathonDetailsComponents.DescriptionSection(hackathon.description)

                            HackathonDetailsComponents.DetailsSection(hackathon)

                            HackathonDetailsComponents.TagsSection(hackathon.tags)

                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }

                else -> {}
            }
        }
    }
}
