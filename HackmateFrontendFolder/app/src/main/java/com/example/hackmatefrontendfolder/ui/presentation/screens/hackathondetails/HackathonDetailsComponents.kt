package com.example.hackmatefrontendfolder.ui.presentation.screens.hackathondetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.hackmatefrontendfolder.domain.model.hackathon.HackathonDetailsResponse
import com.example.hackmatefrontendfolder.ui.theme.AppColors

object HackathonDetailsComponents {

    @Composable
    fun LoadingState() {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = AppColors.primary,
                strokeWidth = 3.dp,
                modifier = Modifier.size(48.dp)
            )
        }
    }

    @Composable
    fun ErrorState(
        message: String,
        onRetry: () -> Unit
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ErrorOutline,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = AppColors.error
                )
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyLarge,
                    color = AppColors.onBackground
                )
                Button(
                    onClick = onRetry,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppColors.primary
                    )
                ) {
                    Text("Retry")
                }
            }
        }
    }

    @Composable
    fun HackathonPoster(posterUrl: String?) {
        if (!posterUrl.isNullOrEmpty()) {
            AsyncImage(
                model = posterUrl,
                contentDescription = "Hackathon Poster",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(AppColors.surface)
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(AppColors.surface),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = AppColors.onSurfaceVariant.copy(alpha = 0.5f)
                )
            }
        }
    }

    @Composable
    fun HeaderSection(hackathon: HackathonDetailsResponse) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = hackathon.title,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp
                ),
                color = AppColors.onBackground
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusChip(
                    text = hackathon.status,
                    backgroundColor = getStatusColor(hackathon.status)
                )
                UrgencyChip(urgencyLevel = hackathon.urgencyLevel)
            }

            if (hackathon.organizer != null) {
                InfoRow(
                    icon = Icons.Default.Business,
                    text = hackathon.organizer
                )
            }

            if (hackathon.location != null) {
                InfoRow(
                    icon = Icons.Default.LocationOn,
                    text = hackathon.location
                )
            }
        }
    }

    @Composable
    fun StatsSection(hackathon: HackathonDetailsResponse) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = AppColors.surface
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    icon = Icons.Default.People,
                    value = hackathon.registrationCount.toString(),
                    label = "Registered"
                )
                StatItem(
                    icon = Icons.Default.Groups,
                    value = hackathon.teamCount.toString(),
                    label = "Teams"
                )
                StatItem(
                    icon = Icons.Default.Visibility,
                    value = hackathon.viewCount.toString(),
                    label = "Views"
                )
            }
        }
    }

    @Composable
    fun DescriptionSection(description: String) {
        SectionCard(title = "About") {
            Text(
                text = description,
                style = MaterialTheme.typography.bodyLarge,
                color = AppColors.onSurfaceVariant,
                lineHeight = 24.sp
            )
        }
    }

    @Composable
    fun DetailsSection(hackathon: HackathonDetailsResponse) {
        SectionCard(title = "Details") {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                if (hackathon.prizePool != null) {
                    DetailRow(
                        label = "Prize Pool",
                        value = hackathon.prizePool,
                        icon = Icons.Default.EmojiEvents
                    )
                }

                DetailRow(
                    label = "Deadline",
                    value = hackathon.deadline,
                    icon = Icons.Default.AccessTime
                )

                if (hackathon.eventStartDate != null) {
                    DetailRow(
                        label = "Event Start",
                        value = hackathon.eventStartDate,
                        icon = Icons.Default.CalendarToday
                    )
                }

                if (hackathon.eventEndDate != null) {
                    DetailRow(
                        label = "Event End",
                        value = hackathon.eventEndDate,
                        icon = Icons.Default.Event
                    )
                }

                if (hackathon.minTeamSize != null && hackathon.maxTeamSize != null) {
                    DetailRow(
                        label = "Team Size",
                        value = "${hackathon.minTeamSize} - ${hackathon.maxTeamSize} members",
                        icon = Icons.Default.Group
                    )
                }

                if (hackathon.contactEmail != null) {
                    DetailRow(
                        label = "Contact",
                        value = hackathon.contactEmail,
                        icon = Icons.Default.Email
                    )
                }
            }
        }
    }

    @Composable
    fun TagsSection(tags: List<String>) {
        if (tags.isNotEmpty()) {
            SectionCard(title = "Tags") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    tags.take(5).forEach { tag ->
                        TagChip(tag = tag)
                    }
                }
            }
        }
    }

    @Composable
    fun ActionButtons(
        isRegistered: Boolean,
        isStarred: Boolean,
        onRegisterClick: (Boolean) -> Unit,
        onStarClick: (Boolean) -> Unit,
        onOpenLinkClick: () -> Unit
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = { onRegisterClick(!isRegistered) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isRegistered) AppColors.error else AppColors.primary
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = if (isRegistered) Icons.Default.Close else Icons.Default.CheckCircle,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isRegistered) "Unregister" else "Register Now",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { onStarClick(!isStarred) },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (isStarred) AppColors.primary.copy(alpha = 0.1f) else Color.Transparent
                    )
                ) {
                    Icon(
                        imageVector = if (isStarred) Icons.Default.Star else Icons.Default.StarBorder,
                        contentDescription = null,
                        tint = if (isStarred) AppColors.primary else AppColors.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isStarred) "Starred" else "Star",
                        color = if (isStarred) AppColors.primary else AppColors.onSurfaceVariant
                    )
                }

                OutlinedButton(
                    onClick = onOpenLinkClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.OpenInNew,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Open Link")
                }
            }
        }
    }

    @Composable
    private fun SectionCard(
        title: String,
        content: @Composable ColumnScope.() -> Unit
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = AppColors.surface
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = AppColors.onBackground
                )
                content()
            }
        }
    }

    @Composable
    private fun DetailRow(
        label: String,
        value: String,
        icon: ImageVector
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = AppColors.primary
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall,
                    color = AppColors.onSurfaceVariant,
                    fontSize = 12.sp
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyLarge,
                    color = AppColors.onBackground,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }

    @Composable
    private fun StatItem(
        icon: ImageVector,
        value: String,
        label: String
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = AppColors.primary
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = AppColors.onBackground
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = AppColors.onSurfaceVariant
            )
        }
    }

    @Composable
    private fun StatusChip(text: String, backgroundColor: Color) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = backgroundColor.copy(alpha = 0.2f)
        ) {
            Text(
                text = text.uppercase(),
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                ),
                color = backgroundColor
            )
        }
    }

    @Composable
    private fun UrgencyChip(urgencyLevel: String) {
        val color = when (urgencyLevel.uppercase()) {
            "HIGH" -> Color(0xFFEF4444)
            "MEDIUM" -> Color(0xFFF59E0B)
            "LOW" -> Color(0xFF10B981)
            else -> AppColors.onSurfaceVariant
        }

        StatusChip(text = urgencyLevel, backgroundColor = color)
    }

    @Composable
    private fun TagChip(tag: String) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = AppColors.primary.copy(alpha = 0.1f)
        ) {
            Text(
                text = "#$tag",
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                style = MaterialTheme.typography.bodySmall,
                color = AppColors.primary,
                fontWeight = FontWeight.Medium
            )
        }
    }

    @Composable
    private fun InfoRow(icon: ImageVector, text: String) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = AppColors.onSurfaceVariant
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = AppColors.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }

    @Composable
    private fun getStatusColor(status: String): Color {
        return when (status.uppercase()) {
            "ACTIVE" -> Color(0xFF10B981)
            "UPCOMING" -> Color(0xFF3B82F6)
            "ENDED" -> Color(0xFF6B7280)
            "CANCELLED" -> Color(0xFFEF4444)
            else -> AppColors.onSurfaceVariant
        }
    }
}

