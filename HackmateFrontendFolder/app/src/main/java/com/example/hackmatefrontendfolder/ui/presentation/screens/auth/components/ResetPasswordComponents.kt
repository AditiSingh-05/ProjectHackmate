package com.example.hackmatefrontendfolder.ui.presentation.screens.auth.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.hackmatefrontendfolder.ui.theme.*

object ResetPasswordComponents {

    @Composable
    fun PasswordStrengthIndicator(
        password: String,
        modifier: Modifier = Modifier
    ) {
        val strength = calculatePasswordStrength(password)
        val animatedProgress by animateFloatAsState(
            targetValue = strength.progress,
            animationSpec = tween(500),
            label = "progress"
        )

        Column(modifier = modifier) {
            Text(
                text = "Password Strength",
                style = MaterialTheme.typography.bodySmall,
                color = AppColors.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .background(
                        color = AppColors.surfaceVariant,
                        shape = RoundedCornerShape(4.dp)
                    )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(animatedProgress)
                        .fillMaxHeight()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = strength.colors
                            ),
                            shape = RoundedCornerShape(4.dp)
                        )
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = strength.label,
                style = MaterialTheme.typography.bodySmall,
                color = strength.colors.first(),
                fontWeight = FontWeight.Medium
            )
        }
    }

    @Composable
    fun PasswordRequirements(
        password: String,
        modifier: Modifier = Modifier
    ) {
        val requirements = listOf(
            PasswordRequirement("At least 8 characters", password.length >= 8),
            PasswordRequirement("Contains uppercase letter", password.any { it.isUpperCase() }),
            PasswordRequirement("Contains lowercase letter", password.any { it.isLowerCase() }),
            PasswordRequirement("Contains number", password.any { it.isDigit() }),
            PasswordRequirement(
                "Contains special character",
                password.any { !it.isLetterOrDigit() })
        )

        Card(
            colors = CardDefaults.cardColors(
                containerColor = AppColors.surfaceVariant.copy(alpha = 0.3f)
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = modifier
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Password Requirements",
                    style = MaterialTheme.typography.titleSmall,
                    color = AppColors.onSurface,
                    fontWeight = FontWeight.SemiBold
                )

                requirements.forEach { requirement ->
                    RequirementItem(requirement = requirement)
                }
            }
        }
    }

    @Composable
    private fun RequirementItem(
        requirement: PasswordRequirement,
        modifier: Modifier = Modifier
    ) {
        val iconColor by animateColorAsState(
            targetValue = if (requirement.met) AppColors.success else AppColors.onSurfaceVariant,
            animationSpec = tween(300),
            label = "icon_color"
        )

        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (requirement.met) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(16.dp)
            )

            Text(
                text = requirement.text,
                style = MaterialTheme.typography.bodySmall,
                color = if (requirement.met) AppColors.success else AppColors.onSurfaceVariant
            )
        }
    }

    @Composable
    fun SecurityTip(
        modifier: Modifier = Modifier
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = AppColors.Accent.copy(alpha = 0.1f)
            ),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(
                width = 1.dp,
                color = AppColors.Accent.copy(alpha = 0.3f)
            ),
            modifier = modifier
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = null,
                    tint = AppColors.Accent,
                    modifier = Modifier.size(20.dp)
                )

                Column {
                    Text(
                        text = "Security Tip",
                        style = MaterialTheme.typography.titleSmall,
                        color = AppColors.Accent,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Use a unique password that you don't use for other accounts. Consider using a password manager.",
                        style = MaterialTheme.typography.bodySmall,
                        color = AppColors.onSurfaceVariant
                    )
                }
            }
        }
    }

    private fun calculatePasswordStrength(password: String): PasswordStrength {
        var score = 0

        if (password.length >= 8) score++
        if (password.any { it.isUpperCase() }) score++
        if (password.any { it.isLowerCase() }) score++
        if (password.any { it.isDigit() }) score++
        if (password.any { !it.isLetterOrDigit() }) score++

        return when (score) {
            0, 1 -> PasswordStrength(
                progress = 0.2f,
                label = "Very Weak",
                colors = listOf(ErrorRed, ErrorRed)
            )

            2 -> PasswordStrength(
                progress = 0.4f,
                label = "Weak",
                colors = listOf(WarningOrange, ErrorRed)
            )

            3 -> PasswordStrength(
                progress = 0.6f,
                label = "Fair",
                colors = listOf(PrimaryYellow, WarningOrange)
            )

            4 -> PasswordStrength(
                progress = 0.8f,
                label = "Good",
                colors = listOf(PrimaryCyan, PrimaryYellow)
            )

            else -> PasswordStrength(
                progress = 1f,
                label = "Strong",
                colors = listOf(SuccessGreen, PrimaryCyan)
            )
        }
    }

    private data class PasswordStrength(
        val progress: Float,
        val label: String,
        val colors: List<Color>
    )

    private data class PasswordRequirement(
        val text: String,
        val met: Boolean
    )
}