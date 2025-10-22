package com.example.hackmatefrontendfolder.ui.presentation.home

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.hackmatefrontendfolder.ui.presentation.common.BottomNavigation
import com.example.hackmatefrontendfolder.ui.theme.AppColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onHackathonClick: (Long) -> Unit = {},
    onProfileClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onFilterClick: () -> Unit = {}
) {
    val hackathonsList by viewModel.hackathonsList.collectAsState()
    val hackathonsState by viewModel.hackathonsState.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val registrationToggleState by viewModel.registrationToggleState.collectAsState()
    val starToggleState by viewModel.starToggleState.collectAsState()

    val listState = rememberLazyListState()


    var showRegistrationSnackbar by remember { mutableStateOf(false) }
    var registrationMessage by remember { mutableStateOf("") }
    var showStarSnackbar by remember { mutableStateOf(false) }
    var starMessage by remember { mutableStateOf("") }

    LaunchedEffect(registrationToggleState) {
        when (val state = registrationToggleState) {
            is HomeViewModel.Resource.Success -> {
                state.data?.let { response ->
                    registrationMessage = response.message
                    showRegistrationSnackbar = true
                }
                viewModel.resetRegistrationState()
            }
            is HomeViewModel.Resource.Error -> {
                registrationMessage = state.message ?: "Failed to toggle registration"
                showRegistrationSnackbar = true
                viewModel.resetRegistrationState()
            }
            else -> {}
        }
    }

    // Handle star toggle result
    LaunchedEffect(starToggleState) {
        when (val state = starToggleState) {
            is HomeViewModel.Resource.Success -> {
                state.data?.let { response ->
                    starMessage = response.message
                    showStarSnackbar = true
                }
                viewModel.resetStarState()
            }
            is HomeViewModel.Resource.Error -> {
                starMessage = state.message ?: "Failed to toggle star"
                showStarSnackbar = true
                viewModel.resetStarState()
            }
            else -> {}
        }
    }

    // Detect scroll to bottom for pagination
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex != null && lastVisibleIndex >= hackathonsList.size - 3) {
                    if (hackathonsState is HomeViewModel.Resource.Success) {
                        viewModel.loadNextPage()
                    }
                }
            }
    }

    Scaffold(
        topBar = {
            HomeComponents.TopBar(
                title = "HACKMATE",
                onSearchClick = onSearchClick,
                onFilterClick = onFilterClick
            )
        },
        bottomBar = {
            BottomNavigation(
                selectedItem = 0,
                onItemSelected = { index ->
                    when (index) {
                        3 -> onProfileClick()
                    }
                }
            )
        },
        snackbarHost = {
            if (showRegistrationSnackbar) {
                Snackbar(
                    modifier = Modifier.padding(16.dp),
                    action = {
                        TextButton(onClick = { showRegistrationSnackbar = false }) {
                            Text("OK")
                        }
                    }
                ) {
                    Text(registrationMessage)
                }
            }
            if (showStarSnackbar) {
                Snackbar(
                    modifier = Modifier.padding(16.dp),
                    action = {
                        TextButton(onClick = { showStarSnackbar = false }) {
                            Text("OK")
                        }
                    }
                ) {
                    Text(starMessage)
                }
            }
        },
        containerColor = AppColors.background
    ) { paddingValues ->
        HomeComponents.Background {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (hackathonsState) {
                    is HomeViewModel.Resource.Loading -> {
                        if (hackathonsList.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                HomeComponents.LoadingIndicator()
                            }
                        }
                    }
                    is HomeViewModel.Resource.Error -> {
                        if (hackathonsList.isEmpty()) {
                            HomeComponents.EmptyState(
                                message = (hackathonsState as HomeViewModel.Resource.Error).message
                                    ?: "Failed to load hackathons",
                                icon = Icons.Default.ErrorOutline,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                    is HomeViewModel.Resource.Success -> {
                        if (hackathonsList.isEmpty()) {
                            HomeComponents.EmptyState(
                                message = "No hackathons found.\nCheck back later for exciting opportunities!",
                                icon = Icons.Default.SearchOff,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                    else -> {}
                }

                if (hackathonsList.isNotEmpty()) {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            start = 16.dp,
                            end = 16.dp,
                            top = 16.dp,
                            bottom = 16.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(
                            items = hackathonsList,
                            key = { it.hackathonId }
                        ) { hackathon ->
                            HomeComponents.HackathonCard(
                                hackathon = hackathon,
                                onCardClick = { onHackathonClick(hackathon.hackathonId) },
                                onRegisterToggle = { register ->
                                    viewModel.toggleRegistration(
                                        hackathonId = hackathon.hackathonId,
                                        register = register
                                    )
                                },
                                onStarToggle = { star ->
                                    viewModel.toggleStar(
                                        hackathonId = hackathon.hackathonId,
                                        star = star
                                    )
                                }
                            )
                        }

                        // Loading indicator at bottom for pagination
                        if (hackathonsState is HomeViewModel.Resource.Loading && hackathonsList.isNotEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        color = AppColors.primary,
                                        strokeWidth = 2.dp,
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                            }
                        }
                    }
                }


            }
        }
    }
}

