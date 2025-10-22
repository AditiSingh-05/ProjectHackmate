package com.example.hackmatefrontendfolder.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


object AppColors {

    // Material3 ColorScheme Colors
    val primary: Color
        @Composable get() = MaterialTheme.colorScheme.primary
    val onPrimary: Color
        @Composable get() = MaterialTheme.colorScheme.onPrimary
    val primaryContainer: Color
        @Composable get() = MaterialTheme.colorScheme.primaryContainer
    val onPrimaryContainer: Color
        @Composable get() = MaterialTheme.colorScheme.onPrimaryContainer

    val secondary: Color
        @Composable get() = MaterialTheme.colorScheme.secondary
    val onSecondary: Color
        @Composable get() = MaterialTheme.colorScheme.onSecondary
    val secondaryContainer: Color
        @Composable get() = MaterialTheme.colorScheme.secondaryContainer
    val onSecondaryContainer: Color
        @Composable get() = MaterialTheme.colorScheme.onSecondaryContainer

    val tertiary: Color
        @Composable get() = MaterialTheme.colorScheme.tertiary
    val onTertiary: Color
        @Composable get() = MaterialTheme.colorScheme.onTertiary
    val tertiaryContainer: Color
        @Composable get() = MaterialTheme.colorScheme.tertiaryContainer
    val onTertiaryContainer: Color
        @Composable get() = MaterialTheme.colorScheme.onTertiaryContainer

    val background: Color
        @Composable get() = MaterialTheme.colorScheme.background
    val onBackground: Color
        @Composable get() = MaterialTheme.colorScheme.onBackground
    val surface: Color
        @Composable get() = MaterialTheme.colorScheme.surface
    val onSurface: Color
        @Composable get() = MaterialTheme.colorScheme.onSurface
    val surfaceVariant: Color
        @Composable get() = MaterialTheme.colorScheme.surfaceVariant
    val onSurfaceVariant: Color
        @Composable get() = MaterialTheme.colorScheme.onSurfaceVariant

    val outline: Color
        @Composable get() = MaterialTheme.colorScheme.outline
    val outlineVariant: Color
        @Composable get() = MaterialTheme.colorScheme.outlineVariant
    val scrim: Color
        @Composable get() = MaterialTheme.colorScheme.scrim

    val error: Color
        @Composable get() = MaterialTheme.colorScheme.error
    val onError: Color
        @Composable get() = MaterialTheme.colorScheme.onError
    val errorContainer: Color
        @Composable get() = MaterialTheme.colorScheme.errorContainer
    val onErrorContainer: Color
        @Composable get() = MaterialTheme.colorScheme.onErrorContainer

    val surfaceTint: Color
        @Composable get() = MaterialTheme.colorScheme.surfaceTint
    val inverseSurface: Color
        @Composable get() = MaterialTheme.colorScheme.inverseSurface
    val inverseOnSurface: Color
        @Composable get() = MaterialTheme.colorScheme.inverseOnSurface
    val inversePrimary: Color
        @Composable get() = MaterialTheme.colorScheme.inversePrimary

    // M3 Extended container elevations (if present in your theme)
    val surfaceContainer: Color
        @Composable get() = MaterialTheme.colorScheme.surfaceContainer
    val surfaceContainerHigh: Color
        @Composable get() = MaterialTheme.colorScheme.surfaceContainerHigh
    val surfaceContainerHighest: Color
        @Composable get() = MaterialTheme.colorScheme.surfaceContainerHighest
    val surfaceContainerLow: Color
        @Composable get() = MaterialTheme.colorScheme.surfaceContainerLow
    val surfaceContainerLowest: Color
        @Composable get() = MaterialTheme.colorScheme.surfaceContainerLowest

    // You may extend with any custom color or customColors if needed, for example:
    val neonCyan: Color
        @Composable get() = MaterialTheme.customColors.neonCyan
    val neonYellow: Color
        @Composable get() = MaterialTheme.customColors.neonYellow
    val glowCyan: Color
        @Composable get() = MaterialTheme.customColors.glowCyan
    val glowYellow: Color
        @Composable get() = MaterialTheme.customColors.glowYellow
    val deadlineUrgent: Color
        @Composable get() = MaterialTheme.customColors.deadlineUrgent
    val deadlineWarning: Color
        @Composable get() = MaterialTheme.customColors.deadlineWarning
    val deadlineSafe: Color
        @Composable get() = MaterialTheme.customColors.deadlineSafe
    val teamLeader: Color
        @Composable get() = MaterialTheme.customColors.teamLeader
    val registered: Color
        @Composable get() = MaterialTheme.customColors.registered
    val starred: Color
        @Composable get() = MaterialTheme.customColors.starred
    val success: Color
        @Composable get() = MaterialTheme.customColors.success
    val warning: Color
        @Composable get() = MaterialTheme.customColors.warning
    val info: Color
        @Composable get() = MaterialTheme.customColors.info
    val Accent: Color
        @Composable get() = MaterialTheme.customColors.Accent
//    val Secondary: Color
//        @Composable get() = MaterialTheme.customColors.Secondary
    val hologramBlue: Color
        @Composable get() = MaterialTheme.customColors.hologramBlue
    val matrixGreen: Color
        @Composable get() = MaterialTheme.customColors.matrixGreen
    val surfaceElevated: Color
        @Composable get() = MaterialTheme.customColors.surfaceElevated
    val surfaceCard: Color
        @Composable get() = MaterialTheme.customColors.surfaceCard
    val surfaceDialog: Color
        @Composable get() = MaterialTheme.customColors.surfaceDialog
    val borderAccent: Color
        @Composable get() = MaterialTheme.customColors.borderAccent
}