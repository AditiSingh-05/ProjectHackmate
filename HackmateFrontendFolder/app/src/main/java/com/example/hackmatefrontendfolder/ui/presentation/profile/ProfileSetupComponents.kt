package com.example.hackmatefrontendfolder.ui.presentation.profile

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hackmatefrontendfolder.ui.theme.*
import kotlinx.coroutines.delay

object ProfileSetupComponents {

    @Composable
    fun Background(
        content: @Composable BoxScope.() -> Unit
    ) {
        val infiniteTransition = rememberInfiniteTransition(label = "background")
        val animatedOffset by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1500f,
            animationSpec = infiniteRepeatable(
                animation = tween(20000, easing = LinearEasing),
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
                        center = Offset(animatedOffset * 0.5f, animatedOffset * 0.3f),
                        radius = 1000f
                    )
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                NeonCyan.copy(alpha = 0.02f),
                                Color.Transparent,
                                NeonYellow.copy(alpha = 0.01f),
                                Color.Transparent
                            )
                        )
                    )
            )
            content()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TextField(
        value: String,
        onValueChange: (String) -> Unit,
        label: String,
        icon: ImageVector,
        placeholder: String = "",
        keyboardType: KeyboardType = KeyboardType.Text,
        keyboardActions: KeyboardActions = KeyboardActions.Default,
        isError: Boolean = false,
        errorMessage: String? = null,
        maxLines: Int = 1,
        modifier: Modifier = Modifier
    ) {
        var isFocused by remember { mutableStateOf(false) }

        val borderAnimation by animateColorAsState(
            targetValue = when {
                isError -> ErrorRed
                isFocused -> NeonCyan
                value.isNotEmpty() -> PrimaryCyan.copy(alpha = 0.7f)
                else -> BorderColor
            },
            animationSpec = tween(400, easing = EaseInOutCubic),
            label = "border_animation"
        )

        Column(modifier = modifier) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp
                ),
                color = if (isFocused) PrimaryCyan else TextSecondary,
                modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
            )

            Surface(
                tonalElevation = 24.dp,
                shape = RoundedCornerShape(16.dp),
                color = Color.Transparent
            ) {
                OutlinedTextField(
                    value = value,
                    onValueChange = onValueChange,
                    placeholder = {
                        Text(
                            text = placeholder.ifEmpty { "Enter your ${label.lowercase()}" },
                            color = TextTertiary.copy(alpha = 0.7f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = when {
                                isError -> ErrorRed
                                isFocused -> NeonCyan
                                value.isNotEmpty() -> PrimaryCyan
                                else -> TextSecondary
                            },
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                    keyboardActions = keyboardActions,
                    maxLines = maxLines,
                    isError = isError,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AppColors.secondary.copy(0.8f),
                        unfocusedBorderColor = AppColors.onPrimary.copy(0.5f),
                        errorBorderColor = AppColors.error,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        cursorColor = NeonCyan,
                        focusedContainerColor = AppColors.surface,
                        unfocusedContainerColor = AppColors.surface,
                        errorContainerColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { focusState ->
                            isFocused = focusState.isFocused
                        }
                        .shadow(
                            elevation = if (isFocused) 32.dp else 16.dp,
                            shape = RoundedCornerShape(16.dp)
                        )
                )
            }

            AnimatedVisibility(
                visible = isError && errorMessage != null,
                enter = slideInVertically { -it } + fadeIn(),
                exit = slideOutVertically { -it } + fadeOut()
            ) {
                Row(
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Error,
                        contentDescription = null,
                        tint = ErrorRed,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = errorMessage ?: "",
                        color = ErrorRed,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }
        }
    }

    @Composable
    fun AvatarSelector(
        selectedAvatarId: String?,
        onAvatarSelected: (String) -> Unit,
        modifier: Modifier = Modifier
    ) {
        val avatarOptions = listOf(
            "avatar_1" to "👨‍💻",
            "avatar_2" to "👩‍💻",
            "avatar_3" to "🧑‍🎓",
            "avatar_4" to "👨‍🔬",
            "avatar_5" to "👩‍🔬",
            "avatar_6" to "🦾",
            "avatar_7" to "🤖",
            "avatar_8" to "👾"
        )

        Column(modifier = modifier) {
            Text(
                text = "CHOOSE AVATAR",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp
                ),
                color = TextSecondary,
                modifier = Modifier.padding(bottom = 16.dp, start = 4.dp)
            )

            LazyHorizontalGrid(
                rows = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.height(120.dp)
            ) {
                items(avatarOptions) { (id, emoji) ->
                    AvatarOption(
                        emoji = emoji,
                        isSelected = selectedAvatarId == id,
                        onClick = { onAvatarSelected(id) }
                    )
                }
            }
        }
    }

    @Composable
    private fun AvatarOption(
        emoji: String,
        isSelected: Boolean,
        onClick: () -> Unit
    ) {
        val animatedScale by animateFloatAsState(
            targetValue = if (isSelected) 1.1f else 1f,
            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
            label = "avatar_scale"
        )

        val borderColor by animateColorAsState(
            targetValue = if (isSelected) NeonCyan else BorderColor,
            animationSpec = tween(300),
            label = "avatar_border"
        )

        Box(
            modifier = Modifier
                .size(48.dp)
                .scale(animatedScale)
                .background(
                    brush = if (isSelected) {
                        Brush.radialGradient(
                            colors = listOf(
                                PrimaryCyan.copy(alpha = 0.2f),
                                Color.Transparent
                            )
                        )
                    } else {
                        Brush.radialGradient(
                            colors = listOf(
                                SurfaceCard.copy(alpha = 0.3f),
                                Color.Transparent
                            )
                        )
                    },
                    shape = CircleShape
                )
                .border(
                    width = if (isSelected) 3.dp else 1.dp,
                    color = borderColor,
                    shape = CircleShape
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = emoji,
                fontSize = 24.sp,
                textAlign = TextAlign.Center
            )
        }
    }

    @Composable
    fun SkillSelector(
        selectedSkills: List<String>,
        onSkillsChanged: (List<String>) -> Unit,
        modifier: Modifier = Modifier
    ) {
        val availableSkills = listOf(
            "Frontend", "Backend", "Mobile", "UI/UX", "Machine Learning",
            "Data Science", "DevOps", "Blockchain", "Game Dev", "AR/VR",
            "IoT", "Cybersecurity", "Cloud", "API Development", "Testing",
            "Product Management", "Marketing", "Business"
        )

        Column(modifier = modifier) {
            Text(
                text = "SKILLS & EXPERTISE",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp
                ),
                color = TextSecondary,
                modifier = Modifier.padding(bottom = 16.dp, start = 4.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.height(300.dp)
            ) {
                items(availableSkills) { skill ->
                    SkillChip(
                        skill = skill,
                        isSelected = selectedSkills.contains(skill),
                        onClick = {
                            val newSkills = if (selectedSkills.contains(skill)) {
                                selectedSkills - skill
                            } else {
                                selectedSkills + skill
                            }
                            onSkillsChanged(newSkills)
                        }
                    )
                }
            }
        }
    }

    @Composable
    private fun SkillChip(
        skill: String,
        isSelected: Boolean,
        onClick: () -> Unit
    ) {
        val animatedScale by animateFloatAsState(
            targetValue = if (isSelected) 1.05f else 1f,
            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
            label = "skill_scale"
        )

        val backgroundBrush = if (isSelected) {
            Brush.horizontalGradient(
                colors = listOf(
                    PrimaryCyan.copy(alpha = 0.8f),
                    NeonCyan.copy(alpha = 0.6f)
                )
            )
        } else {
            Brush.horizontalGradient(
                colors = listOf(
                    SurfaceCard.copy(alpha = 0.3f),
                    TertiaryBlack.copy(alpha = 0.5f)
                )
            )
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .scale(animatedScale)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onClick() },
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            shape = RoundedCornerShape(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(backgroundBrush)
                    .border(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) NeonCyan else BorderColor,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = skill,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        letterSpacing = 0.5.sp
                    ),
                    color = if (isSelected) Color.Black else TextPrimary,
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    @Composable
    fun YearSelector(
        selectedYear: String,
        onYearSelected: (String) -> Unit,
        modifier: Modifier = Modifier
    ) {
        val years = listOf("1st Year", "2nd Year", "3rd Year", "4th Year", "Graduate", "PhD")

        Column(modifier = modifier.selectableGroup()) {
            Text(
                text = "ACADEMIC YEAR",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp
                ),
                color = TextSecondary,
                modifier = Modifier.padding(bottom = 16.dp, start = 4.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.height(120.dp)
            ) {
                items(years) { year ->
                    YearOption(
                        year = year,
                        isSelected = selectedYear == year,
                        onClick = { onYearSelected(year) }
                    )
                }
            }
        }
    }

    @Composable
    private fun YearOption(
        year: String,
        isSelected: Boolean,
        onClick: () -> Unit
    ) {
        val animatedScale by animateFloatAsState(
            targetValue = if (isSelected) 1.05f else 1f,
            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
            label = "year_scale"
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .scale(animatedScale)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onClick() },
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            shape = RoundedCornerShape(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = if (isSelected) {
                            Brush.horizontalGradient(
                                colors = listOf(
                                    PrimaryYellow.copy(alpha = 0.8f),
                                    NeonYellow.copy(alpha = 0.6f)
                                )
                            )
                        } else {
                            Brush.horizontalGradient(
                                colors = listOf(
                                    SurfaceCard.copy(alpha = 0.3f),
                                    TertiaryBlack.copy(alpha = 0.5f)
                                )
                            )
                        }
                    )
                    .border(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) NeonYellow else BorderColor,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = year,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        letterSpacing = 0.5.sp
                    ),
                    color = if (isSelected) Color.Black else TextPrimary,
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    @Composable
    fun CompletionIndicator(
        completionPercentage: Int,
        modifier: Modifier = Modifier
    ) {
        val animatedProgress by animateFloatAsState(
            targetValue = completionPercentage / 100f,
            animationSpec = tween(1000, easing = EaseInOutCubic),
            label = "completion_progress"
        )

        Column(modifier = modifier) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "PROFILE COMPLETION",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.sp
                    ),
                    color = TextSecondary
                )
                Text(
                    text = "$completionPercentage%",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = when {
                        completionPercentage >= 80 -> SuccessGreen
                        completionPercentage >= 50 -> PrimaryYellow
                        else -> ErrorRed
                    }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .background(
                        color = SurfaceCard,
                        shape = RoundedCornerShape(4.dp)
                    )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(animatedProgress)
                        .fillMaxHeight()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = when {
                                    completionPercentage >= 80 -> listOf(SuccessGreen, MatrixGreen)
                                    completionPercentage >= 50 -> listOf(PrimaryYellow, NeonYellow)
                                    else -> listOf(ErrorRed, ErrorRed.copy(alpha = 0.7f))
                                }
                            ),
                            shape = RoundedCornerShape(4.dp)
                        )
                )
            }
        }
    }

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    fun Button(
        text: String,
        onClick: () -> Unit,
        isLoading: Boolean = false,
        isSuccess: Boolean = false,
        isError: Boolean = false,
        modifier: Modifier = Modifier
    ) {
        var buttonState by remember { mutableStateOf(ButtonState.NORMAL) }
        val keyboardController = LocalSoftwareKeyboardController.current

        LaunchedEffect(isLoading, isSuccess, isError) {
            buttonState = when {
                isLoading -> ButtonState.LOADING
                isSuccess -> ButtonState.SUCCESS
                isError -> ButtonState.ERROR
                else -> ButtonState.NORMAL
            }
        }

        val animatedScale by animateFloatAsState(
            targetValue = when (buttonState) {
                ButtonState.LOADING -> 0.98f
                ButtonState.SUCCESS -> 1.05f
                ButtonState.ERROR -> 0.95f
                else -> 1f
            },
            animationSpec = when (buttonState) {
                ButtonState.SUCCESS -> spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
                else -> tween(200)
            },
            label = "button_scale"
        )

        val buttonGradient = when (buttonState) {
            ButtonState.SUCCESS -> Brush.horizontalGradient(
                colors = listOf(SuccessGreen, MatrixGreen, SuccessGreen)
            )
            ButtonState.ERROR -> Brush.horizontalGradient(
                colors = listOf(ErrorRed, ErrorRed.copy(alpha = 0.8f), ErrorRed)
            )
            ButtonState.LOADING -> Brush.horizontalGradient(
                colors = listOf(
                    PrimaryCyan.copy(alpha = 0.6f),
                    NeonCyan.copy(alpha = 0.4f),
                    PrimaryCyan.copy(alpha = 0.6f)
                )
            )
            else -> Brush.horizontalGradient(
                colors = listOf(PrimaryCyan, NeonCyan, PrimaryCyan)
            )
        }

        Card(
            modifier = modifier
                .fillMaxWidth()
                .height(56.dp)
                .scale(animatedScale)
                .shadow(
                    elevation = when (buttonState) {
                        ButtonState.SUCCESS -> 20.dp
                        ButtonState.ERROR -> 8.dp
                        ButtonState.LOADING -> 12.dp
                        else -> 16.dp
                    },
                    shape = RoundedCornerShape(16.dp)
                ),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            shape = RoundedCornerShape(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(buttonGradient)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        keyboardController?.hide()
                        onClick()
                    },
                contentAlignment = Alignment.Center
            ) {
                AnimatedContent(
                    targetState = buttonState,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(300)) + scaleIn(animationSpec = tween(300)) with
                                fadeOut(animationSpec = tween(300)) + scaleOut(animationSpec = tween(300))
                    },
                    label = "button_content"
                ) { state ->
                    when (state) {
                        ButtonState.LOADING -> {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(
                                    color = Color.Black,
                                    strokeWidth = 2.dp,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "CREATING PROFILE...",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        letterSpacing = 1.5.sp
                                    ),
                                    color = Color.Black
                                )
                            }
                        }
                        ButtonState.SUCCESS -> {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = Color.Black,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "PROFILE CREATED!",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        letterSpacing = 2.sp
                                    ),
                                    color = Color.Black
                                )
                            }
                        }
                        ButtonState.ERROR -> {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Error,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "ERROR!",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        letterSpacing = 2.sp
                                    ),
                                    color = Color.White
                                )
                            }
                        }
                        else -> {
                            Text(
                                text = text.uppercase(),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 2.sp,
                                    fontSize = 16.sp
                                ),
                                color = Color.Black
                            )
                        }
                    }
                }
            }
        }

        LaunchedEffect(buttonState) {
            if (buttonState == ButtonState.SUCCESS || buttonState == ButtonState.ERROR) {
                delay(2500)
                buttonState = ButtonState.NORMAL
            }
        }
    }

    private enum class ButtonState {
        NORMAL, LOADING, SUCCESS, ERROR
    }
}