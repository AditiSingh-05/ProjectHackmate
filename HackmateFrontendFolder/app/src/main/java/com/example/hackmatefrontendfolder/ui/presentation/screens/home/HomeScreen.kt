package com.example.hackmatefrontendfolder.ui.presentation.screens.home

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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.hackmatefrontendfolder.ui.presentation.common.BottomNavigation
import com.example.hackmatefrontendfolder.ui.presentation.viewmodels.HomeViewModel
import com.example.hackmatefrontendfolder.ui.theme.AppColors
import com.example.hackmatefrontendfolder.utils.UIState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onHackathonClick: (Long) -> Unit = {},
    onProfileClick: () -> Unit = {},
    onTeamsClick: () -> Unit = {},
    onRegisteredClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onFilterClick: () -> Unit = {}
) {
    val hackathonsList by viewModel.hackathonsList.collectAsState()
    val hackathonsState by viewModel.hackathonsState.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex != null && lastVisibleIndex >= hackathonsList.size - 3) {
                    if (hackathonsState is UIState.Success) {
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
                        1 -> onTeamsClick()
                        2 -> onRegisteredClick()
                        3 -> onProfileClick()
                    }
                }
            )
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
                    is UIState.Loading -> {
                        if (hackathonsList.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                HomeComponents.LoadingIndicator()
                            }
                        }
                    }
                    is UIState.Error -> {
                        if (hackathonsList.isEmpty()) {
                            HomeComponents.EmptyState(
                                message = (hackathonsState as UIState.Error).message,
                                icon = Icons.Default.ErrorOutline,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                    is UIState.Success -> {
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
                        contentPadding = PaddingValues(16.dp),
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
                                    viewModel.toggleRegistration(hackathon.hackathonId, register)
                                },
                                onStarToggle = { star ->
                                    viewModel.toggleStar(hackathon.hackathonId, star)
                                }
                            )
                        }

                        if (hackathonsState is UIState.Loading && hackathonsList.isNotEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier.fillMaxWidth().padding(16.dp),
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
