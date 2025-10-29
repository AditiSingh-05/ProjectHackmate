package com.example.hackmatefrontendfolder.ui.presentation.screens.teams

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hackmatefrontendfolder.ui.presentation.common.BottomNavigation
import com.example.hackmatefrontendfolder.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamsScreen(
    onNavigateToHome: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToRegistered: () -> Unit = {},
    onNotificationClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "TEAMS",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 2.sp
                        ),
                        color = AppColors.primary
                    )
                },
                actions = {
                    IconButton(onClick = onNotificationClick) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = AppColors.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppColors.surface.copy(alpha = 0.95f),
                    titleContentColor = AppColors.onSurface
                ),
                modifier = Modifier.shadow(
                    elevation = 8.dp,
                    spotColor = AppColors.primary.copy(alpha = 0.3f),
                    ambientColor = AppColors.primary.copy(alpha = 0.1f)
                )
            )
        },
        bottomBar = {
            BottomNavigation(
                selectedItem = 1,
                onItemSelected = { index ->
                    when (index) {
                        0 -> onNavigateToHome()
                        2 -> onNavigateToRegistered()
                        3 -> onNavigateToProfile()
                    }
                }
            )
        },
        containerColor = AppColors.background
    ) { paddingValues ->
        TeamsBackground {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Teams Screen\nComing Soon",
                    style = MaterialTheme.typography.headlineMedium,
                    color = AppColors.onSurface,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun TeamsBackground(content: @Composable BoxScope.() -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "background")
    val animatedOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2000f,
        animationSpec = infiniteRepeatable(
            animation = tween(30000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offset"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        PrimaryBlack,
                        Color(0xFF0A0A1A),
                        Color(0xFF1A1A2E),
                        PrimaryBlack
                    ),
                    center = Offset(animatedOffset * 0.3f, animatedOffset * 0.5f),
                    radius = 1200f
                )
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.sweepGradient(
                        colors = listOf(
                            Color.Transparent,
                            NeonCyan.copy(alpha = 0.01f),
                            Color.Transparent,
                            NeonYellow.copy(alpha = 0.005f),
                            Color.Transparent
                        )
                    )
                )
        )
        content()
    }
}

