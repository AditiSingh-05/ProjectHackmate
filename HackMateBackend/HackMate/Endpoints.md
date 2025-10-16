# HackMate Backend - API Endpoints Documentation

## Overview
This document provides comprehensive documentation for all API endpoints in the HackMate backend application, including request/response formats, authentication requirements, and usage examples.

## 🔐 Authentication
Most endpoints require JWT authentication. Include the JWT token in the Authorization header:
```
Authorization: Bearer <your-jwt-token>
```

## 📋 Base URL
```
http://localhost:8080
```

---

## 🔑 Authentication & User Management (`/auth`)

### 1. User Registration
**POST** `/auth/signup`
- **Description**: Register a new user account
- **Authentication**: None required
- **Content-Type**: `application/json`

**Request Body**:
```json
{
  "email": "user@example.com",
  "password": "Password123!",
  "confirmPassword": "Password123!"
}
```

**Response** (201 Created):
```json
{
  "userId": 1,
  "email": "user@example.com",
  "message": "User registered successfully. Please verify your email.",
  "role": "USER",
  "emailVerified": false,
  "token": null
}
```

**Error Response** (400 Bad Request):
```json
{
  "success": false,
  "message": "Email already exists",
  "errors": [
    {
      "field": "email",
      "message": "Email is already registered"
    }
  ],
  "timestamp": "2025-10-15T10:30:00"
}
```

### 2. User Login
**POST** `/auth/login`
- **Description**: Authenticate user and get JWT token
- **Authentication**: None required

**Request Body**:
```json
{
  "email": "user@example.com",
  "password": "Password123!"
}
```

**Response** (200 OK):
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "refresh_token_here",
  "email": "user@example.com",
  "role": "USER",
  "emailVerified": true,
  "profileSetup": true
}
```

**Error Response** (401 Unauthorized):
```json
{
  "success": false,
  "message": "Invalid credentials",
  "timestamp": "2025-10-15T10:30:00"
}
```

### 3. Change Password
**POST** `/auth/change-password`
- **Description**: Change user's password
- **Authentication**: Required (JWT)

**Request Body**:
```json
{
  "currentPassword": "OldPassword123!",
  "newPassword": "NewPassword123!",
  "confirmNewPassword": "NewPassword123!"
}
```

**Response** (200 OK):
```json
{
  "success": true,
  "message": "Password changed successfully",
  "timestamp": "2025-10-15T10:30:00"
}
```

### 4. Forgot Password
**POST** `/auth/forgot-password`
- **Description**: Request password reset email
- **Authentication**: None required

**Request Body**:
```json
{
  "email": "user@example.com"
}
```

**Response** (200 OK):
```json
{
  "success": true,
  "message": "Password reset email sent",
  "email": "user@example.com"
}
```

### 5. Reset Password
**POST** `/auth/reset-password`
- **Description**: Reset password using token from email
- **Authentication**: None required

**Request Body**:
```json
{
  "token": "reset_token_from_email",
  "newPassword": "NewPassword123!",
  "confirmPassword": "NewPassword123!"
}
```

**Response** (200 OK):
```json
{
  "success": true,
  "message": "Password reset successfully"
}
```

### 6. Verify Email (POST)
**POST** `/auth/verify-email`
- **Description**: Verify user email with token
- **Authentication**: None required

**Request Body**:
```json
{
  "token": "email_verification_token"
}
```

**Response** (200 OK):
```json
{
  "success": true,
  "message": "Email verified successfully",
  "email": "user@example.com"
}
```

### 7. Verify Email (GET)
**GET** `/auth/verify-email?token={token}`
- **Description**: Verify email via GET request (for email links)
- **Authentication**: None required

**Response** (200 OK):
```json
{
  "success": true,
  "message": "Email verified successfully",
  "email": "user@example.com"
}
```

### 8. Resend Email Verification
**POST** `/auth/resend-verification?email={email}`
- **Description**: Resend email verification
- **Authentication**: None required

**Response** (200 OK):
```json
{
  "success": true,
  "message": "Verification email sent",
  "email": "user@example.com"
}
```

### 9. Get Profile Setup Status
**GET** `/auth/profile-setup-status`
- **Description**: Check if user has completed profile setup
- **Authentication**: Required (JWT)

**Response** (200 OK):
```json
{
  "profileSetup": true,
  "completionPercentage": 85
}
```

### 10. Check Email Existence
**GET** `/auth/check-email?email={email}`
- **Description**: Check if email is already registered
- **Authentication**: None required

**Response** (200 OK):
```json
{
  "exists": true
}
```

### 11. Refresh Token
**POST** `/auth/refresh`
- **Description**: Get new access token using refresh token
- **Authentication**: None required

**Request Body**:
```json
{
  "refreshToken": "refresh_token_here"
}
```

**Response** (200 OK):
```json
{
  "token": "new_access_token",
  "refreshToken": "new_refresh_token"
}
```

### 12. Health Check
**GET** `/auth/health`
- **Description**: Service health check
- **Authentication**: None required

**Response** (200 OK):
```
Authentication service is running
```

---

## 👤 Profile Management (`/profiles`)
*All profile endpoints require USER role authentication*

### 1. Setup Profile
**POST** `/profiles/setup`
- **Description**: Complete initial profile setup
- **Authentication**: Required (USER role)

**Request Body**:
```json
{
  "fullName": "John Doe",
  "bio": "Passionate developer with 3 years of experience",
  "college": "MIT",
  "year": "3rd Year",
  "githubProfile": "https://github.com/johndoe",
  "linkedinProfile": "https://linkedin.com/in/johndoe",
  "portfolioUrl": "https://johndoe.dev",
  "avatarId": "avatar_123",
  "mainSkill": "Full Stack Development",
  "skills": ["JavaScript", "React", "Node.js", "Python", "Docker"],
  "phoneNumber": "+1234567890",
  "discordHandle": "johndoe#1234",
  "whatsappNumber": "+1234567890"
}
```

**Response** (201 Created):
```json
{
  "success": true,
  "message": "Profile setup completed successfully",
  "profileId": 1,
  "userId": 1,
  "completionPercentage": 95
}
```

### 2. Update Profile
**PUT** `/profiles`
- **Description**: Update existing profile
- **Authentication**: Required (USER role)

**Request Body**: (Same as setup profile)

**Response** (200 OK):
```json
{
  "success": true,
  "message": "Profile updated successfully",
  "profileId": 1,
  "completionPercentage": 95
}
```

### 3. Get Own Profile
**GET** `/profiles/me`
- **Description**: Get current user's profile
- **Authentication**: Required (USER role)

**Response** (200 OK):
```json
{
  "profileId": 1,
  "userId": 1,
  "fullName": "John Doe",
  "bio": "Passionate developer with 3 years of experience",
  "college": "MIT",
  "year": "3rd Year",
  "githubProfile": "https://github.com/johndoe",
  "linkedinProfile": "https://linkedin.com/in/johndoe",
  "portfolioUrl": "https://johndoe.dev",
  "avatarId": "avatar_123",
  "mainSkill": "Full Stack Development",
  "skills": ["JavaScript", "React", "Node.js", "Python", "Docker"],
  "badges": ["Winner", "Team Player"],
  "averageRating": 4.5,
  "totalReviews": 12,
  "hackathonsParticipated": 8,
  "hackathonsWon": 2,
  "completionPercentage": 95,
  "phoneNumber": "+1234567890",
  "discordHandle": "johndoe#1234",
  "whatsappNumber": "+1234567890",
  "email": "john@example.com"
}
```

### 4. Get Profile by ID
**GET** `/profiles/{profileId}`
- **Description**: Get specific user's profile by ID
- **Authentication**: Required (USER role)

**Response** (200 OK): (Same structure as Get Own Profile)

### 5. Search Profiles
**GET** `/profiles/search`
- **Description**: Search profiles with filters
- **Authentication**: Required (USER role)
- **Query Parameters**:
  - `skills` (optional): Comma-separated list of skills
  - `college` (optional): College name
  - `year` (optional): Academic year
  - `minRating` (optional): Minimum average rating
  - `page` (optional, default: 0): Page number
  - `size` (optional, default: 20): Page size

**Example**: `/profiles/search?skills=JavaScript,React&college=MIT&minRating=4.0&page=0&size=10`

**Response** (200 OK):
```json
{
  "profiles": [
    {
      "profileId": 1,
      "fullName": "John Doe",
      "bio": "Passionate developer...",
      "college": "MIT",
      "year": "3rd Year",
      "mainSkill": "Full Stack Development",
      "skills": ["JavaScript", "React", "Node.js"],
      "averageRating": 4.5,
      "totalReviews": 12,
      "hackathonsParticipated": 8,
      "hackathonsWon": 2,
      "avatarId": "avatar_123"
    }
  ],
  "totalElements": 1,
  "totalPages": 1,
  "currentPage": 0,
  "pageSize": 10,
  "hasNext": false,
  "hasPrevious": false
}
```

---

## 🏆 Hackathon Management (`/hackathons`)

### 1. Get Public Hackathon Feed
**GET** `/hackathons/feed`
- **Description**: Get public list of hackathons with filtering
- **Authentication**: None required
- **Query Parameters**:
  - `search` (optional): Search term for title/description
  - `showExpired` (optional, default: false): Include expired hackathons
  - `sortBy` (optional, default: "deadline"): Sort field
  - `sortDirection` (optional, default: "asc"): Sort direction
  - `page` (optional, default: 0): Page number
  - `size` (optional, default: 20): Page size

**Example**: `/hackathons/feed?search=AI&showExpired=false&sortBy=deadline&page=0&size=10`

**Response** (200 OK):
```json
{
  "hackathons": [
    {
      "hackathonId": 1,
      "title": "AI Innovation Challenge 2025",
      "description": "Build innovative AI solutions...",
      "registrationLink": "https://example.com/register",
      "deadline": "2025-11-15T23:59:59",
      "posterUrl": "https://example.com/poster.jpg",
      "tags": ["AI", "Machine Learning", "Innovation"],
      "organizer": "Tech Corp",
      "location": "San Francisco, CA",
      "prizePool": "$50,000",
      "eventStartDate": "2025-11-20T09:00:00",
      "eventEndDate": "2025-11-22T18:00:00",
      "maxTeamSize": 4,
      "minTeamSize": 1,
      "contactEmail": "contact@techcorp.com",
      "status": "APPROVED",
      "viewCount": 1250,
      "registrationCount": 89,
      "teamCount": 23,
      "postedBy": "TechCorp Admin",
      "isRegistered": false,
      "isStarred": false,
      "isExpired": false,
      "urgencyLevel": "MEDIUM",
      "createdAt": "2025-10-01T10:00:00"
    }
  ],
  "totalElements": 1,
  "totalPages": 1,
  "currentPage": 0,
  "pageSize": 10,
  "hasNext": false,
  "hasPrevious": false
}
```

### 2. Get Hackathon Details
**GET** `/hackathons/{hackathonId}`
- **Description**: Get detailed information about a specific hackathon
- **Authentication**: None required (but user-specific fields require authentication)

**Response** (200 OK):
```json
{
  "hackathonId": 1,
  "title": "AI Innovation Challenge 2025",
  "description": "Build innovative AI solutions that can solve real-world problems...",
  "registrationLink": "https://example.com/register",
  "deadline": "2025-11-15T23:59:59",
  "posterUrl": "https://example.com/poster.jpg",
  "tags": ["AI", "Machine Learning", "Innovation"],
  "organizer": "Tech Corp",
  "location": "San Francisco, CA",
  "prizePool": "$50,000",
  "eventStartDate": "2025-11-20T09:00:00",
  "eventEndDate": "2025-11-22T18:00:00",
  "maxTeamSize": 4,
  "minTeamSize": 1,
  "contactEmail": "contact@techcorp.com",
  "status": "APPROVED",
  "viewCount": 1251,
  "registrationCount": 89,
  "teamCount": 23,
  "isRegistered": false,
  "isStarred": false,
  "isExpired": false,
  "urgencyLevel": "MEDIUM",
  "postedBy": "TechCorp Admin",
  "approvedAt": "2025-10-01T10:00:00",
  "createdAt": "2025-09-30T15:30:00"
}
```

### 3. Create Hackathon
**POST** `/hackathons`
- **Description**: Create a new hackathon (requires authentication)
- **Authentication**: Required (USER role)

**Request Body**:
```json
{
  "title": "AI Innovation Challenge 2025",
  "description": "Build innovative AI solutions that can solve real-world problems...",
  "registrationLink": "https://example.com/register",
  "deadline": "2025-11-15T23:59:59",
  "posterUrl": "https://example.com/poster.jpg",
  "tags": ["AI", "Machine Learning", "Innovation"],
  "organizer": "Tech Corp",
  "location": "San Francisco, CA",
  "prizePool": "$50,000",
  "eventStartDate": "2025-11-20T09:00:00",
  "eventEndDate": "2025-11-22T18:00:00",
  "maxTeamSize": 4,
  "minTeamSize": 1,
  "contactEmail": "contact@techcorp.com",
  "originalMessage": "Original submission text for AI processing"
}
```

**Response** (201 Created):
```json
{
  "success": true,
  "message": "Hackathon submitted successfully. It will be reviewed by admins.",
  "hackathonId": 1,
  "status": "PENDING",
  "createdAt": "2025-10-15T10:30:00"
}
```

### 4. Toggle Registration
**POST** `/hackathons/register`
- **Description**: Register or unregister for a hackathon
- **Authentication**: Required (USER role)

**Request Body**:
```json
{
  "hackathonId": 1,
  "register": true
}
```

**Response** (200 OK):
```json
{
  "success": true,
  "message": "Successfully registered for hackathon",
  "isRegistered": true,
  "registeredAt": "2025-10-15T10:30:00"
}
```

### 5. Toggle Star
**POST** `/hackathons/star`
- **Description**: Star or unstar a hackathon for bookmarking
- **Authentication**: Required (USER role)

**Request Body**:
```json
{
  "hackathonId": 1,
  "star": true
}
```

**Response** (200 OK):
```json
{
  "success": true,
  "message": "Hackathon starred successfully",
  "isStarred": true,
  "starredAt": "2025-10-15T10:30:00"
}
```

### 6. Get User's Registered Hackathons
**GET** `/hackathons/registered`
- **Description**: Get current user's registered hackathons
- **Authentication**: Required (USER role)
- **Query Parameters**:
  - `page` (optional, default: 0): Page number
  - `size` (optional, default: 20): Page size

**Response** (200 OK):
```json
{
  "hackathons": [
    {
      "hackathonId": 1,
      "title": "AI Innovation Challenge 2025",
      "description": "Build innovative AI solutions...",
      "deadline": "2025-11-15T23:59:59",
      "posterUrl": "https://example.com/poster.jpg",
      "tags": ["AI", "Machine Learning"],
      "organizer": "Tech Corp",
      "location": "San Francisco, CA",
      "prizePool": "$50,000",
      "isRegistered": true,
      "isStarred": false,
      "urgencyLevel": "MEDIUM",
      "createdAt": "2025-10-01T10:00:00"
    }
  ],
  "totalElements": 1,
  "totalPages": 1,
  "currentPage": 0,
  "pageSize": 20,
  "hasNext": false,
  "hasPrevious": false
}
```

### 7. Get User's Starred Hackathons
**GET** `/hackathons/starred`
- **Description**: Get current user's starred hackathons
- **Authentication**: Required (USER role)
- **Query Parameters**:
  - `page` (optional, default: 0): Page number
  - `size` (optional, default: 20): Page size

**Response** (200 OK): (Same structure as registered hackathons)

### 8. Get Pending Hackathons (Admin)
**GET** `/hackathons/pending`
- **Description**: Get pending hackathons for admin review
- **Authentication**: Required (ADMIN role)

**Response** (200 OK):
```json
{
  "hackathons": [
    {
      "hackathonId": 2,
      "title": "Blockchain Innovation Summit",
      "description": "Create decentralized solutions...",
      "status": "PENDING",
      "postedBy": "user@example.com",
      "createdAt": "2025-10-15T09:00:00"
    }
  ],
  "totalElements": 1,
  "totalPages": 1,
  "currentPage": 0,
  "pageSize": 20,
  "hasNext": false,
  "hasPrevious": false
}
```

### 9. Approve Hackathon (Admin)
**POST** `/hackathons/{hackathonId}/approve`
- **Description**: Approve a pending hackathon
- **Authentication**: Required (ADMIN role)

**Response** (200 OK):
```json
{
  "success": true,
  "message": "Hackathon approved successfully",
  "hackathonId": 1,
  "status": "ACCEPTED",
  "approvedAt": "2025-10-15T10:30:00"
}
```

### 10. Reject Hackathon (Admin)
**POST** `/hackathons/{hackathonId}/reject`
- **Description**: Reject a pending hackathon
- **Authentication**: Required (ADMIN role)

**Response** (200 OK):
```json
{
  "success": true,
  "message": "Hackathon rejected",
  "hackathonId": 1,
  "status": "REJECTED",
  "rejectedAt": "2025-10-15T10:30:00"
}
```

### 11. AI Extract Hackathon Data
**POST** `/hackathons/ai-extract`
- **Description**: Extract hackathon information from text using AI
- **Authentication**: Required (USER role)

**Request Body**:
```json
{
  "messageText": "Join our AI Hackathon! Build innovative solutions. Deadline: Nov 15, 2025. Prize: $50,000. Register at: https://example.com/register"
}
```

**Response** (200 OK):
```json
{
  "success": true,
  "title": "AI Hackathon",
  "description": "Build innovative solutions",
  "registrationLink": "https://example.com/register",
  "deadline": "2025-11-15T23:59:59",
  "tags": ["AI"],
  "organizer": null,
  "location": null,
  "prizePool": "$50,000",
  "confidence": 0.75,
  "error": null
}
```

---

## 👥 Team Management (`/teams`)
*All team endpoints require USER role authentication*

### 1. Create Team
**POST** `/teams`
- **Description**: Create a new team for a hackathon
- **Authentication**: Required (USER role)

**Request Body**:
```json
{
  "hackathonId": 1,
  "teamName": "AI Innovators",
  "description": "We are passionate about building AI solutions that make a difference",
  "requiredSkills": ["Python", "Machine Learning", "TensorFlow"],
  "maxMembers": 4,
  "contactEmail": "team@aiinnovators.com",
  "discordServer": "https://discord.gg/aiinnovators",
  "isOpenForRequests": true
}
```

**Response** (201 Created):
```json
{
  "success": true,
  "message": "Team created successfully",
  "teamId": 1,
  "teamName": "AI Innovators",
  "leaderId": 1
}
```

### 2. Get Team Details
**GET** `/teams/{teamId}`
- **Description**: Get detailed information about a specific team
- **Authentication**: Required (USER role)

**Response** (200 OK):
```json
{
  "teamId": 1,
  "teamName": "AI Innovators",
  "description": "We are passionate about building AI solutions that make a difference",
  "hackathon": {
    "hackathonId": 1,
    "title": "AI Innovation Challenge 2025",
    "deadline": "2025-11-15T23:59:59"
  },
  "leader": {
    "userId": 1,
    "fullName": "John Doe",
    "mainSkill": "Full Stack Development",
    "avatarId": "avatar_123"
  },
  "members": [
    {
      "memberId": 1,
      "user": {
        "userId": 1,
        "fullName": "John Doe",
        "mainSkill": "Full Stack Development",
        "avatarId": "avatar_123"
      },
      "role": "LEADER",
      "joinedAt": "2025-10-15T10:30:00"
    }
  ],
  "requiredSkills": ["Python", "Machine Learning", "TensorFlow"],
  "maxMembers": 4,
  "currentMemberCount": 1,
  "contactEmail": "team@aiinnovators.com",
  "discordServer": "https://discord.gg/aiinnovators",
  "isOpenForRequests": true,
  "createdAt": "2025-10-15T10:30:00"
}
```

### 3. Search Teams
**GET** `/teams/search`
- **Description**: Search for teams with filtering options
- **Authentication**: Required (USER role)
- **Query Parameters**:
  - `hackathonId` (optional): Filter by hackathon
  - `skills` (optional): Comma-separated required skills
  - `openForRequests` (optional): Filter teams open for join requests
  - `page` (optional, default: 0): Page number
  - `size` (optional, default: 20): Page size

**Example**: `/teams/search?hackathonId=1&skills=Python,ML&openForRequests=true&page=0&size=10`

**Response** (200 OK):
```json
{
  "teams": [
    {
      "teamId": 1,
      "teamName": "AI Innovators",
      "description": "We are passionate about building AI solutions...",
      "hackathon": {
        "hackathonId": 1,
        "title": "AI Innovation Challenge 2025"
      },
      "leader": {
        "userId": 1,
        "fullName": "John Doe",
        "mainSkill": "Full Stack Development"
      },
      "requiredSkills": ["Python", "Machine Learning", "TensorFlow"],
      "maxMembers": 4,
      "currentMemberCount": 1,
      "isOpenForRequests": true
    }
  ],
  "totalElements": 1,
  "totalPages": 1,
  "currentPage": 0,
  "pageSize": 10,
  "hasNext": false,
  "hasPrevious": false
}
```

### 4. Update Team
**PUT** `/teams/{teamId}`
- **Description**: Update team information (only by team leader)
- **Authentication**: Required (USER role, team leader)

**Request Body**: (Same as create team, excluding hackathonId)

**Response** (200 OK):
```json
{
  "success": true,
  "message": "Team updated successfully",
  "teamId": 1
}
```

### 5. Delete Team
**DELETE** `/teams/{teamId}`
- **Description**: Delete team (only by team leader)
- **Authentication**: Required (USER role, team leader)

**Response** (200 OK):
```json
{
  "success": true,
  "message": "Team deleted successfully"
}
```

### 6. Leave Team
**POST** `/teams/{teamId}/leave`
- **Description**: Leave a team (members only, not leader)
- **Authentication**: Required (USER role, team member)

**Response** (200 OK):
```json
{
  "success": true,
  "message": "Successfully left the team"
}
```

### 7. Remove Team Member
**DELETE** `/teams/{teamId}/members/{userId}`
- **Description**: Remove a member from team (only by team leader)
- **Authentication**: Required (USER role, team leader)

**Response** (200 OK):
```json
{
  "success": true,
  "message": "Member removed from team successfully"
}
```

### 8. Get User's Teams
**GET** `/teams/my-teams`
- **Description**: Get teams where current user is a member
- **Authentication**: Required (USER role)

**Response** (200 OK):
```json
{
  "teams": [
    {
      "teamId": 1,
      "teamName": "AI Innovators",
      "hackathon": {
        "hackathonId": 1,
        "title": "AI Innovation Challenge 2025",
        "deadline": "2025-11-15T23:59:59"
      },
      "role": "LEADER",
      "memberCount": 3,
      "maxMembers": 4,
      "joinedAt": "2025-10-15T10:30:00"
    }
  ],
  "totalCount": 1
}
```

---

## 🤝 Join Request Management (`/join-requests`)
*All join request endpoints require USER role authentication*

### 1. Send Join Request
**POST** `/join-requests`
- **Description**: Send a request to join a team
- **Authentication**: Required (USER role)

**Request Body**:
```json
{
  "teamId": 1,
  "message": "I'm a passionate Python developer with 3 years of ML experience. I'd love to contribute to your AI project!"
}
```

**Response** (201 Created):
```json
{
  "success": true,
  "message": "Join request sent successfully",
  "requestId": 1,
  "status": "PENDING"
}
```

### 2. Get Received Join Requests
**GET** `/join-requests/received`
- **Description**: Get join requests received for user's teams
- **Authentication**: Required (USER role, team leader)

**Response** (200 OK):
```json
{
  "requests": [
    {
      "requestId": 1,
      "requester": {
        "userId": 2,
        "fullName": "Jane Smith",
        "mainSkill": "Data Science",
        "averageRating": 4.3,
        "hackathonsParticipated": 5,
        "avatarId": "avatar_456"
      },
      "team": {
        "teamId": 1,
        "teamName": "AI Innovators",
        "hackathon": {
          "hackathonId": 1,
          "title": "AI Innovation Challenge 2025"
        }
      },
      "message": "I'm a passionate Python developer with 3 years of ML experience...",
      "status": "PENDING",
      "requestedAt": "2025-10-15T11:00:00"
    }
  ],
  "totalCount": 1
}
```

### 3. Get Sent Join Requests
**GET** `/join-requests/sent`
- **Description**: Get join requests sent by current user
- **Authentication**: Required (USER role)

**Response** (200 OK):
```json
{
  "requests": [
    {
      "requestId": 1,
      "team": {
        "teamId": 1,
        "teamName": "AI Innovators",
        "leader": {
          "userId": 1,
          "fullName": "John Doe"
        },
        "hackathon": {
          "hackathonId": 1,
          "title": "AI Innovation Challenge 2025"
        }
      },
      "message": "I'm a passionate Python developer...",
      "status": "PENDING",
      "requestedAt": "2025-10-15T11:00:00"
    }
  ],
  "totalCount": 1
}
```

### 4. Respond to Join Request
**PUT** `/join-requests/{requestId}/respond`
- **Description**: Accept or reject a join request (only by team leader)
- **Authentication**: Required (USER role, team leader)

**Request Body**:
```json
{
  "response": "ACCEPTED",
  "message": "Welcome to the team! Your ML expertise will be valuable."
}
```

**Response** (200 OK):
```json
{
  "success": true,
  "message": "Join request accepted successfully",
  "requestId": 1,
  "newStatus": "ACCEPTED"
}
```

### 5. Cancel Join Request
**DELETE** `/join-requests/{requestId}`
- **Description**: Cancel a pending join request (only by requester)
- **Authentication**: Required (USER role, requester)

**Response** (200 OK):
```json
{
  "success": true,
  "message": "Join request cancelled successfully"
}
```

---

## 📊 User Management (`/users`)

### 1. Get Current User Info
**GET** `/users/me`
- **Description**: Get current user's basic information
- **Authentication**: Required (USER role)

**Response** (200 OK):
```json
{
  "userId": 1,
  "email": "user@example.com",
  "role": "USER",
  "emailVerified": true,
  "profileSetup": true,
  "createdAt": "2025-10-01T10:00:00"
}
```

### 2. Update User Role (Admin)
**PUT** `/users/{userId}/role`
- **Description**: Update user role (admin only)
- **Authentication**: Required (ADMIN role)

**Request Body**:
```json
{
  "role": "CREATOR"
}
```

**Response** (200 OK):
```json
{
  "success": true,
  "message": "User role updated successfully",
  "userId": 1,
  "newRole": "CREATOR"
}
```

### 3. Delete User Account
**DELETE** `/users/me`
- **Description**: Delete current user's account
- **Authentication**: Required (USER role)

**Response** (200 OK):
```json
{
  "success": true,
  "message": "Account deleted successfully"
}
```

---

## 📊 Status Codes & Error Handling

### Success Codes
- **200 OK**: Request successful
- **201 Created**: Resource created successfully
- **204 No Content**: Request successful, no response body

### Client Error Codes
- **400 Bad Request**: Invalid request data or validation errors
- **401 Unauthorized**: Authentication required or invalid credentials
- **403 Forbidden**: Access denied (insufficient permissions)
- **404 Not Found**: Resource not found
- **409 Conflict**: Resource conflict (e.g., email already exists)

### Server Error Codes
- **500 Internal Server Error**: Unexpected server error

### Common Error Response Format
```json
{
  "success": false,
  "message": "Error description",
  "errors": [
    {
      "field": "email",
      "message": "Email is required"
    }
  ],
  "timestamp": "2025-10-15T10:30:00"
}
```

---

## 🔧 Request/Response Guidelines

### Authentication
- Include JWT token in Authorization header: `Bearer <token>`
- Token expires after 24 hours (86400000 ms)
- Use refresh token for token renewal

### Content Types
- Request: `application/json`
- Response: `application/json`

### Pagination Parameters
- `page`: Page number (0-based, default: 0)
- `size`: Items per page (default: 20, max: 100)

### Sorting Parameters
- `sortBy`: Field to sort by
- `sortDirection`: `asc` or `desc`

### Date Format
- ISO 8601 format: `2025-10-15T10:30:00`
- All timestamps in UTC

---

## 🚀 Usage Examples

### Complete User Registration Flow
```bash
# 1. Register user
curl -X POST http://localhost:8080/auth/signup \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"Password123!","confirmPassword":"Password123!"}'

# 2. Verify email (using token from email)
curl -X POST http://localhost:8080/auth/verify-email \
  -H "Content-Type: application/json" \
  -d '{"token":"email_verification_token"}'

# 3. Login
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"Password123!"}'

# 4. Setup profile (using JWT token from login)
curl -X POST http://localhost:8080/profiles/setup \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{"fullName":"John Doe","college":"MIT","year":"3rd Year","skills":["JavaScript","React"]}'
```

### Team Formation Flow
```bash
# 1. Search for hackathons
curl -X GET "http://localhost:8080/hackathons/feed?search=AI&page=0&size=10"

# 2. Register for hackathon
curl -X POST http://localhost:8080/hackathons/register \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{"hackathonId":1,"register":true}'

# 3. Create team
curl -X POST http://localhost:8080/teams \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{"hackathonId":1,"teamName":"AI Innovators","requiredSkills":["Python","ML"]}'

# 4. Search for team members
curl -X GET "http://localhost:8080/profiles/search?skills=Python,ML" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Hackathon Management Flow
```bash
# 1. Create hackathon
curl -X POST http://localhost:8080/hackathons \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{"title":"AI Challenge","description":"Build AI solutions","deadline":"2025-11-15T23:59:59"}'

# 2. AI extract hackathon data
curl -X POST http://localhost:8080/hackathons/ai-extract \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{"messageText":"Join our AI Hackathon! Deadline: Nov 15. Prize: $50k"}'

# 3. Star a hackathon
curl -X POST http://localhost:8080/hackathons/star \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{"hackathonId":1,"star":true}'
```

---

## 📝 Notes

- All endpoints that modify data require authentication
- Some endpoints have role-based access control (USER, CREATOR, ADMIN)
- Use proper HTTP methods (GET, POST, PUT, DELETE)
- Follow RESTful conventions
- Include proper error handling in your client applications
- Use pagination for large datasets
- Validate all input data before sending requests

---

**Last Updated**: October 15, 2025  
**API Version**: 1.0  
**Base URL**: http://localhost:8080
