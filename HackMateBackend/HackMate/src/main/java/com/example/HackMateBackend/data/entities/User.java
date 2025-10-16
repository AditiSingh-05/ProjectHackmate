package com.example.HackMateBackend.data.entities;

import com.example.HackMateBackend.data.enums.Roles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Roles role = Roles.USER;

    @Column(name = "is_email_verified", nullable = false)
    private boolean emailVerified = false;

    @Column(name = "email_verification_token")
    private String emailVerificationToken;

    @Column(name = "password_reset_token")
    private String passwordResetToken;

    @Column(name = "password_reset_expires_at")
    private LocalDateTime passwordResetExpiresAt;

    @Column(name = "is_profile_setup", nullable = false)
    private boolean profileSetup = false;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (updatedAt == null) {
            updatedAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public boolean isAdmin() {
        return this.role == Roles.ADMIN;
    }

    public boolean isCreator() {
        return this.role == Roles.CREATOR;
    }

    public boolean isUser() {
        return this.role == Roles.USER;
    }

    public boolean hasRole(Roles role) {
        return this.role == role;
    }

    public void updateLastLogin() {
        this.lastLoginAt = LocalDateTime.now();
    }

    public void markProfileAsSetup() {
        this.profileSetup = true;
    }

    public void verifyEmail() {
        this.emailVerified = true;
        this.emailVerificationToken = null;
    }

    public void setPasswordResetToken(String token, int expirationHours) {
        this.passwordResetToken = token;
        this.passwordResetExpiresAt = LocalDateTime.now().plusHours(expirationHours);
    }

    public void clearPasswordResetToken() {
        this.passwordResetToken = null;
        this.passwordResetExpiresAt = null;
    }

    public boolean isPasswordResetTokenValid() {
        return passwordResetToken != null &&
                passwordResetExpiresAt != null &&
                passwordResetExpiresAt.isAfter(LocalDateTime.now());
    }
}
