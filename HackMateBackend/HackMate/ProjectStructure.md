# HackMate Backend - Project Structure & Learning Guide

## Overview
HackMate is a comprehensive hackathon management platform that allows users to discover hackathons, form teams, and manage their participation. This Spring Boot application provides a complete backend API with authentication, team management, and hackathon discovery features.

## 🏗️ Project Architecture

### Technology Stack
- **Framework**: Spring Boot 3.5.6
- **Language**: Java 17
- **Database**: MySQL with JPA/Hibernate
- **Security**: Spring Security with JWT
- **Build Tool**: Maven
- **Code Generation**: Lombok + MapStruct
- **Migration**: Flyway
- **Email**: Spring Boot Mail
- **Testing**: Spring Boot Test + Security Test

### Project Structure
```
src/main/java/com/example/HackMateBackend/
├── HackMateApplication.java          # Main application entry point
├── config/                           # Configuration classes
│   ├── EmailConfigLogger.java       # Email configuration
│   ├── GlobalExceptionHandler.java  # Global error handling
│   ├── JpaConfig.java               # JPA/Database configuration
│   ├── JwtConfig.java               # JWT token configuration
│   ├── ScheduledTasks.java          # Background tasks
│   ├── SecurityConfig.java          # Spring Security setup
│   └── WebConfig.java               # Web MVC configuration
├── controllers/                      # REST API endpoints
│   ├── UserController.java         # Authentication & user management
│   ├── ProfileController.java      # User profile management
│   ├── HackathonController.java    # Hackathon CRUD operations
│   ├── TeamController.java         # Team formation & management
│   └── JoinRequestController.java  # Team join request handling
├── data/
│   ├── entities/                    # JPA entities (database models)
│   │   ├── BaseEntity.java         # Common entity fields
│   │   ├── User.java               # User authentication data
│   │   ├── Profile.java            # User profile information
│   │   ├── Hackathon.java          # Hackathon details
│   │   ├── Team.java               # Team information
│   │   ├── TeamMember.java         # Team membership
│   │   ├── JoinRequest.java        # Team join requests
│   │   ├── Review.java             # User reviews/ratings
│   │   ├── Notification.java       # User notifications
│   │   └── HackathonRegistration.java # Hackathon registrations
│   └── enums/                       # Enum definitions
├── dtos/                            # Data Transfer Objects
│   ├── AuthenticationDto.java      # Auth-related DTOs
│   ├── ProfileDto.java             # Profile-related DTOs
│   ├── HackathonDto.java           # Hackathon-related DTOs
│   ├── TeamDto.java                # Team-related DTOs
│   └── JoinRequestDto.java         # Join request DTOs
├── jwtauth/                         # JWT authentication logic
├── repositories/                    # Data access layer
├── services/
│   ├── interfaces/                  # Service contracts
│   └── implementations/            # Service implementations
└── utils/                          # Utility classes

src/main/resources/
├── application.properties          # Application configuration
├── static/                         # Static web resources
└── templates/                      # Email templates
```

## 🎯 Core Features

### 1. Authentication System
- **User Registration**: Email-based signup with verification
- **Email Verification**: Token-based email confirmation
- **Login/Logout**: JWT-based authentication
- **Password Management**: Change password, forgot password, reset password
- **Role-based Access**: USER, CREATOR, ADMIN roles

### 2. Profile Management
- **Profile Setup**: Complete user profile with skills, college, etc.
- **Avatar Management**: Profile picture handling
- **Skills & Badges**: Skill tracking and achievement badges
- **Statistics**: Hackathon participation and win tracking
- **Reviews**: Peer review and rating system

### 3. Hackathon Discovery
- **Public Feed**: Browse available hackathons
- **Search & Filter**: Find hackathons by criteria
- **Registration**: Register for hackathons
- **Starring**: Bookmark interesting hackathons
- **Status Tracking**: Track registration status

### 4. Team Formation
- **Team Creation**: Create teams for hackathons
- **Team Search**: Find teams looking for members
- **Skill Matching**: Match teams based on required skills
- **Join Requests**: Request to join teams
- **Team Management**: Manage team members and roles

### 5. Communication
- **Contact Information**: Email, phone, Discord, WhatsApp
- **External Links**: LinkedIn, GitHub integration
- **Notifications**: System notifications for important events

## 🚀 Getting Started Guide

### Prerequisites
1. **Java 17** - Download from OpenJDK or Oracle
2. **Maven 3.6+** - For dependency management
3. **MySQL 8.0+** - Database server
4. **IDE** - IntelliJ IDEA, Eclipse, or VS Code

### Environment Setup
1. **Clone the repository**
2. **Set up MySQL database**:
   ```sql
   CREATE DATABASE hackmate;
   CREATE USER 'hackmate_user'@'localhost' IDENTIFIED BY 'your_password';
   GRANT ALL PRIVILEGES ON hackmate.* TO 'hackmate_user'@'localhost';
   ```

3. **Configure application.properties**:
   ```properties
   # Database Configuration
   spring.datasource.url=jdbc:mysql://localhost:3306/hackmate
   spring.datasource.username=hackmate_user
   spring.datasource.password=your_password
   
   # Email Configuration (for verification emails)
   spring.mail.host=smtp.gmail.com
   spring.mail.port=587
   spring.mail.username=your-email@gmail.com
   spring.mail.password=your-app-password
   
   # JWT Configuration
   jwt.secret=your-secret-key
   jwt.expiration=86400000
   ```

### Running the Application
1. **Build the project**: `mvn clean install`
2. **Run the application**: `mvn spring-boot:run`
3. **Access the API**: `http://localhost:8080`

## 📚 Learning Path

### Phase 1: Understanding Spring Boot Basics
1. **Study Spring Boot Auto-configuration**
   - How `@SpringBootApplication` works
   - Component scanning and dependency injection
   - Configuration properties

2. **Learn about Spring MVC**
   - `@RestController` and `@RequestMapping`
   - Request/Response handling
   - HTTP status codes and error handling

### Phase 2: Database & JPA
1. **JPA/Hibernate Fundamentals**
   - Entity mapping with `@Entity`, `@Table`
   - Primary keys and `@GeneratedValue`
   - Column mapping and constraints

2. **Relationships**
   - `@OneToOne`, `@OneToMany`, `@ManyToOne`, `@ManyToMany`
   - Bidirectional relationships and `mappedBy`
   - Cascade types and fetch strategies

3. **Spring Data JPA**
   - Repository pattern
   - Query methods and `@Query`
   - Pagination and sorting

### Phase 3: Security & Authentication
1. **Spring Security Concepts**
   - Authentication vs Authorization
   - Security filter chain
   - User details and authorities

2. **JWT Implementation**
   - Token generation and validation
   - Security configuration
   - Protected endpoints

### Phase 4: Advanced Features
1. **Email Integration**
   - Spring Boot Mail
   - Template processing
   - Async email sending

2. **Validation**
   - Bean validation with `@Valid`
   - Custom validators
   - Error message handling

3. **Exception Handling**
   - `@ControllerAdvice`
   - Custom exception classes
   - Global error responses

## 🛠️ Key Components to Study

### 1. Security Configuration (`SecurityConfig.java`)
- JWT filter implementation
- Endpoint protection
- CORS configuration

### 2. Service Layer Pattern
- Interface-based design
- Transaction management with `@Transactional`
- Business logic separation

### 3. DTO Pattern
- Data transfer between layers
- Input validation
- Response standardization

### 4. Repository Pattern
- Custom query methods
- Database abstraction
- Data access optimization

## 🔧 Common Issues & Solutions

### 1. JPA Mapping Errors
- **Issue**: Entity relationship mapping problems
- **Solution**: Ensure correct `mappedBy` attributes and entity references

### 2. Lombok Integration
- **Issue**: IDE not recognizing generated methods
- **Solution**: Install Lombok plugin and enable annotation processing

### 3. JWT Token Issues
- **Issue**: Token validation failures
- **Solution**: Check token expiration and secret key configuration

### 4. Database Connection Problems
- **Issue**: Connection refused or authentication failures
- **Solution**: Verify database credentials and server status

## 📋 Development Workflow

1. **Feature Development**:
   - Create/update entities
   - Define DTOs for request/response
   - Implement repository methods
   - Write service logic
   - Create controller endpoints
   - Add validation and error handling

2. **Testing Strategy**:
   - Unit tests for services
   - Integration tests for repositories
   - API tests for controllers
   - Security tests for protected endpoints

3. **Code Quality**:
   - Use Lombok to reduce boilerplate
   - Follow REST API conventions
   - Implement proper exception handling
   - Add comprehensive logging

## 🎓 Next Steps for Learning

1. **Master the Basics**: Understand Spring Boot fundamentals and dependency injection
2. **Deep Dive into JPA**: Learn entity relationships and query optimization
3. **Security Deep Dive**: Understand JWT implementation and Spring Security
4. **API Design**: Learn REST principles and best practices
5. **Testing**: Write comprehensive unit and integration tests
6. **Deployment**: Learn Docker, cloud deployment, and CI/CD

This project serves as an excellent learning platform for modern Spring Boot development, covering authentication, database design, team collaboration features, and real-world API development patterns.
