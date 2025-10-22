# Frontend Screen to API & Data Classes Mapping

> **Complete Guide for Frontend Development**  
> This document maps every frontend screen to the backend APIs they need to call and the data classes (DTOs) they should use.

---


## 🏠 Home/Feed Screen

**Primary API:**
- `GET /api/hackathons/feed`

**Query Parameters:**
```
search: String (optional)
showExpired: boolean (default: false)
sortBy: String (default: "deadline") - options: deadline, createdAt, viewCount
sortDirection: String (default: "asc") - options: asc, desc
page: int (default: 0)
size: int (default: 20)
```

**Response Data Class:**

HackathonListResponseDto {
    hackathons: List<HackathonListItemDto>
    currentPage: int
    totalPages: int
    totalElements: long
    hasNext: boolean
    hasPrevious: boolean
}

HackathonListItemDto {
    hackathonId: Long
    title: String
    description: String
    posterUrl: String
    tags: List<String>
    organizer: String
    location: String
    deadline: LocalDateTime
    prizePool: String
    viewCount: long
    registrationCount: long
    teamCount: long
    isRegistered: boolean
    isStarred: boolean
    urgencyLevel: String (LOW/MEDIUM/HIGH/URGENT)
    postedAt: LocalDateTime
}
```

**Features to Implement:**
1. **Infinite Scrolling/Pagination**: Use `page` parameter to load more
2. **Search Bar**: Pass search query to API
3. **Filter Options**: 
   - Show/Hide expired hackathons
   - Sort by deadline, popularity, etc.
4. **Pull to Refresh**: Reload feed with page=0
5. **Card Actions**:
   - Tap card → Navigate to Hackathon Details
   - Star button → Call `/api/hackathons/star`
   - Register button → Call `/api/hackathons/register`

**Additional APIs for Actions:**
- `POST /api/hackathons/star` - Toggle star/favorite
- `POST /api/hackathons/register` - Toggle registration

---

## 📄 Hackathon Details Screen

**Primary API:**
- `GET /api/hackathons/{id}`

**Response Data Class:**
HackathonDetailsResponseDto {
    hackathonId: Long
    title: String
    description: String
    registrationLink: String
    posterUrl: String
    tags: List<String>
    organizer: String
    location: String
    prizePool: String
    deadline: LocalDateTime
    eventStartDate: LocalDateTime
    eventEndDate: LocalDateTime
    maxTeamSize: Integer
    minTeamSize: Integer
    contactEmail: String
    viewCount: long
    registrationCount: long
    teamCount: long
    status: String
    isRegistered: boolean
    isStarred: boolean
    isExpired: boolean
    urgencyLevel: String
    postedBy: String
    postedAt: LocalDateTime
    approvedAt: LocalDateTime
}

**Secondary APIs:**
- `POST /api/hackathons/register` - Register/Unregister for hackathon
- `POST /api/hackathons/star` - Star/Unstar hackathon
- `POST /api/teams/search` - Find teams for this hackathon

**UI Sections:**
1. **Header**: Title, poster, organizer
2. **Details Section**: Description, dates, location, prize pool
3. **Stats**: View count, registration count, team count
4. **Actions**:
   - Register/Unregister button
   - Star/Favorite button
   - Open registration link button
   - Share button
5. **Teams Section**: 
   - "Browse Teams" button → Navigate to Teams List Screen
   - "Create Team" button → Navigate to Create Team Screen
6. **Tags**: Clickable tags for filtering

---

## 👤 Profile Screens

### 1. **Profile Setup Screen** (First Time Only)
**API Required:**
- `POST /api/profiles/setup`

**Request Data Class:**

ProfileSetupRequestDto {
    fullName: String (required, 2-100 chars)
    bio: String (optional)
    hackathonsParticipated: int (default: 0)
    hackathonsWon: int (default: 0)
    college: String (required)
    year: String (required) - e.g., "1st Year", "2nd Year"
    githubProfile: String (optional)
    linkedinProfile: String (optional)
    portfolioUrl: String (optional)
    avatarId: String (optional)
    mainSkill: String (optional)
}
```

**Response Data Class:**

ProfileSetupResponseDto {
    success: boolean
    message: String
    userId: Long
    fullName: String
    profileCompletionPercentage: int
}
```

**Flow:**
1. Show form with required and optional fields
2. Submit to setup API
3. On success, navigate to Home Screen

---

### 2. **My Profile Screen** (Own Profile)
**Primary API:**
- `GET /api/profiles/me`

**Response Data Class:**

PrivateProfileResponseDto {
    userId: Long
    email: String (only visible on own profile)
    fullName: String
    bio: String
    hackathonsParticipated: int
    hackathonsWon: int
    college: String
    year: String
    githubProfile: String
    linkedinProfile: String
    portfolioUrl: String
    avatarId: String
    mainSkill: String
    averageRating: double
    totalReviews: int
    recentReviews: List<ReviewDto>
}

ReviewDto {
    reviewId: Long
    reviewerName: String
    reviewerAvatarId: String
    rating: int (1-5)
    comment: String
    hackathonName: String
    reviewedAt: LocalDateTime
}
```

**Secondary APIs:**
- `PUT /api/profiles` - Update profile
- `GET /api/teams/my-teams` - Get user's teams
- `GET /api/hackathons/my-registered` - Get registered hackathons
- `GET /api/hackathons/my-starred` - Get starred hackathons

**UI Sections:**
1. **Header**: Avatar, name, rating
2. **Stats Cards**: Hackathons participated, won
3. **About**: Bio, college, year, skills
4. **Social Links**: GitHub, LinkedIn, Portfolio
5. **Reviews Section**: Display reviews from teammates
6. **My Teams Tab**: List of teams user is part of
7. **My Hackathons Tab**: Registered and starred hackathons
8. **Settings Button**: Navigate to Settings Screen

---

### 3. **Public Profile Screen** (Other User's Profile)
**API Required:**
- `GET /api/profiles/{userId}`

**Response Data Class:**

PublicProfileResponseDto {
    userId: Long
    fullName: String
    bio: String
    hackathonsParticipated: int
    hackathonsWon: int
    college: String
    year: String
    githubProfile: String
    linkedinProfile: String
    portfolioUrl: String
    avatarId: String
    mainSkill: String
    averageRating: double
    totalReviews: int
    publicReviews: List<ReviewDto>
    badges: List<String>
    joinedAt: LocalDateTime
}
```

**Features:**
- View-only (no edit button)
- Can see public reviews
- Can see badges/achievements
- Contact options (if team member)

---

### 4. **Edit Profile Screen**
**API Required:**
- `PUT /api/profiles`

**Request Data Class:**

ProfileUpdateRequestDto {
    fullName: String
    bio: String
    college: String
    year: String
    githubProfile: String
    linkedinProfile: String
    portfolioUrl: String
    mainSkill: String
}
```

**Response Data Class:**

ProfileUpdateResponseDto {
    success: boolean
    message: String
    updatedAt: LocalDateTime
}
```

---

## 👥 Team Screens

### 1. **Teams List Screen** (For a Hackathon)
**Primary API:**
- `POST /api/teams/search`

**Request Data Class:**

TeamSearchRequestDto {
    hackathonId: Long (required)
    userSkills: List<String> (for matching)
    search: String (optional)
    status: String (default: "OPEN") - options: OPEN, FULL, CLOSED, ALL
    publicOnly: boolean (default: true)
    sortBy: String (default: "matchPercentage") - options: matchPercentage, createdAt, availableSlots
    sortDirection: String (default: "desc")
    page: int (default: 0)
    size: int (default: 20)
}
```

**Response Data Class:**

TeamSearchResponseDto {
    teams: List<TeamListItemDto>
    currentPage: int
    totalPages: int
    totalElements: long
    hasNext: boolean
    hasPrevious: boolean
}

TeamListItemDto {
    teamId: Long
    teamName: String
    description: String
    maxSize: int
    currentSize: int
    availableSlots: int
    leaderName: String
    leaderAvatarId: String
    rolesNeeded: List<String>
    skillsNeeded: List<String>
    status: String (OPEN/FULL/CLOSED)
    isPublic: boolean
    autoAccept: boolean
    matchingSkills: List<String>
    matchPercentage: int
    createdAt: LocalDateTime
}
```

**Features:**
1. **Smart Matching**: Teams are sorted by match percentage with user's skills
2. **Filters**: By status (open/full/closed), search
3. **Match Indicator**: Show match percentage badge
4. **Team Cards**: Click to view Team Details

---

### 2. **Team Details Screen**
**Primary API:**
- `GET /api/teams/{id}`

**Response Data Class:**

TeamDetailsResponseDto {
    teamId: Long
    teamName: String
    description: String
    maxSize: int
    currentSize: int
    availableSlots: int
    status: String
    isPublic: boolean
    autoAccept: boolean
    rolesNeeded: List<String>
    skillsNeeded: List<String>
    leader: TeamMemberDto
    members: List<TeamMemberDto>
    hackathon: HackathonSummaryDto
    isUserMember: boolean
    isUserLeader: boolean
    hasPendingRequest: boolean
    canJoin: boolean
    createdAt: LocalDateTime
    updatedAt: LocalDateTime
}

TeamMemberDto {
    userId: Long
    fullName: String
    avatarId: String
    role: String (LEADER/MEMBER)
    assignedRole: String (Frontend/Backend/etc.)
    skills: List<String>
    college: String
    year: String
    email: String
    phone: String
    discordUsername: String
    githubProfile: String
    joinedAt: LocalDateTime
    active: boolean
}

HackathonSummaryDto {
    hackathonId: Long
    title: String
    organizer: String
    deadline: LocalDateTime
    urgencyLevel: String
}
```

**Secondary APIs:**
- `POST /api/join-requests` - Send join request
- `PUT /api/teams` - Update team (leader only)
- `DELETE /api/teams/{teamId}/members/{userId}` - Remove member (leader only)
- `POST /api/teams/leave` - Leave team
- `POST /api/teams/export` - Export team data

**UI Sections:**
1. **Header**: Team name, hackathon info
2. **Team Info**: Description, roles needed, skills needed
3. **Members Section**: 
   - Leader card (highlighted)
   - Member cards with contact info
   - Empty slots indication
4. **Actions** (Conditional):
   - **Not a member**: "Request to Join" button
   - **Pending request**: "Request Pending" (disabled)
   - **Member**: "Leave Team" button
   - **Leader**: "Edit Team", "Manage Requests", "Remove Member" options
5. **Contact Section**: Email, Discord, WhatsApp links

---

### 3. **Create Team Screen**
**API Required:**
- `POST /api/teams`

**Request Data Class:**

CreateTeamRequestDto {
    hackathonId: Long (required)
    teamName: String (required, 3-100 chars)
    description: String (required, 10-1000 chars)
    maxSize: int (required, 2-10)
    rolesNeeded: List<String> (required, at least 1)
    skillsNeeded: List<String> (optional)
    contactEmail: String (optional)
    contactPhone: String (optional)
    discordServer: String (optional)
    whatsappGroup: String (optional)
    linkedinGroup: String (optional)
    externalGroupLink: String (optional)
    isPublic: boolean (default: true)
    autoAccept: boolean (default: false)
}
```

**Response Data Class:**

CreateTeamResponseDto {
    success: boolean
    message: String
    teamId: Long
    teamName: String
    createdAt: LocalDateTime
}
```

**Form Fields:**
1. **Basic Info**: Team name, description
2. **Team Size**: Max members (2-10)
3. **Roles & Skills**: What you're looking for
4. **Contact Info**: Email, phone, social links
5. **Privacy Settings**: Public/private, auto-accept

---

### 4. **Edit Team Screen** (Leader Only)
**API Required:**
- `PUT /api/teams`

**Request Data Class:**

UpdateTeamRequestDto {
    teamId: Long (required)
    teamName: String
    description: String
    rolesNeeded: List<String>
    skillsNeeded: List<String>
    contactEmail: String
    contactPhone: String
    discordServer: String
    whatsappGroup: String
    linkedinGroup: String
    externalGroupLink: String
    isPublic: boolean
    autoAccept: boolean
}
```

**Response Data Class:**

UpdateTeamResponseDto {
    success: boolean
    message: String
    updatedAt: LocalDateTime
}
```

---

### 5. **My Teams Screen**
**API Required:**
- `GET /api/teams/my-teams`

**Response Data Class:**

TeamSearchResponseDto {
    teams: List<TeamListItemDto>
    // ... pagination info
}
```

**Features:**
- List all teams user is part of
- Shows role (Leader/Member)
- Quick access to team details
- Badge showing new join requests (for leaders)

---

## 📨 Join Request Screens

### 1. **Send Join Request Screen** (Bottom Sheet/Modal)
**API Required:**
- `POST /api/join-requests`

**Request Data Class:**

SendJoinRequestDto {
    teamId: Long (required)
    requestedRole: String (required) - e.g., "Frontend Developer"
    userSkills: List<String> (required, at least 1)
    message: String (required, 10-500 chars)
}
```

**Response Data Class:**

SendJoinRequestResponseDto {
    success: boolean
    message: String
    joinRequestId: Long
    teamName: String
    status: String (PENDING)
    sentAt: LocalDateTime
    expiresAt: LocalDateTime
}
```

**Form:**
1. **Role Selection**: Dropdown/picker from rolesNeeded
2. **Skills**: Multi-select from user's profile skills
3. **Message**: Why you want to join
4. **Submit Button**

---

### 2. **My Join Requests Screen**
**API Required:**
- `GET /api/join-requests/my`

**Response Data Class:**

MyJoinRequestsResponseDto {
    activeRequests: List<MyJoinRequestItemDto>
    pastRequests: List<MyJoinRequestItemDto>
    hasActiveRequest: boolean
    nextAvailableRequestTime: String
}

MyJoinRequestItemDto {
    joinRequestId: Long
    teamId: Long
    teamName: String
    hackathonTitle: String
    requestedRole: String
    userSkills: List<String>
    message: String
    status: String (PENDING/ACCEPTED/REJECTED/EXPIRED)
    responseMessage: String
    sentAt: LocalDateTime
    expiresAt: LocalDateTime
    processedAt: LocalDateTime
    canCancel: boolean
    isExpired: boolean
}
```

**Secondary API:**
- `POST /api/join-requests/cancel` - Cancel pending request

**UI:**
- **Active Tab**: Pending requests with cancel option
- **Past Tab**: Accepted/Rejected/Expired requests with status

---

### 3. **Manage Join Requests Screen** (Team Leader)
**API Required:**
- `GET /api/join-requests/team/{teamId}`

**Response Data Class:**

TeamJoinRequestsResponseDto {
    pendingRequests: List<JoinRequestListItemDto>
    processedRequests: List<JoinRequestListItemDto>
    totalPending: int
    totalProcessed: int
    teamName: String
    availableSlots: int
}

JoinRequestListItemDto {
    joinRequestId: Long
    teamId: Long
    teamName: String
    hackathonTitle: String
    requesterId: Long
    requesterName: String
    requesterAvatarId: String
    requesterCollege: String
    requesterYear: String
    requestedRole: String
    requesterSkills: List<String>
    message: String
    matchingSkills: List<String>
    matchPercentage: int
    status: String
    sentAt: LocalDateTime
    expiresAt: LocalDateTime
    processedAt: LocalDateTime
    isExpired: boolean
}
```

**Secondary API:**
- `POST /api/join-requests/process` - Accept/Reject request

**Request for Process:**

ProcessJoinRequestDto {
    joinRequestId: Long (required)
    action: String (required) - "ACCEPT" or "REJECT"
    responseMessage: String (optional, max 200 chars)
}
```

**UI:**
- **Pending Tab**: Cards with Accept/Reject buttons
- **Processed Tab**: History of accepted/rejected requests
- **Requester Cards**: Show profile, skills, match percentage
- **Quick View**: Tap to see requester's full profile

---

### 4. **Join Request Stats Screen**
**API Required:**
- `GET /api/join-requests/stats`

**Response Data Class:**

JoinRequestStatsDto {
    totalSent: int
    totalAccepted: int
    totalRejected: int
    totalExpired: int
    currentPending: int
    acceptanceRate: double
    firstRequestSent: LocalDateTime
    lastRequestSent: LocalDateTime
}
```

---

## 🔔 Notifications Screen

**Primary API:**
- `GET /api/notifications` (Assuming exists based on NotificationDto)

**Response Data Class:**

NotificationListResponseDto {
    notifications: List<NotificationItemDto>
    unreadCount: int
    totalCount: int
    hasMore: boolean
    page: int
    size: int
}

NotificationItemDto {
    notificationId: Long
    title: String
    message: String
    type: String (HACKATHON_DEADLINE/JOIN_REQUEST/TEAM_UPDATE/GENERAL)
    priority: Priority (LOW/MEDIUM/HIGH/URGENT)
    isRead: boolean
    actionUrl: String (deep link)
    relatedEntityId: Long
    relatedEntityType: String
    createdAt: LocalDateTime
    readAt: LocalDateTime
    expiresAt: LocalDateTime
    isExpired: boolean
}
```

**Secondary APIs:**
- `POST /api/notifications/mark-read` - Mark single notification as read
- `POST /api/notifications/mark-all-read` - Mark all as read
- `DELETE /api/notifications/{id}` - Delete notification
- `GET /api/notifications/stats` - Get notification stats

**Features:**
1. **Notification List**: Grouped by date (Today, Yesterday, This Week)
2. **Unread Badge**: Show count on app icon
3. **Priority Colors**: Different colors for priority levels
4. **Action Links**: Tap to navigate to related screen
5. **Swipe Actions**: Swipe to mark as read or delete
6. **Pull to Refresh**: Reload notifications

---

## 📋 My Registrations Screen

**API Required:**
- `GET /api/hackathons/my-registered`

**Query Parameters:**
```
page: int (default: 0)
size: int (default: 20)
```

**Response Data Class:**

HackathonListResponseDto {
    hackathons: List<HackathonListItemDto>
    currentPage: int
    totalPages: int
    totalElements: long
    hasNext: boolean
    hasPrevious: boolean
}
```

**Features:**
- List of all hackathons user has registered for
- Filter by upcoming/past
- Quick access to:
  - View hackathon details
  - Browse teams for that hackathon
  - Unregister from hackathon

---

## ⭐ My Starred/Favorites Screen

**API Required:**
- `GET /api/hackathons/my-starred`

**Query Parameters:**
```
page: int (default: 0)
size: int (default: 20)
```

**Response Data Class:**

HackathonListResponseDto {
    hackathons: List<HackathonListItemDto>
    // ... same as above
}
```

**Features:**
- List of starred/favorited hackathons
- Quick unstar option
- Register from this screen

---

## 🎯 Create Hackathon Screen (Optional - If allowing users to create)

**API Required:**
- `POST /api/hackathons`

**Request Data Class:**

CreateHackathonRequestDto {
    title: String (required, 5-200 chars)
    description: String (required, 20-2000 chars)
    registrationLink: String (required)
    deadline: LocalDateTime (required, future date)
    posterUrl: String (optional)
    tags: List<String> (optional)
    organizer: String (optional)
    location: String (optional)
    prizePool: String (optional)
    eventStartDate: LocalDateTime (optional)
    eventEndDate: LocalDateTime (optional)
    maxTeamSize: int (optional)
    minTeamSize: int (optional)
    originalMessage: String (optional)
    contactEmail: String (optional)
}
```

**Response Data Class:**

CreateHackathonResponseDto {
    success: boolean
    message: String
    hackathonId: Long
    status: Status (PENDING/APPROVED/REJECTED)
    submittedAt: LocalDateTime
}
```

---

## 🔧 Settings Screen

**APIs Required:**
- `GET /api/profiles/me` - Get current profile
- `PUT /api/profiles` - Update profile
- `POST /api/auth/change-password` - Change password
- `GET /api/notifications/preferences` - Get notification settings
- `PUT /api/notifications/preferences` - Update notification settings

**Sections:**
1. **Account Settings**: Email, password change
2. **Profile Settings**: Edit profile button
3. **Notification Preferences**: Toggle notification types
4. **Privacy Settings**: Profile visibility
5. **About**: App version, terms, privacy policy
6. **Logout**: Clear token and navigate to login

---

## 🎨 Common Data Classes Used Across Screens

### Priority Enum

enum Priority {
    LOW,
    MEDIUM,
    HIGH,
    URGENT
}
```

### Status Enum

enum Status {
    PENDING,
    APPROVED,
    REJECTED
}
```

### TeamStatus Enum

enum TeamStatus {
    OPEN,      // Accepting members
    FULL,      // All slots filled
    CLOSED     // Not accepting members
}
```

### Roles Enum

enum Roles {
    USER,
    ADMIN,
    CREATOR
}
```

---

## 📝 Common Request Headers

All authenticated requests must include:
```
Authorization: Bearer {JWT_TOKEN}
Content-Type: application/json
```

---

## 🚀 Navigation Flow Summary

```
Login/Signup
    ↓
Email Verification (if needed)
    ↓
Profile Setup (first time only)
    ↓
Home Screen (Hackathon Feed)
    ├─→ Hackathon Details
    │       ├─→ Teams List
    │       │       ├─→ Team Details
    │       │       │       ├─→ Send Join Request
    │       │       │       └─→ Public Profile (member)
    │       │       └─→ Create Team
    │       └─→ Register/Star Actions
    ├─→ My Profile
    │       ├─→ Edit Profile
    │       ├─→ My Teams
    │       ├─→ My Registrations
    │       ├─→ My Starred
    │       └─→ Settings
    ├─→ Notifications
    │       └─→ Action Links (to related screens)
    ├─→ My Join Requests
    │       ├─→ Request Details
    │       └─→ Cancel Request
    └─→ Search/Filter
```

---

## 💡 Tips for Frontend Development

### 1. **State Management**
Store these globally:
- JWT Token (secure storage)
- User ID
- Profile completion status
- Unread notification count

### 2. **Error Handling**
All API responses have consistent structure. Check `success: boolean` field.

### 3. **Loading States**
Show loading indicators while API calls are in progress.

### 4. **Caching**
Cache frequently accessed data:
- User profile
- Current teams
- Registered hackathons

### 5. **Real-time Updates**
Consider implementing:
- WebSocket for notifications
- Pull-to-refresh for feeds
- Auto-refresh on app foreground

### 6. **Offline Support**
Store critical data locally:
- User profile
- Recent hackathons
- Team details

### 7. **Pagination**
Implement infinite scrolling for:
- Hackathon feed
- Teams list
- Notifications
- Join requests

### 8. **Deep Linking**
Handle deep links from:
- Email verification links
- Password reset links
- Notification actions
- Shared hackathons/teams

---

## 📞 API Base URL

**Development:**
```
http://localhost:8080
```

**Production:**
```
https://your-api-domain.com
```

---

## 🔗 Related Documentation

- [Endpoints.md](./Endpoints.md) - Detailed API documentation
- [AuthenticationReadme.md](./AuthenticationReadme.md) - Authentication flow details
- [ProjectStructure.md](./ProjectStructure.md) - Backend structure

---

## ✅ Checklist for Each Screen

- [ ] Identify required APIs
- [ ] Create request/response data models
- [ ] Handle loading states
- [ ] Handle error states
- [ ] Implement success feedback
- [ ] Add input validation
- [ ] Test with various data scenarios
- [ ] Implement pull-to-refresh (where applicable)
- [ ] Add empty states
- [ ] Test navigation flows

---

**Last Updated:** October 17, 2025  
**Version:** 1.0

