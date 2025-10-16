package com.example.HackMateBackend.data.entities;

import com.example.HackMateBackend.data.enums.NotificationType;
import com.example.HackMateBackend.data.enums.Priority;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Notification extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(name = "type", nullable = false)
    private NotificationType type; //

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private Priority priority = Priority.MEDIUM;

    @Column(name = "is_read", nullable = false)
    private boolean isRead = false;

    @Column(name = "action_url")
    private String actionUrl;

    @Column(name = "related_entity_id")
    private Long relatedEntityId;

    @Column(name = "related_entity_type")
    private String relatedEntityType;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "read_at")
    private LocalDateTime readAt;

    public boolean isExpired() {
        return expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
    }

    public void markAsRead() {
        this.isRead = true;
        this.readAt = LocalDateTime.now();
    }

    // Factory methods for different notification types
    public static Notification createJoinRequestNotification(User user, JoinRequest joinRequest) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setTitle("New Join Request");
        notification.setMessage(String.format("New join request for team '%s' from %s",
                joinRequest.getTeam().getTeamName(), joinRequest.getRequester().getEmail()));
        notification.setType(NotificationType.JOIN_REQUEST);
        notification.setPriority(Priority.HIGH);
        notification.setRelatedEntityId(joinRequest.getId());
        notification.setRelatedEntityType("JOIN_REQUEST");
        notification.setActionUrl("/teams/" + joinRequest.getTeam().getId() + "/requests");
        return notification;
    }

    public static Notification createJoinRequestResponseNotification(User user, JoinRequest joinRequest, boolean accepted) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setTitle(accepted ? "Join Request Accepted!" : "Join Request Declined");
        notification.setMessage(String.format("Your join request for team '%s' has been %s",
                joinRequest.getTeam().getTeamName(), accepted ? "accepted" : "declined"));
        notification.setType(NotificationType.JOIN_REQUEST);
        notification.setPriority(accepted ? Priority.HIGH : Priority.MEDIUM);
        notification.setRelatedEntityId(joinRequest.getTeam().getId());
        notification.setRelatedEntityType("TEAM");
        notification.setActionUrl("/teams/" + joinRequest.getTeam().getId());
        return notification;
    }

    public static Notification createHackathonDeadlineNotification(User user, Hackathon hackathon, int hoursRemaining) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setTitle("Hackathon Deadline Reminder");
        notification.setMessage(String.format("'%s' deadline is in %d hours!", hackathon.getTitle(), hoursRemaining));
        notification.setType(NotificationType.HACKATHON_DEADLINE);
        notification.setPriority(hoursRemaining <= 24 ? Priority.URGENT : Priority.HIGH);
        notification.setRelatedEntityId(hackathon.getId());
        notification.setRelatedEntityType("HACKATHON");
        notification.setActionUrl("/hackathons/" + hackathon.getId());
        notification.setExpiresAt(hackathon.getDeadline());
        return notification;
    }
}