package com.example.hackmatefrontendfolder.ui.presentation.screens.createhackathon

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.hackmatefrontendfolder.ui.presentation.viewmodels.CreateHackathonViewModel
import com.example.hackmatefrontendfolder.ui.theme.AppColors

@Composable
fun CreateHackathonScreen(
    viewModel: CreateHackathonViewModel = hiltViewModel(),
    onHackathonCreated: () -> Unit = {}
) {
    var title by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var registrationLink by rememberSaveable { mutableStateOf("") }
    var deadline by rememberSaveable { mutableStateOf("") }
    var posterUrl by rememberSaveable { mutableStateOf("") }
    var selectedTags by rememberSaveable { mutableStateOf(listOf<String>()) }
    var organizer by rememberSaveable { mutableStateOf("") }
    var location by rememberSaveable { mutableStateOf("") }
    var prizePool by rememberSaveable { mutableStateOf("") }
    var eventStartDate by rememberSaveable { mutableStateOf("") }
    var eventEndDate by rememberSaveable { mutableStateOf("") }
    var maxTeamSize by rememberSaveable { mutableIntStateOf(1) }
    var minTeamSize by rememberSaveable { mutableIntStateOf(1) }
    var contactEmail by rememberSaveable { mutableStateOf("") }
    var isLoading by rememberSaveable { mutableStateOf(false) }




    CreateHackathonComponents.Background {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
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
                textAlign = TextAlign.Center
            )

            CreateHackathonComponents.TextField(
                value = title,
                onValueChange = {

                },
                label = "HACKATHON TITLE",
                icon = Icons.Default.Event,
                placeholder = "Enter hackathon title"
            )

            CreateHackathonComponents.TextField(
                value = description,
                onValueChange = {
                    description = it
                },
                label = "DESCRIPTION",
                icon = Icons.Default.Description,
                placeholder = "Describe the hackathon",
                maxLines = 4,
                singleLine = false
            )

            CreateHackathonComponents.TextField(
                value = registrationLink,
                onValueChange = {

                },
                label = "REGISTRATION LINK",
                icon = Icons.Default.Link,
                placeholder = "https://your-hackathon-registration.com"
            )

            CreateHackathonComponents.DateTimeSelector(
                selectedDateTime = deadline,
                onDateTimeSelected = {
                    deadline = it
                },
                label = "REGISTRATION DEADLINE",
                icon = Icons.Default.Schedule
            )

            CreateHackathonComponents.TextField(
                value = posterUrl,
                onValueChange = {
                    posterUrl = it
                },
                label = "POSTER URL (Optional)",
                icon = Icons.Default.Image,
                placeholder = "https://image.jpg"
            )

            CreateHackathonComponents.TagSelector(
                selectedTags = selectedTags,
                onTagsChanged = {
                    selectedTags = it
                }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CreateHackathonComponents.TextField(
                    value = organizer,
                    onValueChange = {
                        organizer = it
                    },
                    label = "ORGANIZER",
                    icon = Icons.Default.Business,
                    placeholder = "Organization name",
                    modifier = Modifier.weight(1f)
                )

                CreateHackathonComponents.TextField(
                    value = location,
                    onValueChange = {
                        location = it
                    },
                    label = "LOCATION",
                    icon = Icons.Default.LocationOn,
                    placeholder = "City, Country",
                    modifier = Modifier.weight(1f)
                )
            }

            CreateHackathonComponents.TextField(
                value = prizePool,
                onValueChange = {
                    prizePool = it
                },
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
                    onDateTimeSelected = {
                        eventStartDate = it
                    },
                    label = "EVENT START",
                    icon = Icons.Default.PlayArrow,
                    modifier = Modifier.weight(1f)
                )

                CreateHackathonComponents.DateTimeSelector(
                    selectedDateTime = eventEndDate,
                    onDateTimeSelected = {
                        eventEndDate = it
                    },
                    label = "EVENT END",
                    icon = Icons.Default.Stop,
                    modifier = Modifier.weight(1f)
                )
            }

            CreateHackathonComponents.TextField(
                value = contactEmail,
                onValueChange = {
                    contactEmail = it
                },
                label = "CONTACT EMAIL (Optional)",
                icon = Icons.Default.Email,
                placeholder = "contact@hackathon.com"
            )

            CreateHackathonComponents.Button(
                text = "Create Hackathon",
                onClick = { viewModel.createHackathon(
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
                    contactEmail = contactEmail,
                    minTeamSize = minTeamSize.toString(),
                    maxTeamSize = maxTeamSize.toString()
                ) },
                isLoading = isLoading,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
