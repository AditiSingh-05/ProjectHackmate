package com.example.HackMateBackend.data.entities;

import com.example.HackMateBackend.data.enums.Status;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "join_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class JoinRequest extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    @Column(name = "requested_role", nullable = false)
    private String requestedRole;

    @ElementCollection
    @CollectionTable(name = "join_request_skills", joinColumns = @JoinColumn(name = "join_request_id"))
    @Column(name = "skill")
    private List<String> userSkills = new ArrayList<>();

    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status = Status.PENDING;

    @Column(name = "response_message")
    private String responseMessage;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    @Column(name = "processed_by")
    private Long processedBy;

    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (expiresAt == null) {
            // Join requests expire after 72 hours
            expiresAt = LocalDateTime.now().plusHours(72);
        }
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }

    public boolean canCancel() {
        return status == Status.PENDING && !isExpired();
    }

    public void accept(Long processedBy, String responseMessage) {
        this.status = Status.ACCEPTED;
        this.processedAt = LocalDateTime.now();
        this.processedBy = processedBy;
        this.responseMessage = responseMessage;
    }

    public void reject(Long processedBy, String responseMessage) {
        this.status = Status.REJECTED;
        this.processedAt = LocalDateTime.now();
        this.processedBy = processedBy;
        this.responseMessage = responseMessage;
    }

    public List<String> getMatchingSkills() {
        if (team == null || team.getSkillsNeeded() == null || userSkills == null) {
            return new ArrayList<>();
        }

        return userSkills.stream()
                .filter(skill -> team.getSkillsNeeded().contains(skill))
                .distinct()
                .toList();
    }

    public int getMatchPercentage() {
        if (team == null || team.getSkillsNeeded().isEmpty() || userSkills.isEmpty()) {
            return 0;
        }

        List<String> matchingSkills = getMatchingSkills();
        return (int) ((double) matchingSkills.size() / team.getSkillsNeeded().size() * 100);
    }
}