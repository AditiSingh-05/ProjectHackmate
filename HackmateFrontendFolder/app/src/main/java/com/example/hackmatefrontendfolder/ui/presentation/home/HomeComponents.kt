package com.example.hackmatefrontendfolder.ui.presentation.home

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.hackmatefrontendfolder.data.model.hackathon.Hackathon
import com.example.hackmatefrontendfolder.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

object HomeComponents {

    @Composable
    fun Background(
        content: @Composable BoxScope.() -> Unit
    ) {
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

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TopBar(
        title: String,
        onSearchClick: () -> Unit,
        onFilterClick: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        TopAppBar(
            title = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 2.sp
                    ),
                    color = AppColors.primary
                )
            },
            actions = {
                IconButton(onClick = onSearchClick) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = AppColors.primary
                    )
                }
                IconButton(onClick = onFilterClick) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "Filter",
                        tint = AppColors.primary
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = AppColors.surface.copy(alpha = 0.95f),
                titleContentColor = AppColors.onSurface
            ),
            modifier = modifier
                .shadow(
                    elevation = 8.dp,
                    spotColor = AppColors.primary.copy(alpha = 0.3f),
                    ambientColor = AppColors.primary.copy(alpha = 0.1f)
                )
        )
    }

    @Composable
    fun HackathonCard(
        hackathon: Hackathon,
        onCardClick: () -> Unit,
        onRegisterToggle: (Boolean) -> Unit,
        onStarToggle: (Boolean) -> Unit,
        modifier: Modifier = Modifier
    ) {
        var isRegistered by remember(hackathon.isRegistered) { mutableStateOf(hackathon.isRegistered) }
        var isStarred by remember(hackathon.isStarred) { mutableStateOf(hackathon.isStarred) }

        val cardScale by animateFloatAsState(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            ),
            label = "card_scale"
        )

        Card(
            modifier = modifier
                .fillMaxWidth()
                .scale(cardScale)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onCardClick() },
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                SurfaceCard.copy(alpha = 0.6f),
                                TertiaryBlack.copy(alpha = 0.8f)
                            )
                        )
                    )
                    .border(
                        width = 1.dp,
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                BorderColor,
                                AppColors.primary.copy(alpha = 0.3f),
                                BorderColor
                            )
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    // Top section with poster and urgency indicator
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        // Poster or placeholder
                        if (!hackathon.posterUrl.isNullOrBlank()) {
                            AsyncImage(
                                model = hackathon.posterUrl,
                                contentDescription = "Hackathon Poster",
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(
                                        width = 1.dp,
                                        color = AppColors.primary.copy(alpha = 0.5f),
                                        shape = RoundedCornerShape(12.dp)
                                    ),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .background(
                                        brush = Brush.linearGradient(
                                            colors = listOf(
                                                AppColors.primary.copy(alpha = 0.2f),
                                                AppColors.secondary.copy(alpha = 0.1f)
                                            )
                                        ),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .border(
                                        width = 1.dp,
                                        color = AppColors.primary.copy(alpha = 0.5f),
                                        shape = RoundedCornerShape(12.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.EmojiEvents,
                                    contentDescription = null,
                                    tint = AppColors.primary,
                                    modifier = Modifier.size(40.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        // Title and urgency
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = hackathon.title,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
                                ),
                                color = TextPrimary,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            UrgencyChip(urgencyLevel = hackathon.urgencyLevel)
                        }

                        // Star button
                        IconButton(
                            onClick = {
                                isStarred = !isStarred
                                onStarToggle(isStarred)
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = if (isStarred) Icons.Default.Star else Icons.Default.StarBorder,
                                contentDescription = if (isStarred) "Unstar" else "Star",
                                tint = if (isStarred) PrimaryYellow else TextSecondary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Description
                    Text(
                        text = hackathon.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Tags
                    if (hackathon.tags.isNotEmpty()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            hackathon.tags.take(3).forEach { tag ->
                                TagChip(tag = tag)
                            }
                            if (hackathon.tags.size > 3) {
                                TagChip(tag = "+${hackathon.tags.size - 3}")
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    // Organizer and location
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        if (!hackathon.organizer.isNullOrBlank()) {
                            InfoRow(
                                icon = Icons.Default.Business,
                                text = hackathon.organizer,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        if (!hackathon.location.isNullOrBlank()) {
                            InfoRow(
                                icon = Icons.Default.LocationOn,
                                text = hackathon.location,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Prize pool and deadline
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (!hackathon.prizePool.isNullOrBlank()) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.EmojiEvents,
                                    contentDescription = null,
                                    tint = PrimaryYellow,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = hackathon.prizePool,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    color = PrimaryYellow
                                )
                            }
                        }

                        DeadlineInfo(deadline = hackathon.deadline)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Stats and register button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Stats
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            StatItem(
                                icon = Icons.Default.Visibility,
                                count = hackathon.viewCount
                            )
                            StatItem(
                                icon = Icons.Default.People,
                                count = hackathon.registrationCount
                            )
                            StatItem(
                                icon = Icons.Default.Groups,
                                count = hackathon.teamCount
                            )
                        }

                        // Register button
                        RegisterButton(
                            isRegistered = isRegistered,
                            onClick = {
                                isRegistered = !isRegistered
                                onRegisterToggle(isRegistered)
                            }
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun UrgencyChip(urgencyLevel: String) {
        val (backgroundColor, textColor, label) = when (urgencyLevel.uppercase()) {
            "URGENT" -> Triple(
                DeadlineUrgent.copy(alpha = 0.2f),
                DeadlineUrgent,
                "🔥 URGENT"
            )
            "WARNING" -> Triple(
                DeadlineWarning.copy(alpha = 0.2f),
                DeadlineWarning,
                "⚠️ SOON"
            )
            "SAFE" -> Triple(
                DeadlineSafe.copy(alpha = 0.2f),
                DeadlineSafe,
                "✓ TIME"
            )
            else -> Triple(
                BorderColor.copy(alpha = 0.2f),
                TextSecondary,
                urgencyLevel
            )
        }

        Box(
            modifier = Modifier
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(6.dp)
                )
                .border(
                    width = 1.dp,
                    color = textColor.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(6.dp)
                )
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                ),
                color = textColor
            )
        }
    }

    @Composable
    private fun TagChip(tag: String) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            AppColors.primary.copy(alpha = 0.15f),
                            AppColors.secondary.copy(alpha = 0.1f)
                        )
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
                .border(
                    width = 1.dp,
                    color = AppColors.primary.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(
                text = tag,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = AppColors.primary
            )
        }
    }

    @Composable
    private fun InfoRow(
        icon: ImageVector,
        text: String,
        modifier: Modifier = Modifier
    ) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = TextSecondary,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }

    @Composable
    private fun DeadlineInfo(deadline: String) {
        val formattedDeadline = remember(deadline) {
            try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                val date = inputFormat.parse(deadline)
                date?.let { outputFormat.format(it) } ?: deadline
            } catch (e: Exception) {
                deadline
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Schedule,
                contentDescription = null,
                tint = AppColors.primary,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = formattedDeadline,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = AppColors.primary
            )
        }
    }

    @Composable
    private fun StatItem(icon: ImageVector, count: Long) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = TextTertiary,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = formatCount(count),
                style = MaterialTheme.typography.labelSmall,
                color = TextTertiary
            )
        }
    }

    @Composable
    private fun RegisterButton(
        isRegistered: Boolean,
        onClick: () -> Unit
    ) {
        val buttonColor by animateColorAsState(
            targetValue = if (isRegistered) RegisteredColor else AppColors.primary,
            animationSpec = tween(300),
            label = "button_color"
        )

        val buttonScale by animateFloatAsState(
            targetValue = if (isRegistered) 1.05f else 1f,
            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
            label = "button_scale"
        )

        Box(
            modifier = Modifier
                .scale(buttonScale)
                .background(
                    color = buttonColor.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(8.dp)
                )
                .border(
                    width = 1.dp,
                    color = buttonColor,
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onClick() }
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = if (isRegistered) Icons.Default.CheckCircle else Icons.Default.AppRegistration,
                    contentDescription = null,
                    tint = buttonColor,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = if (isRegistered) "REGISTERED" else "REGISTER",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    ),
                    color = buttonColor
                )
            }
        }
    }

    @Composable
    fun EmptyState(
        message: String,
        icon: ImageVector = Icons.Default.SearchOff,
        modifier: Modifier = Modifier
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = TextTertiary,
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
        }
    }

    @Composable
    fun LoadingIndicator(modifier: Modifier = Modifier) {
        Box(
            modifier = modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = AppColors.primary,
                strokeWidth = 3.dp,
                modifier = Modifier.size(48.dp)
            )
        }
    }

    private fun formatCount(count: Long): String {
        return when {
            count >= 1000000 -> String.format("%.1fM", count / 1000000.0)
            count >= 1000 -> String.format("%.1fK", count / 1000.0)
            else -> count.toString()
        }
    }
}