package com.example.hackmatefrontendfolder.ui.presentation.auth

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hackmatefrontendfolder.ui.theme.*
import kotlinx.coroutines.delay
import java.lang.Math.*
import kotlin.math.*
import com.example.hackmatefrontendfolder.R
object AuthComponents {

    @Composable
    fun Background(
        content: @Composable BoxScope.() -> Unit
    ) {
val systemInDarkMode = isSystemInDarkTheme()


        Box(

        ) {



            content()
        }
    }

    @Composable
    fun AuthModeToggle(
        isSignUp: Boolean,
        onToggle: (Boolean) -> Unit,
        modifier: Modifier = Modifier
    ) {
        val animatedScale by animateFloatAsState(
            targetValue = if (isSignUp) 1.08f else 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            ),
            label = "scale"
        )



        Card(
            modifier = modifier
                .fillMaxWidth()
                .height(64.dp)
                .scale(animatedScale)
               ,
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            ),
            shape = RoundedCornerShape(24.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                SurfaceCard.copy(alpha = 0.15f),
                                TertiaryBlack.copy(alpha = 0.25f),
                                SurfaceCard.copy(alpha = 0.15f)
                            )
                        )
                    )
                    .border(
                        width = 2.dp,
                        color = AppColors.glowYellow
                        ,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ToggleButton(
                        text = "SIGN UP",
                        isSelected = isSignUp,
                        onClick = { onToggle(true) },
                        modifier = Modifier.weight(1f)
                    )

                    ToggleButton(
                        text = "LOGIN",
                        isSelected = !isSignUp,
                        onClick = { onToggle(false) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }

    @Composable
    private fun ToggleButton(
        text: String,
        isSelected: Boolean,
        onClick: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        val animatedScale by animateFloatAsState(
            targetValue = if (isSelected) 1.05f else 0.95f,
            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
            label = "button_scale"
        )

        val backgroundBrush = if (isSelected) {
            Brush.horizontalGradient(
                colors = listOf(
                    PrimaryCyan.copy(alpha = 0.9f),
                    NeonCyan.copy(alpha = 0.7f),
                    PrimaryCyan.copy(alpha = 0.9f)
                )
            )
        } else {
            Brush.horizontalGradient(
                colors = listOf(Color.Transparent, Color.Transparent)
            )
        }

        Box(
            modifier = modifier
                .fillMaxHeight()
                .scale(animatedScale)
                .clip(RoundedCornerShape(20.dp))
                .background(backgroundBrush)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onClick() }
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = if (isSelected) Color.Black else TextSecondary,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                    letterSpacing = 2.sp,
                    fontSize = 14.sp
                ),
                textAlign = TextAlign.Center
            )

            // Glow effect for selected state
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    NeonCyan.copy(alpha = 0.1f),
                                    Color.Transparent
                                ),
                                radius = 100f
                            ),
                            shape = RoundedCornerShape(20.dp)
                        )
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TextField(
        value: String,
        onValueChange: (String) -> Unit,
        label: String,
        icon: ImageVector,
        isPassword: Boolean = false,
        keyboardType: KeyboardType = KeyboardType.Text,
        keyboardActions: KeyboardActions = KeyboardActions.Default,
        isError: Boolean = false,
        errorMessage: String? = null,
        modifier: Modifier = Modifier
    ) {
        var passwordVisible by remember { mutableStateOf(false) }
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


        val backgroundAlpha by animateFloatAsState(
            targetValue = if (isFocused) 0.15f else 0.05f,
            animationSpec = tween(300),
            label = "background_alpha"
        )

        Column(modifier = modifier) {

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
                        text = "Enter your ${label.lowercase()}",
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
                        modifier = Modifier
                            .size(24.dp)
                            .alpha(if (isFocused) 1f else 0.7f)
                    )
                },
                trailingIcon = if (isPassword) {
                    {
                        IconButton(
                            onClick = { passwordVisible = !passwordVisible },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.Visibility
                                else Icons.Default.VisibilityOff,
                                contentDescription = null,
                                tint = TextSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                } else null,
                visualTransformation = if (isPassword && !passwordVisible) {
                    PasswordVisualTransformation()
                } else VisualTransformation.None,
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                keyboardActions = keyboardActions,
                singleLine = true,
                isError = isError,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AppColors.secondaryContainer,
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
                    .fillMaxSize()
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                    }
                    .shadow(
                        elevation = 24.dp,
                        shape = RoundedCornerShape(16.dp),

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
                colors = listOf(
                    SuccessGreen,
                    MatrixGreen,
                    SuccessGreen
                )
            )
            ButtonState.ERROR -> Brush.horizontalGradient(
                colors = listOf(
                    ErrorRed,
                    ErrorRed.copy(alpha = 0.8f),
                    ErrorRed
                )
            )
            ButtonState.LOADING -> Brush.horizontalGradient(
                colors = listOf(
                    PrimaryCyan.copy(alpha = 0.6f),
                    NeonCyan.copy(alpha = 0.4f),
                    PrimaryCyan.copy(alpha = 0.6f)
                )
            )
            else -> Brush.horizontalGradient(
                colors = listOf(
                    PrimaryCyan,
                    NeonCyan,
                    PrimaryCyan
                )
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
                    shape = RoundedCornerShape(16.dp),
                    ambientColor = when (buttonState) {
                        ButtonState.SUCCESS -> SuccessGreen.copy(alpha = 0.4f)
                        ButtonState.ERROR -> ErrorRed.copy(alpha = 0.4f)
                        else -> PrimaryCyan.copy(alpha = 0.4f)
                    },
                    spotColor = when (buttonState) {
                        ButtonState.SUCCESS -> MatrixGreen.copy(alpha = 0.6f)
                        ButtonState.ERROR -> ErrorRed.copy(alpha = 0.6f)
                        else -> NeonCyan.copy(alpha = 0.6f)
                    }
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
                                    text = "PROCESSING...",
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
                                    text = "SUCCESS!",
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

    @Composable
    fun HackmateTitle(
        modifier: Modifier = Modifier
    ) {
        val infiniteTransition = rememberInfiniteTransition(label = "title_animation")

        val glowIntensity by infiniteTransition.animateFloat(
            initialValue = 0.3f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(2500, easing = EaseInOutSine),
                repeatMode = RepeatMode.Reverse
            ),
            label = "glow_intensity"
        )

        val textOffset by infiniteTransition.animateFloat(
            initialValue = -2f,
            targetValue = 2f,
            animationSpec = infiniteRepeatable(
                animation = tween(4000, easing = EaseInOutSine),
                repeatMode = RepeatMode.Reverse
            ),
            label = "text_offset"
        )

        Box(
            modifier = modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            // Background glow effect
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                NeonCyan.copy(alpha = glowIntensity * 0.1f),
                                PrimaryCyan.copy(alpha = glowIntensity * 0.08f),
                                Color.Transparent
                            ),
                            radius = 300f
                        ),
                        shape = RoundedCornerShape(24.dp)
                    )
            )

            // Main title with effects
            Text(
                text = "HACKMATE",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = 52.sp,
                    letterSpacing = 6.sp,
                    fontWeight = FontWeight.Black
                ),
                color = TextPrimary,
                modifier = Modifier
                    .offset(x = textOffset.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                PrimaryCyan.copy(alpha = glowIntensity * 0.15f),
                                NeonYellow.copy(alpha = glowIntensity * 0.12f),
                                PrimaryCyan.copy(alpha = glowIntensity * 0.15f)
                            )
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            )

            // Glitch effect overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color.Transparent,
                                NeonCyan.copy(alpha = glowIntensity * 0.05f),
                                Color.Transparent,
                                NeonYellow.copy(alpha = glowIntensity * 0.03f),
                                Color.Transparent
                            )
                        ),
                        shape = RoundedCornerShape(24.dp)
                    )
            )
        }
    }

    @Composable
    fun AuthCard(
        content: @Composable ColumnScope.() -> Unit,
    ) {
        val systemInDarkMode = isSystemInDarkTheme()
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 32.dp,
                    shape = RoundedCornerShape(
                        topStart = 40.dp,
                        topEnd = 40.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 0.dp
                    ),
                    ambientColor = PrimaryCyan.copy(alpha = 0.2f),
                    spotColor = NeonCyan.copy(alpha = 0.3f)
                ),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            shape = RoundedCornerShape(
                topStart = 40.dp,
                topEnd = 40.dp,
                bottomStart = 0.dp,
                bottomEnd = 0.dp
            )
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                SurfaceCard.copy(alpha = 0.95f),
                                TertiaryBlack.copy(alpha = 0.98f),
                                SecondaryBlack.copy(alpha = 1f)
                            )
                        )
                    )
                    .border(
                        width = 1.dp,
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                BorderAccent.copy(alpha = 0.6f),
                                NeonYellow.copy(alpha = 0.4f),
                                BorderAccent.copy(alpha = 0.6f)
                            )
                        ),
                        shape = RoundedCornerShape(
                            topStart = 40.dp,
                            topEnd = 40.dp,
                            bottomStart = 0.dp,
                            bottomEnd = 0.dp
                        )
                    )
            ) {
                Image(
                    painter = painterResource(id =
                        if(systemInDarkMode){
                            R.drawable.bgdark
                        }else{
                            R.drawable.bglight
                        }

                    ),
                    contentDescription = "BackGround",

                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.fillMaxSize()
                )
                Column(
                    modifier = Modifier.fillMaxWidth()
                        .padding(32.dp)
                    ,
                    content = content
                )
            }
        }
    }



    private enum class ButtonState {
        NORMAL, LOADING, SUCCESS, ERROR
    }
}