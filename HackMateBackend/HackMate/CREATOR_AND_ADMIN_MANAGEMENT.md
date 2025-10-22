# Creator and Admin Management Endpoints

## Overview
This document describes the creator account setup and admin management endpoints.

## Creator Account Setup

### Environment Variables
The following environment variables have been added to `.env`:

```
CREATOR_EMAIL=hackmatecreator@gmail.com
CREATOR_PASSWORD=iamironman
```

### Creator Account Features
- **Email**: hackmatecreator@gmail.com
- **Password**: iamironman
- **Role**: CREATOR
- **Auto-verified**: Yes (no email verification required)
- **Profile Setup**: Pre-completed
- **Special Privileges**: Can manage user roles and create admins

The creator account is automatically initialized when the application starts. If it doesn't exist, it will be created. If it exists, it will be updated to ensure correct role and verification status.

---

## Creator Endpoints

All creator endpoints are protected and require `ROLE_CREATOR` authentication.

Base URL: `/api/creator/admin_control`

### 1. Change User Role
**Endpoint**: `POST /api/creator/admin_control/change-role`

**Description**: Allows the creator to change any user's role (promote to admin or demote to user).

**Authorization**: Required (ROLE_CREATOR)

**Request Body**:
```json
{
  "userId": 2,
  "newRole": "ADMIN"
}
```

**Possible Roles**: 
- `USER` - Regular user
- `ADMIN` - Administrator with hackathon approval rights

**Response** (Success - 200 OK):
```json
{
  "success": true,
  "message": "User role updated successfully",
  "userId": 2,
  "email": "user@example.com",
  "previousRole": "USER",
  "newRole": "ADMIN",
  "updatedAt": "2025-10-17T10:30:00"
}
```

**Response** (Error - 400 Bad Request):
```json
{
  "success": false,
  "message": "Cannot change the role of a creator",
  "userId": null,
  "email": null,
  "previousRole": null,
  "newRole": null,
  "updatedAt": null
}
```

**Rules**:
- Cannot change creator's own role
- Cannot change another creator's role
- Cannot assign CREATOR role to any user
- Can promote users to admins or demote admins to users

---

### 2. Get User Role Info by User ID
**Endpoint**: `GET /api/creator/admin_control/user/{userId}`

**Description**: Get detailed role information for a specific user.

**Authorization**: Required (ROLE_CREATOR)

**Path Parameter**: 
- `userId` (Long) - The user's ID

**Response** (200 OK):
```json
{
  "userId": 2,
  "email": "user@example.com",
  "role": "ADMIN",
  "emailVerified": true,
  "profileSetup": true,
  "createdAt": "2025-10-15T08:00:00",
  "lastLoginAt": "2025-10-17T09:15:00"
}
```

---

### 3. Get User Role Info by Email
**Endpoint**: `POST /api/creator/admin_control/user/by-email`

**Description**: Get detailed role information for a specific user by email.

**Authorization**: Required (ROLE_CREATOR)

**Request Body**:
```json
{
  "email": "user@example.com"
}
```

**Response** (200 OK):
```json
{
  "userId": 2,
  "email": "user@example.com",
  "role": "ADMIN",
  "emailVerified": true,
  "profileSetup": true,
  "createdAt": "2025-10-15T08:00:00",
  "lastLoginAt": "2025-10-17T09:15:00"
}
```

---

### 4. Get All Admins
**Endpoint**: `GET /api/creator/admin_control/admins`

**Description**: Retrieve a list of all users with ADMIN role.

**Authorization**: Required (ROLE_CREATOR)

**Response** (200 OK):
```json
[
  {
    "userId": 2,
    "email": "admin1@example.com",
    "role": "ADMIN",
    "emailVerified": true,
    "profileSetup": true,
    "createdAt": "2025-10-15T08:00:00",
    "lastLoginAt": "2025-10-17T09:15:00"
  },
  {
    "userId": 5,
    "email": "admin2@example.com",
    "role": "ADMIN",
    "emailVerified": true,
    "profileSetup": true,
    "createdAt": "2025-10-16T10:00:00",
    "lastLoginAt": "2025-10-17T08:30:00"
  }
]
```

---

### 5. Get All Users
**Endpoint**: `GET /api/creator/admin_control/users`

**Description**: Retrieve a list of all users with their roles.

**Authorization**: Required (ROLE_CREATOR)

**Response** (200 OK):
```json
[
  {
    "userId": 1,
    "email": "hackmatecreator@gmail.com",
    "role": "CREATOR",
    "emailVerified": true,
    "profileSetup": true,
    "createdAt": "2025-10-17T00:00:00",
    "lastLoginAt": "2025-10-17T10:00:00"
  },
  {
    "userId": 2,
    "email": "admin@example.com",
    "role": "ADMIN",
    "emailVerified": true,
    "profileSetup": true,
    "createdAt": "2025-10-15T08:00:00",
    "lastLoginAt": "2025-10-17T09:15:00"
  },
  {
    "userId": 3,
    "email": "user@example.com",
    "role": "USER",
    "emailVerified": true,
    "profileSetup": true,
    "createdAt": "2025-10-16T12:00:00",
    "lastLoginAt": "2025-10-17T08:45:00"
  }
]
```

---

## Admin Endpoints (For Hackathon Approval)

All admin endpoints require `ROLE_ADMIN` authentication. These endpoints already existed but are documented here for reference.

Base URL: `/api/hackathons`

### 1. Get Pending Hackathons
**Endpoint**: `GET /api/hackathons/pending`

**Description**: Get all hackathons pending admin approval.

**Authorization**: Required (ROLE_ADMIN)

**Response** (200 OK):
```json
{
  "success": true,
  "message": "Pending hackathons retrieved successfully",
  "hackathons": [...],
  "totalCount": 5,
  "page": 0,
  "size": 20
}
```

---

### 2. Approve Hackathon
**Endpoint**: `POST /api/hackathons/approve/{id}`

**Description**: Approve a pending hackathon.

**Authorization**: Required (ROLE_ADMIN)

**Path Parameter**: 
- `id` (Long) - The hackathon ID

**Response** (200 OK):
```json
{
  "success": true,
  "message": "Hackathon approved successfully",
  "hackathonId": 10,
  "title": "AI Innovation Challenge 2025",
  "status": "APPROVED"
}
```

---

### 3. Reject Hackathon
**Endpoint**: `POST /api/hackathons/reject/{id}`

**Description**: Reject a pending hackathon.

**Authorization**: Required (ROLE_ADMIN)

**Path Parameter**: 
- `id` (Long) - The hackathon ID

**Response** (200 OK):
```json
{
  "success": true,
  "message": "Hackathon rejected",
  "hackathonId": 10,
  "title": "AI Innovation Challenge 2025",
  "status": "REJECTED"
}
```

---

## Authentication Flow

### Creator Login
1. Use the login endpoint: `POST /api/auth/login`
2. Credentials:
   ```json
   {
     "email": "hackmatecreator@gmail.com",
     "password": "iamironman"
   }
   ```
3. The creator account is pre-verified and doesn't need email verification
4. Receive JWT token with ROLE_CREATOR
5. Use the token to access creator endpoints

### Making Admin from User
1. Login as creator
2. Get the user's ID (either from user list or by email)
3. Call change-role endpoint:
   ```json
   {
     "userId": 2,
     "newRole": "ADMIN"
   }
   ```
4. User now has ROLE_ADMIN and can approve/reject hackathons

---

## Security Configuration

The following routes are protected in SecurityConfig:

```java
.requestMatchers("/creator/admin_control/**").hasRole("CREATOR")
.requestMatchers("/api/creator/admin_control/**").hasRole("CREATOR")
.requestMatchers("/admin/**").hasRole("ADMIN")
.requestMatchers("/api/admin/**").hasRole("ADMIN")
.requestMatchers(HttpMethod.POST, "/hackathons/approve/**").hasRole("ADMIN")
.requestMatchers(HttpMethod.POST, "/api/hackathons/approve/**").hasRole("ADMIN")
.requestMatchers(HttpMethod.GET, "/hackathons/pending").hasRole("ADMIN")
.requestMatchers(HttpMethod.GET, "/api/hackathons/pending").hasRole("ADMIN")
```

---

## Example Workflow

### Scenario: Creator Creates an Admin

1. **Creator logs in**:
   ```bash
   POST /api/auth/login
   {
     "email": "hackmatecreator@gmail.com",
     "password": "iamironman"
   }
   ```
   Response includes JWT token with ROLE_CREATOR

2. **Creator views all users**:
   ```bash
   GET /api/creator/admin_control/users
   Authorization: Bearer <creator-jwt-token>
   ```

3. **Creator promotes user to admin**:
   ```bash
   POST /api/creator/admin_control/change-role
   Authorization: Bearer <creator-jwt-token>
   {
     "userId": 3,
     "newRole": "ADMIN"
   }
   ```

4. **User (now admin) logs in again** (to get new token with ROLE_ADMIN):
   ```bash
   POST /api/auth/login
   {
     "email": "newadmin@example.com",
     "password": "theirpassword"
   }
   ```

5. **Admin approves hackathon**:
   ```bash
   POST /api/hackathons/approve/10
   Authorization: Bearer <admin-jwt-token>
   ```

---

## Notes

- Creator account is automatically created/updated on application startup
- Creator cannot change their own role or other creators' roles
- Only creator can assign/revoke ADMIN role
- Admins can approve/reject hackathons but cannot manage other users' roles
- All role changes are logged for audit purposes

