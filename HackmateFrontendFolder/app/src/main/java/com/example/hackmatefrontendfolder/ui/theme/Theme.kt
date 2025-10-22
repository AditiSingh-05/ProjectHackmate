package com.example.hackmatefrontendfolder.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * HackMate Theme -  Black, Cyan & Yellow Aesthetic
 * Professional dark theme with neon accents for modern tech feel
 */

/**
 * Dark Color Scheme - Primary theme matching  aesthetic
 * Uses black, cyan, and yellow color palette with tech accents
 */
private val DarkColorScheme = darkColorScheme(
    // Primary colors - Cyan brand identity
    primary = PrimaryCyan,                    // Bright cyan for buttons, links
    onPrimary = PrimaryBlack,                 // Black text on cyan elements
    primaryContainer = PrimaryCyanDark,       // Darker cyan containers
    onPrimaryContainer = TextPrimary,         // White text on cyan containers

    // Secondary colors - Yellow accents
    secondary = PrimaryYellow,                // Yellow for secondary actions
    onSecondary = PrimaryBlack,               // Black text on yellow elements
    secondaryContainer = PrimaryYellowDark,   // Darker yellow containers
    onSecondaryContainer = PrimaryBlack,      // Black text on yellow containers

    // Tertiary colors - Special highlights
    tertiary = NeonCyan,                      // Neon cyan for special accents
    onTertiary = PrimaryBlack,                // Black text on tertiary elements
    tertiaryContainer = TertiaryBlack,        // Dark containers for tertiary
    onTertiaryContainer = TextSecondary,      // Light text on tertiary containers

    // Background colors - Main app surfaces
    background = PrimaryBlack,                // Pure black background
    onBackground = TextPrimary,               // White text on background
    surface = SecondaryBlack,                 // Card surfaces, dialogs
    onSurface = TextPrimary,                  // White text on surfaces
    surfaceVariant = TertiaryBlack,          // Elevated surfaces
    onSurfaceVariant = TextSecondary,        // Light gray text on surface variants

    // Utility colors
    outline = BorderColor,                    // Borders, dividers
    outlineVariant = DividerColor,           // Subtle borders
    scrim = Color.Black.copy(alpha = 0.7f),  // Modal overlays with more opacity

    // System colors
    error = ErrorRed,                         // Error states
    onError = TextPrimary,                    // White text on error elements
    errorContainer = ErrorRed.copy(alpha = 0.15f), // Error containers with transparency
    onErrorContainer = ErrorRed,              // Error color text on error containers

    // Surface tints and containers
    surfaceTint = PrimaryCyan,                // Surface tinting with cyan
    inverseSurface = TextPrimary,             // White surface for contrast
    inverseOnSurface = PrimaryBlack,          // Black text on inverse surface
    inversePrimary = PrimaryCyanLight,        // Light cyan for inverse primary

    // Container colors for various states
    surfaceContainer = SurfaceCard,           // Card container background
    surfaceContainerHigh = SurfaceElevated,   // Elevated container background
    surfaceContainerHighest = QuaternaryBlack, // Highest elevation containers
    surfaceContainerLow = SecondaryBlack,     // Low elevation containers
    surfaceContainerLowest = PrimaryBlack,    // Lowest elevation containers
)

/**
 * Light Color Scheme - Optional light mode
 * Maintains  feel with lighter backgrounds
 */
private val LightColorScheme = lightColorScheme(
    primary = PrimaryCyanDark,
    onPrimary = Color.White,
    primaryContainer = PrimaryCyanLight,
    onPrimaryContainer = PrimaryCyanDark,

    secondary = PrimaryYellowDark,
    onSecondary = PrimaryBlack,
    secondaryContainer = PrimaryYellowLight,
    onSecondaryContainer = PrimaryYellowDark,

    tertiary = InfoBlue,
    onTertiary = Color.White,
    tertiaryContainer = InfoBlue.copy(alpha = 0.1f),
    onTertiaryContainer = InfoBlue,

    background = LightBackground,
    onBackground = LightOnSurface,
    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = Color(0xFF64748B),

    outline = Color(0xFFCBD5E1),
    outlineVariant = Color(0xFFE2E8F0),

    error = ErrorRed,
    onError = Color.White,
    errorContainer = ErrorRed.copy(alpha = 0.1f),
    onErrorContainer = ErrorRed,

    surfaceTint = PrimaryCyanDark,
    inverseSurface = Color(0xFF1F1F1F),
    inverseOnSurface = Color.White,
    inversePrimary = PrimaryCyan,
)

/**
 * Main Theme Composable
 * Handles dark/light theme switching and system bar configuration
 */
@Composable
fun HackmateFrontendFolderTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color disabled to maintain consistent  branding
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    // Select color scheme based on theme preference
    val colorScheme = when {
        // Note: Dynamic colors disabled to maintain consistent  branding
        // Enable if you want Material You color adaptation
        // dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        //     val context = LocalContext.current
        //     if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        // }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // Configure system bars to match  theme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()

            // Set system bar content colors for better visibility
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

/**
 * Extension properties for easy access to custom colors
 */
val MaterialTheme.customColors: HackMateCustomColors
    @Composable
    get() = if (isSystemInDarkTheme()) {
        DarkCustomColors
    } else {
        LightCustomColors
    }

/**
 * Custom colors not covered by Material Theme
 * -specific colors and effects
 */
data class HackMateCustomColors(
    val neonCyan: Color,
    val neonYellow: Color,
    val glowCyan: Color,
    val glowYellow: Color,
    val deadlineUrgent: Color,
    val deadlineWarning: Color,
    val deadlineSafe: Color,
    val teamLeader: Color,
    val registered: Color,
    val starred: Color,
    val success: Color,
    val warning: Color,
    val info: Color,
    val Accent: Color,
    val Secondary: Color,
    val hologramBlue: Color,
    val matrixGreen: Color,
    val surfaceElevated: Color,
    val surfaceCard: Color,
    val surfaceDialog: Color,
    val borderAccent: Color
)

private val DarkCustomColors = HackMateCustomColors(
    neonCyan = NeonCyan,
    neonYellow = NeonYellow,
    glowCyan = GlowCyan,
    glowYellow = GlowYellow,
    deadlineUrgent = DeadlineUrgent,
    deadlineWarning = DeadlineWarning,
    deadlineSafe = DeadlineSafe,
    teamLeader = TeamLeaderColor,
    registered = RegisteredColor,
    starred = StarredColor,
    success = SuccessGreen,
    warning = WarningOrange,
    info = InfoBlue,
    Accent = Accent,
    Secondary = Secondary,
    hologramBlue = HologramBlue,
    matrixGreen = MatrixGreen,
    surfaceElevated = SurfaceElevated,
    surfaceCard = SurfaceCard,
    surfaceDialog = SurfaceDialog,
    borderAccent = BorderAccent
)

private val LightCustomColors = HackMateCustomColors(
    neonCyan = PrimaryCyan,
    neonYellow = PrimaryYellow,
    glowCyan = PrimaryCyanDark,
    glowYellow = PrimaryYellowDark,
    deadlineUrgent = DeadlineUrgent,
    deadlineWarning = DeadlineWarning,
    deadlineSafe = DeadlineSafe,
    teamLeader = TeamLeaderColor,
    registered = RegisteredColor,
    starred = StarredColor,
    success = SuccessGreen,
    warning = WarningOrange,
    info = InfoBlue,
    Accent = PrimaryCyanDark,
    Secondary = PrimaryYellowDark,
    hologramBlue = HologramBlue,
    matrixGreen = MatrixGreen,
    surfaceElevated = Color.White,
    surfaceCard = Color.White,
    surfaceDialog = Color.White,
    borderAccent = PrimaryCyanDark
)

/**
 * Preview composable for theme testing
 */
@Composable
fun ThemePreview(content: @Composable () -> Unit) {
    HackmateFrontendFolderTheme {
        content()
    }
}