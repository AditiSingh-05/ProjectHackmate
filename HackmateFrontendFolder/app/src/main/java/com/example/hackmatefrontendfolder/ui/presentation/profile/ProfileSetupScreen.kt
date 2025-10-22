package com.example.hackmatefrontendfolder.ui.presentation.profile

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.hackmatefrontendfolder.data.model.profile.ProfileSetupRequest
import com.example.hackmatefrontendfolder.ui.theme.AppColors
import kotlinx.coroutines.launch

@Composable
fun ProfileSetupScreen(
    viewModel: ProfileSetupViewModel = hiltViewModel(),
    onProfileComplete: () -> Unit = {}
) {
    var fullName by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var hackathonsParticipated by remember { mutableStateOf("") }
    var hackathonsWon by remember { mutableStateOf("") }
    var college by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var githubProfile by remember { mutableStateOf("") }
    var linkedinProfile by remember { mutableStateOf("") }
    var portfolioUrl by remember { mutableStateOf("") }
    var avatarId by remember { mutableStateOf("") }
    var mainSkill by remember { mutableStateOf("") }
    var selectedSkills by remember { mutableStateOf<List<String>>(emptyList()) }

    var fullNameError by remember { mutableStateOf<String?>(null) }
    var collegeError by remember { mutableStateOf<String?>(null) }
    var yearError by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()

    val setupState by viewModel.setupState.collectAsState()

    var completionPercentage by remember { mutableStateOf(0) }

    LaunchedEffect(fullName, bio, college, year, githubProfile, linkedinProfile, portfolioUrl, avatarId, mainSkill, selectedSkills) {
        var completed = 0
        val totalFields = 10

        if (fullName.isNotBlank()) completed++
        if (bio.isNotBlank()) completed++
        if (college.isNotBlank()) completed++
        if (year.isNotBlank()) completed++
        if (githubProfile.isNotBlank()) completed++
        if (linkedinProfile.isNotBlank()) completed++
        if (portfolioUrl.isNotBlank()) completed++
        if (avatarId.isNotBlank()) completed++
        if (mainSkill.isNotBlank()) completed++
        if (selectedSkills.isNotEmpty()) completed++

        completionPercentage = (completed * 100) / totalFields
    }

    LaunchedEffect(setupState) {
        when (val state = setupState) {
            is ProfileSetupViewModel.Resource.Success -> {
                state.data?.let { response ->
                    if (response.success) {
                        kotlinx.coroutines.delay(2000)
                        onProfileComplete()
                    } else {
                        errorMessage = response.message
                    }
                }
            }
            is ProfileSetupViewModel.Resource.Error -> {
                errorMessage = state.message ?: "Profile setup failed. Please try again."
            }
            else -> {}
        }
    }

    if (errorMessage != null) {
        LaunchedEffect(errorMessage) {
            kotlinx.coroutines.delay(4000)
            errorMessage = null
        }
    }

    ProfileSetupComponents.Background {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "COMPLETE YOUR\nPROFILE",
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 3.sp,
                    lineHeight = 36.sp
                ),
                color = AppColors.primary,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Set up your hackathon profile to connect with teams",
                style = MaterialTheme.typography.bodyLarge,
                color = AppColors.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            ProfileSetupComponents.CompletionIndicator(
                completionPercentage = completionPercentage
            )

            AnimatedVisibility(
                visible = errorMessage != null,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = AppColors.error.copy(alpha = 0.1f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = errorMessage ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = AppColors.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            ProfileSetupComponents.TextField(
                value = fullName,
                onValueChange = {
                    fullName = it
                    fullNameError = null
                    errorMessage = null
                },
                label = "FULL NAME",
                icon = Icons.Default.Person,
                placeholder = "Enter your full name",
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                isError = fullNameError != null,
                errorMessage = fullNameError
            )

            ProfileSetupComponents.TextField(
                value = bio,
                onValueChange = { bio = it },
                label = "BIO",
                icon = Icons.Default.Description,
                placeholder = "Tell us about yourself and your interests",
                maxLines = 3,
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ProfileSetupComponents.TextField(
                    value = hackathonsParticipated,
                    onValueChange = { hackathonsParticipated = it },
                    label = "HACKATHONS",
                    icon = Icons.Default.EmojiEvents,
                    placeholder = "0",
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.weight(1f)
                )

                ProfileSetupComponents.TextField(
                    value = hackathonsWon,
                    onValueChange = { hackathonsWon = it },
                    label = "WINS",
                    icon = Icons.Default.Star,
                    placeholder = "0",
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.weight(1f)
                )
            }

            ProfileSetupComponents.TextField(
                value = college,
                onValueChange = {
                    college = it
                    collegeError = null
                    errorMessage = null
                },
                label = "COLLEGE/UNIVERSITY",
                icon = Icons.Default.School,
                placeholder = "Enter your institution name",
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                isError = collegeError != null,
                errorMessage = collegeError
            )

            ProfileSetupComponents.YearSelector(
                selectedYear = year,
                onYearSelected = {
                    year = it
                    yearError = null
                    errorMessage = null
                }
            )

            ProfileSetupComponents.TextField(
                value = githubProfile,
                onValueChange = { githubProfile = it },
                label = "GITHUB PROFILE",
                icon = Icons.Default.Code,
                placeholder = "https://github.com/username",
                keyboardType = KeyboardType.Uri
            )

            ProfileSetupComponents.TextField(
                value = linkedinProfile,
                onValueChange = { linkedinProfile = it },
                label = "LINKEDIN PROFILE",
                icon = Icons.Default.Business,
                placeholder = "https://linkedin.com/in/username",
                keyboardType = KeyboardType.Uri
            )

            ProfileSetupComponents.TextField(
                value = portfolioUrl,
                onValueChange = { portfolioUrl = it },
                label = "PORTFOLIO URL",
                icon = Icons.Default.Language,
                placeholder = "https://yourportfolio.com",
                keyboardType = KeyboardType.Uri
            )

            ProfileSetupComponents.AvatarSelector(
                selectedAvatarId = avatarId,
                onAvatarSelected = { avatarId = it }
            )

            ProfileSetupComponents.TextField(
                value = mainSkill,
                onValueChange = { mainSkill = it },
                label = "PRIMARY SKILL",
                icon = Icons.Default.Psychology,
                placeholder = "e.g., Frontend Development, Data Science"
            )

            ProfileSetupComponents.SkillSelector(
                selectedSkills = selectedSkills,
                onSkillsChanged = { selectedSkills = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProfileSetupComponents.Button(
                text = "Complete Profile Setup",
                onClick = {
                    var hasError = false

                    if (fullName.isBlank()) {
                        fullNameError = "Full name is required"
                        hasError = true
                    }

                    if (college.isBlank()) {
                        collegeError = "College/University is required"
                        hasError = true
                    }

                    if (year.isBlank()) {
                        yearError = "Please select your academic year"
                        hasError = true
                    }

                    if (!hasError) {
                        val request = ProfileSetupRequest(
                            fullName = fullName,
                            bio = bio.takeIf { it.isNotBlank() },
                            hackathonsParticipated = hackathonsParticipated.toIntOrNull() ?: 0,
                            hackathonsWon = hackathonsWon.toIntOrNull() ?: 0,
                            college = college,
                            year = year,
                            githubProfile = githubProfile.takeIf { it.isNotBlank() },
                            linkedinProfile = linkedinProfile.takeIf { it.isNotBlank() },
                            portfolioUrl = portfolioUrl.takeIf { it.isNotBlank() },
                            avatarId = avatarId.takeIf { it.isNotBlank() },
                            mainSkill = mainSkill.takeIf { it.isNotBlank() },
                            skills = selectedSkills
                        )

                        coroutineScope.launch {
                            viewModel.setupProfile(request)
                        }
                    }
                },
                isLoading = setupState is ProfileSetupViewModel.Resource.Loading,
                isSuccess = setupState is ProfileSetupViewModel.Resource.Success,
                isError = setupState is ProfileSetupViewModel.Resource.Error
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}