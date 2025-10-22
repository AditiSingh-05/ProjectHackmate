package com.example.hackmatefrontendfolder.ui.presentation.createhackathon

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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

object CreateHackathonComponents {

    @Composable
    fun Background(
        content: @Composable BoxScope.() -> Unit
    ) {
        val infiniteTransition = rememberInfiniteTransition(label = "background")
        val animatedOffset by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 2000f,
            animationSpec = infiniteRepeatable(
                animation = tween(25000, easing = LinearEasing),
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
                        center = Offset(animatedOffset * 0.4f, animatedOffset * 0.6f),
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
        singleLine: Boolean = true,
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
                            text = placeholder.ifEmpty { "Enter ${label.lowercase()}" },
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
                    singleLine = singleLine,
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
    fun TagSelector(
        selectedTags: List<String>,
        onTagsChanged: (List<String>) -> Unit,
        modifier: Modifier = Modifier
    ) {
        val predefinedTags = listOf(
            "Web Dev", "Mobile", "AI/ML", "Blockchain", "IoT", "AR/VR",
            "Gaming", "FinTech", "HealthTech", "EdTech", "ClimaTech", "SocialGood",
            "Open Source", "Hardware", "Design", "Data Science", "DevOps", "Cloud"
        )

        var customTag by remember { mutableStateOf("") }

        Column(modifier = modifier) {
            Text(
                text = "HACKATHON TAGS",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp
                ),
                color = TextSecondary,
                modifier = Modifier.padding(bottom = 16.dp, start = 4.dp)
            )

            // Selected tags display
            if (selectedTags.isNotEmpty()) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    items(selectedTags) { tag ->
                        SelectedTagChip(
                            tag = tag,
                            onRemove = { onTagsChanged(selectedTags - tag) }
                        )
                    }
                }
            }

            // Custom tag input
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = customTag,
                    onValueChange = { customTag = it },
                    placeholder = {
                        Text(
                            text = "Add custom tag",
                            color = TextTertiary.copy(alpha = 0.7f),
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AppColors.secondary.copy(0.8f),
                        unfocusedBorderColor = AppColors.onPrimary.copy(0.5f),
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        cursorColor = NeonCyan,
                        focusedContainerColor = AppColors.surface,
                        unfocusedContainerColor = AppColors.surface
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f),
                    textStyle = MaterialTheme.typography.bodySmall
                )

                IconButton(
                    onClick = {
                        if (customTag.isNotBlank() && !selectedTags.contains(customTag)) {
                            onTagsChanged(selectedTags + customTag)
                            customTag = ""
                        }
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(PrimaryCyan, NeonCyan)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add tag",
                        tint = Color.Black
                    )
                }
            }

            // Predefined tags
            Text(
                text = "Popular Tags",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.5.sp
                ),
                color = TextSecondary,
                modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(predefinedTags.filter { !selectedTags.contains(it) }) { tag ->
                    PredefinedTagChip(
                        tag = tag,
                        onClick = { onTagsChanged(selectedTags + tag) }
                    )
                }
            }
        }
    }

    @Composable
    private fun SelectedTagChip(
        tag: String,
        onRemove: () -> Unit
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            PrimaryCyan.copy(alpha = 0.8f),
                            NeonCyan.copy(alpha = 0.6f)
                        )
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
                .border(
                    width = 1.dp,
                    color = NeonCyan,
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = tag,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = Color.Black
                )

                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remove tag",
                    tint = Color.Black,
                    modifier = Modifier
                        .size(16.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onRemove() }
                )
            }
        }
    }

    @Composable
    private fun PredefinedTagChip(
        tag: String,
        onClick: () -> Unit
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            SurfaceCard.copy(alpha = 0.3f),
                            TertiaryBlack.copy(alpha = 0.5f)
                        )
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
                .border(
                    width = 1.dp,
                    color = BorderColor,
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onClick() }
        ) {
            Text(
                text = tag,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = TextPrimary,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }
    }

    @Composable
    fun TeamSizeSelector(
        minTeamSize: String,
        maxTeamSize: String,
        onMinSizeChange: (String) -> Unit,
        onMaxSizeChange: (String) -> Unit,
        modifier: Modifier = Modifier
    ) {
        Column(modifier = modifier) {
            Text(
                text = "TEAM SIZE LIMITS",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp
                ),
                color = TextSecondary,
                modifier = Modifier.padding(bottom = 16.dp, start = 4.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TextField(
                    value = minTeamSize,
                    onValueChange = onMinSizeChange,
                    label = "MIN SIZE",
                    icon = Icons.Default.People,
                    placeholder = "2",
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.weight(1f)
                )

                TextField(
                    value = maxTeamSize,
                    onValueChange = onMaxSizeChange,
                    label = "MAX SIZE",
                    icon = Icons.Default.Groups,
                    placeholder = "6",
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }

    @Composable
    fun DateTimeSelector(
        selectedDateTime: String,
        onDateTimeSelected: (String) -> Unit,
        label: String,
        icon: ImageVector,
        modifier: Modifier = Modifier
    ) {
        var showDatePicker by remember { mutableStateOf(false) }

        Column(modifier = modifier) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp
                ),
                color = TextSecondary,
                modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
            )

            Card(
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true }
                    .shadow(
                        elevation = 16.dp,
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                Surface(
                    tonalElevation = 24.dp,
                    shape = RoundedCornerShape(16.dp),
                    color = AppColors.surface
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = if (selectedDateTime.isNotEmpty()) PrimaryCyan else TextSecondary,
                            modifier = Modifier.size(24.dp)
                        )

                        Text(
                            text = selectedDateTime.ifEmpty { "Select $label" },
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (selectedDateTime.isNotEmpty()) TextPrimary else TextTertiary,
                            modifier = Modifier.weight(1f)
                        )

                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Open calendar",
                            tint = TextSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        // Note: You'll need to implement actual date picker dialog here
        // For now, using a simple text input fallback
        if (showDatePicker) {
            AlertDialog(
                onDismissRequest = { showDatePicker = false },
                title = { Text("Set $label") },
                text = {
                    var tempDateTime by remember { mutableStateOf(selectedDateTime) }
                    OutlinedTextField(
                        value = tempDateTime,
                        onValueChange = { tempDateTime = it },
                        label = { Text("YYYY-MM-DD HH:MM:SS") },
                        placeholder = { Text("2025-01-15 18:00:00") }
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onDateTimeSelected(selectedDateTime)
                            showDatePicker = false
                        }
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }

    @Composable
    fun AIExtractionCard(
        originalMessage: String,
        onMessageChange: (String) -> Unit,
        onExtract: () -> Unit,
        isExtracting: Boolean,
        modifier: Modifier = Modifier
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = AppColors.secondary.copy(alpha = 0.1f)
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            PrimaryYellow.copy(alpha = 0.5f),
                            NeonYellow.copy(alpha = 0.3f)
                        )
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = PrimaryYellow,
                        modifier = Modifier.size(24.dp)
                    )

                    Text(
                        text = "AI HACKATHON EXTRACTOR",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        ),
                        color = PrimaryYellow
                    )
                }

                Text(
                    text = "Paste hackathon announcement text below and let AI extract all the details automatically",
                    style = MaterialTheme.typography.bodySmall,
                    color = AppColors.onSurfaceVariant
                )

                OutlinedTextField(
                    value = originalMessage,
                    onValueChange = onMessageChange,
                    placeholder = {
                        Text(
                            text = "Paste hackathon announcement here...\n\nExample:\n\"Join our 48-hour AI hackathon! Registration deadline: Jan 15, 2025. Prize pool: $10,000. Team size: 2-5 members.\"",
                            color = TextTertiary.copy(alpha = 0.7f),
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    maxLines = 6,
                    singleLine = false,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryYellow,
                        unfocusedBorderColor = BorderColor,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        cursorColor = PrimaryYellow,
                        focusedContainerColor = SurfaceCard.copy(alpha = 0.3f),
                        unfocusedContainerColor = SurfaceCard.copy(alpha = 0.1f)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    text = "Extract with AI",
                    onClick = onExtract,
                    isLoading = isExtracting,
                    enabled = originalMessage.isNotBlank(),
                    modifier = Modifier.fillMaxWidth()
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
        enabled: Boolean = true,
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

        val buttonGradient = when {
            !enabled -> Brush.horizontalGradient(
                colors = listOf(BorderColor, BorderColor)
            )
            buttonState == ButtonState.SUCCESS -> Brush.horizontalGradient(
                colors = listOf(SuccessGreen, MatrixGreen, SuccessGreen)
            )
            buttonState == ButtonState.ERROR -> Brush.horizontalGradient(
                colors = listOf(ErrorRed, ErrorRed.copy(alpha = 0.8f), ErrorRed)
            )
            buttonState == ButtonState.LOADING -> Brush.horizontalGradient(
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
                .height(56.dp)
                .scale(animatedScale)
                .shadow(
                    elevation = if (enabled) {
                        when (buttonState) {
                            ButtonState.SUCCESS -> 20.dp
                            ButtonState.ERROR -> 8.dp
                            ButtonState.LOADING -> 12.dp
                            else -> 16.dp
                        }
                    } else 4.dp,
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
                        enabled = enabled,
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
                                    text = "CREATING...",
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
                                    text = "CREATED!",
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
                                color = if (enabled) Color.Black else TextTertiary
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