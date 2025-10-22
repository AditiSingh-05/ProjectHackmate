package com.example.hackmatefrontendfolder.ui.presentation.createhackathon

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.hackmatefrontendfolder.ui.theme.AppColors
import kotlinx.coroutines.launch

@Composable
fun CreateHackathonScreen(
    viewModel: CreateHackathonViewModel = hiltViewModel(),
    onHackathonCreated: () -> Unit = {}
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var registrationLink by remember { mutableStateOf("") }
    var deadline by remember { mutableStateOf("") }
    var posterUrl by remember { mutableStateOf("") }
    var selectedTags by remember { mutableStateOf<List<String>>(emptyList()) }
    var organizer by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var prizePool by remember { mutableStateOf("") }
    var eventStartDate by remember { mutableStateOf("") }
    var eventEndDate by remember { mutableStateOf("") }
    var maxTeamSize by remember { mutableStateOf("") }
    var minTeamSize by remember { mutableStateOf("") }
    var originalMessage by remember { mutableStateOf("") }
    var contactEmail by remember { mutableStateOf("") }

    var titleError by remember { mutableStateOf<String?>(null) }
    var descriptionError by remember { mutableStateOf<String?>(null) }
    var registrationLinkError by remember { mutableStateOf<String?>(null) }
    var deadlineError by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()

    val isLoading by viewModel.isLoading.collectAsState()
    val createHackathonResponse by viewModel.createHackathonResponse.collectAsState()
    val apiErrorMessage by viewModel.errorMessage.collectAsState()

    var isExtracting by remember { mutableStateOf(false) }

    LaunchedEffect(createHackathonResponse) {
        createHackathonResponse?.let { response ->
            if (response.isSuccessful && response.body()?.success == true) {
                kotlinx.coroutines.delay(2000)
                onHackathonCreated()
            }
        }
    }

    LaunchedEffect(apiErrorMessage) {
        if (apiErrorMessage.isNotEmpty()) {
            errorMessage = apiErrorMessage
        }
    }

    if (errorMessage != null) {
        LaunchedEffect(errorMessage) {
            kotlinx.coroutines.delay(4000)
            errorMessage = null
        }
    }

    CreateHackathonComponents.Background {
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
                text = "CREATE\nHACKATHON",
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 3.sp,
                    lineHeight = 36.sp
                ),
                color = AppColors.primary,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Share an amazing hackathon opportunity with the community",
                style = MaterialTheme.typography.bodyLarge,
                color = AppColors.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
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

            CreateHackathonComponents.AIExtractionCard(
                originalMessage = originalMessage,
                onMessageChange = { originalMessage = it },
                onExtract = {
                    coroutineScope.launch {
                        isExtracting = true
                        kotlinx.coroutines.delay(2000) // Simulate AI processing

                        // Mock AI extraction - replace with actual AI service
                        if (originalMessage.contains("hackathon", ignoreCase = true)) {
                            if (title.isEmpty()) title = "AI Hackathon 2025"
                            if (description.isEmpty()) description = "Join us for an exciting AI-focused hackathon where innovation meets creativity!"
                            if (organizer.isEmpty()) organizer = "Tech Community"
                            if (prizePool.isEmpty()) prizePool = "$10,000"
                            if (maxTeamSize.isEmpty()) maxTeamSize = "5"
                            if (minTeamSize.isEmpty()) minTeamSize = "2"
                            if (deadline.isEmpty()) deadline = "2025-01-15 23:59:00"
                        }

                        isExtracting = false
                    }
                },
                isExtracting = isExtracting
            )

            CreateHackathonComponents.TextField(
                value = title,
                onValueChange = {
                    title = it
                    titleError = null
                    errorMessage = null
                },
                label = "HACKATHON TITLE",
                icon = Icons.Default.Event,
                placeholder = "Enter hackathon title",
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                isError = titleError != null,
                errorMessage = titleError
            )

            CreateHackathonComponents.TextField(
                value = description,
                onValueChange = {
                    description = it
                    descriptionError = null
                    errorMessage = null
                },
                label = "DESCRIPTION",
                icon = Icons.Default.Description,
                placeholder = "Describe the hackathon theme, goals, and expectations",
                maxLines = 4,
                singleLine = false,
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                isError = descriptionError != null,
                errorMessage = descriptionError
            )

            CreateHackathonComponents.TextField(
                value = registrationLink,
                onValueChange = {
                    registrationLink = it
                    registrationLinkError = null
                    errorMessage = null
                },
                label = "REGISTRATION LINK",
                icon = Icons.Default.Link,
                placeholder = "https://your-hackathon-registration.com",
                keyboardType = KeyboardType.Uri,
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                isError = registrationLinkError != null,
                errorMessage = registrationLinkError
            )

            CreateHackathonComponents.DateTimeSelector(
                selectedDateTime = deadline,
                onDateTimeSelected = {
                    deadline = it
                    deadlineError = null
                    errorMessage = null
                },
                label = "REGISTRATION DEADLINE",
                icon = Icons.Default.Schedule
            )

            CreateHackathonComponents.TextField(
                value = posterUrl,
                onValueChange = { posterUrl = it },
                label = "POSTER URL (Optional)",
                icon = Icons.Default.Image,
                placeholder = "https://your-poster-image.com/image.jpg",
                keyboardType = KeyboardType.Uri
            )

            CreateHackathonComponents.TagSelector(
                selectedTags = selectedTags,
                onTagsChanged = { selectedTags = it }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CreateHackathonComponents.TextField(
                    value = organizer,
                    onValueChange = { organizer = it },
                    label = "ORGANIZER",
                    icon = Icons.Default.Business,
                    placeholder = "Organization name",
                    modifier = Modifier.weight(1f)
                )

                CreateHackathonComponents.TextField(
                    value = location,
                    onValueChange = { location = it },
                    label = "LOCATION",
                    icon = Icons.Default.LocationOn,
                    placeholder = "City, Country",
                    modifier = Modifier.weight(1f)
                )
            }

            CreateHackathonComponents.TextField(
                value = prizePool,
                onValueChange = { prizePool = it },
                label = "PRIZE POOL (Optional)",
                icon = Icons.Default.EmojiEvents,
                placeholder = "$10,000 in prizes"
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CreateHackathonComponents.DateTimeSelector(
                    selectedDateTime = eventStartDate,
                    onDateTimeSelected = { eventStartDate = it },
                    label = "EVENT START",
                    icon = Icons.Default.PlayArrow,
                    modifier = Modifier.weight(1f)
                )

                CreateHackathonComponents.DateTimeSelector(
                    selectedDateTime = eventEndDate,
                    onDateTimeSelected = { eventEndDate = it },
                    label = "EVENT END",
                    icon = Icons.Default.Stop,
                    modifier = Modifier.weight(1f)
                )
            }

            CreateHackathonComponents.TeamSizeSelector(
                minTeamSize = minTeamSize,
                maxTeamSize = maxTeamSize,
                onMinSizeChange = { minTeamSize = it },
                onMaxSizeChange = { maxTeamSize = it }
            )

            CreateHackathonComponents.TextField(
                value = contactEmail,
                onValueChange = { contactEmail = it },
                label = "CONTACT EMAIL (Optional)",
                icon = Icons.Default.Email,
                placeholder = "contact@hackathon.com",
                keyboardType = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(16.dp))

            CreateHackathonComponents.Button(
                text = "Create Hackathon",
                onClick = {
                    var hasError = false

                    if (title.isBlank()) {
                        titleError = "Title is required"
                        hasError = true
                    }

                    if (description.isBlank()) {
                        descriptionError = "Description is required"
                        hasError = true
                    }

                    if (registrationLink.isBlank()) {
                        registrationLinkError = "Registration link is required"
                        hasError = true
                    } else if (!registrationLink.startsWith("http")) {
                        registrationLinkError = "Please enter a valid URL"
                        hasError = true
                    }

                    if (deadline.isBlank()) {
                        deadlineError = "Deadline is required"
                        hasError = true
                    }

                    if (!hasError) {
                        coroutineScope.launch {
                            viewModel.createHackathon(
                                title = title,
                                description = description,
                                registrationLink = registrationLink,
                                deadline = deadline,
                                posterUrl = posterUrl,
                                tags = selectedTags,
                                organizer = organizer,
                                location = location,
                                prizePool = prizePool,
                                eventStartDate = eventStartDate,
                                eventEndDate = eventEndDate,
                                maxTeamSize = maxTeamSize,
                                minTeamSize = minTeamSize,
                                originalMessage = originalMessage,
                                contactEmail = contactEmail
                            )
                        }
                    }
                },
                isLoading = isLoading,
                isSuccess = createHackathonResponse?.isSuccessful == true && createHackathonResponse?.body()?.success == true,
                isError = createHackathonResponse?.isSuccessful == false || createHackathonResponse?.body()?.success == false,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview
@Composable
fun CreateHackathonScreenPreview() {
    CreateHackathonScreen()
}

