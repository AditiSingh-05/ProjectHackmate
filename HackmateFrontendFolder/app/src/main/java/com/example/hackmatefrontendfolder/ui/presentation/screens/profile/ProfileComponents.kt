package com.example.hackmatefrontendfolder.ui.presentation.screens.profile

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hackmatefrontendfolder.domain.model.profile.PrivateProfileResponse
import com.example.hackmatefrontendfolder.domain.model.profile.Review
import com.example.hackmatefrontendfolder.ui.theme.AppColors
import com.example.hackmatefrontendfolder.ui.theme.NeonCyan
import com.example.hackmatefrontendfolder.ui.theme.NeonYellow
import com.example.hackmatefrontendfolder.ui.theme.PrimaryBlack
import kotlin.collections.take

object ProfileComponents {

    @Composable
    fun LoadingState() {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    color = AppColors.primary,
                    strokeWidth = 3.dp,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Loading profile...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppColors.onSurfaceVariant
                )
            }
        }
    }


    @Composable
    fun ErrorState(message: String, onRetry: () -> Unit) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Error,
                    contentDescription = null,
                    tint = AppColors.error,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyLarge,
                    color = AppColors.onSurface,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
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
    fun ProfileBackground(content: @Composable BoxScope.() -> Unit) {
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


    @Composable
    fun ProfileHeader(
        profile: PrivateProfileResponse,
        onEditProfile: () -> Unit
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = AppColors.surfaceCard.copy(alpha = 0.6f)
            ),
            border = BorderStroke(1.dp, AppColors.primary.copy(alpha = 0.3f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier.size(120.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawCircle(
                            brush = Brush.linearGradient(
                                colors = listOf(NeonCyan, NeonYellow)
                            ),
                            radius = size.minDimension / 2,
                            alpha = 0.3f
                        )
                    }

                    Surface(
                        modifier = Modifier.size(110.dp),
                        shape = CircleShape,
                        color = AppColors.primaryContainer,
                        border = BorderStroke(3.dp, AppColors.primary)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                text = profile.avatarId ?: profile.fullName.firstOrNull()?.toString() ?: "U",
                                style = MaterialTheme.typography.displayMedium,
                                color = AppColors.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    IconButton(
                        onClick = onEditProfile,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(36.dp)
                            .background(AppColors.primary, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Profile",
                            tint = AppColors.onPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = profile.fullName,
                    style = MaterialTheme.typography.headlineMedium,
                    color = AppColors.onSurface,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = profile.email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppColors.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = null,
                        tint = AppColors.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${profile.college} • ${profile.year}",
                        style = MaterialTheme.typography.bodySmall,
                        color = AppColors.onSurfaceVariant
                    )
                }
            }
        }
    }

    @Composable
    fun StatsSection(profile: PrivateProfileResponse) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                icon = Icons.Default.EmojiEvents,
                value = profile.hackathonsWon.toString(),
                label = "Won",
                modifier = Modifier.weight(1f),
                color = NeonYellow
            )

            StatCard(
                icon = Icons.Default.Flag,
                value = profile.hackathonsParticipated.toString(),
                label = "Participated",
                modifier = Modifier.weight(1f),
                color = NeonCyan
            )

            StatCard(
                icon = Icons.Default.Star,
                value = String.format("%.1f", profile.averageRating),
                label = "Rating",
                modifier = Modifier.weight(1f),
                color = NeonYellow
            )
        }
    }

    @Composable
    fun StatCard(
        icon: ImageVector,
        value: String,
        label: String,
        modifier: Modifier = Modifier,
        color: Color
    ) {
        Card(
            modifier = modifier,
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = AppColors.surfaceCard.copy(alpha = 0.5f)
            ),
            border = BorderStroke(1.dp, color.copy(alpha = 0.3f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(28.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineSmall,
                    color = AppColors.onSurface,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall,
                    color = AppColors.onSurfaceVariant
                )
            }
        }
    }

    @Composable
    fun AboutSection(profile: PrivateProfileResponse) {
        if (!profile.bio.isNullOrBlank()) {
            SectionCard(title = "About") {
                Text(
                    text = profile.bio,
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppColors.onSurface,
                    lineHeight = 22.sp
                )
            }
        }
    }

    @Composable
    fun SkillsSection(skills: List<String>, mainSkill: String?) {
        if (skills.isNotEmpty()) {
            SectionCard(title = "Skills") {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    mainSkill?.let {
                        SkillChip(skill = it, isMainSkill = true)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        skills.filter { it != mainSkill }.forEach { skill ->
                            SkillChip(skill = skill, isMainSkill = false)
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun SkillChip(skill: String, isMainSkill: Boolean) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = if (isMainSkill) AppColors.primary.copy(alpha = 0.2f) else AppColors.surfaceVariant,
            border = if (isMainSkill) BorderStroke(1.dp, AppColors.primary) else null
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isMainSkill) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = AppColors.primary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }
                Text(
                    text = skill,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isMainSkill) AppColors.primary else AppColors.onSurfaceVariant,
                    fontWeight = if (isMainSkill) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }

    @Composable
    fun SocialLinksSection(profile: PrivateProfileResponse) {
        val links = listOfNotNull(
            profile.githubProfile?.let { SocialLink(Icons.Default.Code, "GitHub", it) },
            profile.linkedinProfile?.let { SocialLink(Icons.Default.Business, "LinkedIn", it) },
            profile.portfolioUrl?.let { SocialLink(Icons.Default.Language, "Portfolio", it) }
        )

        if (links.isNotEmpty()) {
            SectionCard(title = "Social Links") {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    links.forEach { link ->
                        SocialLinkItem(link = link)
                    }
                }
            }
        }
    }

    @Composable
    fun SocialLinkItem(link: SocialLink) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { }
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = link.icon,
                contentDescription = null,
                tint = AppColors.primary,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = link.label,
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppColors.onSurface,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = link.url,
                    style = MaterialTheme.typography.bodySmall,
                    color = AppColors.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                tint = AppColors.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }
    }

    @Composable
    fun ReviewsSection(
        reviews: List<Review>,
        averageRating: Double,
        totalReviews: Int
    ) {
        SectionCard(title = "Reviews ($totalReviews)") {
            if (reviews.isEmpty()) {
                Text(
                    text = "No reviews yet",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppColors.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    reviews.take(3).forEach { review ->
                        ReviewItem(review = review)
                    }

                    if (reviews.size > 3) {
                        TextButton(
                            onClick = { },
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text("View All Reviews")
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun ReviewItem(review: Review) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = AppColors.surfaceVariant.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        modifier = Modifier.size(32.dp),
                        shape = CircleShape,
                        color = AppColors.primary.copy(alpha = 0.2f)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = review.reviewerAvatarId ?: "U",
                                style = MaterialTheme.typography.bodySmall,
                                color = AppColors.primary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = review.reviewerName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = AppColors.onSurface,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = NeonYellow,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = review.rating.toString(),
                        style = MaterialTheme.typography.bodySmall,
                        color = AppColors.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            review.comment?.let { comment ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = comment,
                    style = MaterialTheme.typography.bodySmall,
                    color = AppColors.onSurfaceVariant,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }

            review.hackathonName?.let { hackathonName ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "• $hackathonName",
                    style = MaterialTheme.typography.bodySmall,
                    color = AppColors.primary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }

    @Composable
    fun SectionCard(
        title: String,
        content: @Composable ColumnScope.() -> Unit
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = AppColors.surfaceCard.copy(alpha = 0.5f)
            ),
            border = BorderStroke(1.dp, AppColors.primary.copy(alpha = 0.2f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = AppColors.primary,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                content()
            }
        }
    }

    @Composable
    fun LogoutButton(onLogout: () -> Unit) {
        OutlinedButton(
            onClick = onLogout,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, AppColors.error),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = AppColors.error
            )
        ) {
            Icon(
                imageVector = Icons.Default.Logout,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Logout",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ProfileTopBar(onNotificationClick: () -> Unit) {
        TopAppBar(
            title = {
                Text(
                    text = "PROFILE",
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
    }




    data class SocialLink(
        val icon: ImageVector,
        val label: String,
        val url: String
    )


}

