# HackMate Authentication System - Complete Guide

## 🔧 Fixed Critical Issues Before First Run

**I've identified and fixed several critical issues that would have prevented your application from running:**

1. **Missing JWT Configuration** - Added essential JWT properties to `application.properties`
2. **JwtConfig Constructor Issue** - Removed conflicting `@AllArgsConstructor` annotation
3. **SecurityConfig Bean Visibility** - Fixed private modifier on `@Bean` methods
4. **Email Password Placeholder** - Noted need for actual Gmail App Password

## 🚀 Quick Start Guide

### Prerequisites
1. **MySQL Database**: Ensure MySQL is running on localhost:3306 with database named `hackmate`
2. **Gmail App Password**: Replace `your_actual_gmail_app_password_here` in `application.properties` with your actual Gmail App Password
3. **Java 17+** and **Maven** installed

### First Run Steps
1. Update the Gmail password in `application.properties`
2. Ensure MySQL database `hackmate` exists
3. Run: `mvn clean install`
4. Run: `mvn spring-boot:run`

## 🏗️ Architecture Overview

### Core Components

#### 1. **User Entity** (`User.java`)
- Primary authentication entity with email-based authentication
- Supports email verification, password reset, and profile setup tracking
- Role-based access control (USER, CREATOR, ADMIN)
- Automatic timestamp management

#### 2. **JWT Authentication System**
- **JwtConfig**: Configuration properties for JWT secrets and expiration
- **JwtUtils**: Token generation, validation, and extraction utilities
- **JWTAuthenticationFilter**: Request interceptor for JWT validation
- **JWTAuthenticationEntryPoint**: Handles unauthorized access

#### 3. **Security Configuration**
- Stateless session management
- CORS enabled for cross-origin requests
- Role-based endpoint protection
- Password encryption with BCrypt

#### 4. **Email Service**
- Email verification for new registrations
- Password reset functionality
- Async email sending for better performance

## 🔐 Authentication Flow

### 1. User Registration Flow
```
1. User submits registration → UserController.registerUser()
2. Validate email uniqueness → UserServiceImpl.registerUser()
3. Hash password with BCrypt → PasswordEncoder
4. Generate email verification token → UUID.randomUUID()
5. Save user to database → UserRepository
6. Send verification email → EmailService
7. Return registration response
```

### 2. Email Verification Flow
```
1. User clicks email link → UserController.verifyEmailGet()
2. Extract token from request → EmailVerificationRequestDto
3. Find user by token → UserRepository.findByEmailVerificationToken()
4. Mark email as verified → User.verifyEmail()
5. Clear verification token → Save to database
```

### 3. Login Flow
```
1. User submits credentials → UserController.loginUser()
2. Authenticate with Spring Security → AuthenticationManager
3. Generate JWT token → JwtUtils.generateJwtToken()
4. Update last login time → User.updateLastLogin()
5. Return token and user info → LoginResponseDto
```

### 4. Protected Request Flow
```
1. Request with JWT token → JWTAuthenticationFilter
2. Extract token from header → parseJwt()
3. Validate token → JwtUtils.validateJwtToken()
4. Load user details → CustomUserDetailService
5. Set security context → SecurityContextHolder
6. Process request → Controller
```

## 📋 API Endpoints

### 🔓 Public Endpoints (No Authentication Required)

#### 1. User Registration
```http
POST /auth/signup
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "mypassword123",
  "confirmPassword": "mypassword123"
}
```

**Response:**
```json
{
  "userId": 1,
  "email": "user@example.com",
  "message": "User registered successfully. Please check your email for verification.",
  "role": "USER",
  "emailVerified": false,
  "additionalInfo": "Verification email sent to user@example.com"
}
```

#### 2. User Login
```http
POST /auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "mypassword123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyQGV4YW1wbGUuY29tIiwiaWF0IjoxNjc5ODI0ODAwLCJleHAiOjE2Nzk5MTEyMDB9...",
  "userId": 1,
  "email": "user@example.com",
  "role": "USER",
  "profileSetup": false,
  "emailVerified": true
}
```

#### 3. Email Verification (POST)
```http
POST /auth/verify-email
Content-Type: application/json

{
  "verificationToken": "550e8400-e29b-41d4-a716-446655440000"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Email verified successfully",
  "verifiedAt": "2024-12-29T10:30:00"
}
```

#### 4. Email Verification (GET - for email links)
```http
GET /auth/verify-email?token=550e8400-e29b-41d4-a716-446655440000
```

**Response:**
```json
{
  "success": true,
  "message": "Email verified successfully",
  "verifiedAt": "2024-12-29T10:30:00"
}
```

#### 5. Resend Email Verification
```http
POST /auth/resend-verification?email=user@example.com
```

**Response:**
```json
{
  "success": true,
  "message": "Verification email sent successfully",
  "sentAt": "2024-12-29T10:30:00"
}
```

#### 6. Forgot Password
```http
POST /auth/forgot-password
Content-Type: application/json

{
  "email": "user@example.com"
}
```

**Response:**
```json
{
  "success": true,
  "message": "If the email exists, you will receive a password reset link",
  "additionalInfo": "Password reset instructions sent"
}
```

#### 7. Reset Password
```http
POST /auth/reset-password
Content-Type: application/json

{
  "resetToken": "550e8400-e29b-41d4-a716-446655440001",
  "newPassword": "newpassword123",
  "confirmPassword": "newpassword123"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Password reset successfully"
}
```

### 🔒 Protected Endpoints (Authentication Required)

#### 8. Change Password
```http
POST /auth/change-password
Authorization: Bearer YOUR_JWT_TOKEN
Content-Type: application/json

{
  "currentPassword": "oldpassword123",
  "newPassword": "newpassword123",
  "confirmNewPassword": "newpassword123"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Password changed successfully",
  "changedAt": "2024-12-29T10:30:00"
}
```

## 🔑 JWT Token Configuration

### Token Details
- **Algorithm**: HMAC SHA-512
- **Access Token Expiration**: 24 hours (86400000 ms)
- **Refresh Token Expiration**: 7 days (604800000 ms)
- **Header Format**: `Authorization: Bearer <token>`

### Token Payload
```json
{
  "sub": "user@example.com",
  "iat": 1679824800,
  "exp": 1679911200
}
```

## 🛡️ Security Features

### 1. **Password Security**
- BCrypt hashing with salt
- Minimum 6 characters requirement
- Password confirmation validation

### 2. **Email Verification**
- UUID-based verification tokens
- Email verification required for login
- Resend verification functionality

### 3. **Password Reset Security**
- Time-limited reset tokens (24 hours)
- UUID-based reset tokens
- Secure token validation

### 4. **JWT Security**
- Stateless authentication
- Configurable expiration times
- Secure secret key (256-bit minimum recommended)

### 5. **Role-Based Access Control**
- USER: Standard user access
- CREATOR: Enhanced privileges
- ADMIN: Full administrative access

## 🚨 Error Handling

### Common Error Responses

#### Authentication Errors
```json
{
  "status": 401,
  "error": "Unauthorized",
  "message": "You need to login first",
  "path": "/protected-endpoint"
}
```

#### Validation Errors
```json
{
  "success": false,
  "message": "Passwords do not match",
  "additionalInfo": null
}
```

#### User Not Found
```json
{
  "success": false,
  "message": "User not found",
  "additionalInfo": null
}
```

## 📧 Email Templates

The system sends the following types of emails:
1. **Email Verification**: Welcome email with verification link
2. **Password Reset**: Secure reset link with expiration
3. **Account Notifications**: Various account-related updates

## 🔧 Configuration

### Database Configuration
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/hackmate
spring.datasource.username=root
spring.datasource.password=Aditi321
spring.jpa.hibernate.ddl-auto=update
```

### JWT Configuration
```properties
jwt.secret=hackmate2024secretkeyforjwttokensthatshouldbelongandcomplexenoughforsecurity
jwt.expiration=86400000
jwt.refresh-expiration=604800000
```

### Email Configuration
```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=bitversestudiohackmate@gmail.com
spring.mail.password=YOUR_GMAIL_APP_PASSWORD
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

## 🧪 Testing Your Setup

### 1. Health Check
```http
GET /actuator/health
```

### 2. Test Registration
Use the registration endpoint with a valid email to test the complete flow.

### 3. Test Email Verification
Check your email and click the verification link or use the token with the API.

### 4. Test Login
After email verification, test login to receive a JWT token.

### 5. Test Protected Endpoints
Use the JWT token to access protected endpoints like change password.

## 📝 Important Notes

1. **Email Password**: You MUST replace `your_actual_gmail_app_password_here` with your actual Gmail App Password
2. **JWT Secret**: The provided secret is for development. Use a more secure secret in production
3. **Database**: Ensure the `hackmate` database exists in MySQL
4. **CORS**: Currently configured to allow all origins for development

## 🚀 Ready to Run!

Your authentication system is now ready for its first run. The logic is comprehensive and includes:
- ✅ Complete user registration with email verification
- ✅ Secure JWT-based authentication
- ✅ Password reset functionality  
- ✅ Role-based access control
- ✅ Comprehensive error handling
- ✅ Security best practices

Just update the Gmail password and you're good to go!
