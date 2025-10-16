package com.example.HackMateBackend.data.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Profile extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;

    @Column(name = "hackathons_participated", nullable = false)
    private Integer hackathonsParticipated = 0;

    @Column(name = "hackathons_won", nullable = false)
    private Integer hackathonsWon = 0;

    @Column(name = "college", nullable = false)
    private String college;

    @Column(name = "year", nullable = false)
    private String year;

    @Column(name = "github_profile")
    private String githubProfile;

    @Column(name = "linkedin_profile")
    private String linkedinProfile;

    @Column(name = "portfolio_url")
    private String portfolioUrl;

    @Column(name = "avatar_id")
    private String avatarId;

    @Column(name = "main_skill")
    private String mainSkill;

    @ElementCollection
    @CollectionTable(name = "profile_skills", joinColumns = @JoinColumn(name = "profile_id"))
    @Column(name = "skill")
    private List<String> skills = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "profile_badges", joinColumns = @JoinColumn(name = "profile_id"))
    @Column(name = "badge")
    private List<String> badges = new ArrayList<>();

    @Column(name = "average_rating", nullable = false)
    private Double averageRating = 0.0;

    @Column(name = "total_reviews", nullable = false)
    private Integer totalReviews = 0;

    @OneToMany(mappedBy = "reviewedProfile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> receivedReviews = new ArrayList<>();

    public int calculateCompletionPercentage() {
        int totalFields = 10;
        int completedFields = 0;

        if (fullName != null && !fullName.trim().isEmpty()) completedFields++;
        if (bio != null && !bio.trim().isEmpty()) completedFields++;
        if (college != null && !college.trim().isEmpty()) completedFields++;
        if (year != null && !year.trim().isEmpty()) completedFields++;
        if (githubProfile != null && !githubProfile.trim().isEmpty()) completedFields++;
        if (linkedinProfile != null && !linkedinProfile.trim().isEmpty()) completedFields++;
        if (portfolioUrl != null && !portfolioUrl.trim().isEmpty()) completedFields++;
        if (avatarId != null && !avatarId.trim().isEmpty()) completedFields++;
        if (mainSkill != null && !mainSkill.trim().isEmpty()) completedFields++;
        if (skills != null && !skills.isEmpty()) completedFields++;

        return (int) ((double) completedFields / totalFields * 100);
    }

    public void updateRating(double newRating) {
        double totalRating = averageRating * totalReviews + newRating;
        totalReviews++;
        averageRating = totalRating / totalReviews;
    }

    public void addBadge(String badge) {
        if (!badges.contains(badge)) {
            badges.add(badge);
        }
    }

    public void incrementHackathonsParticipated() {
        hackathonsParticipated++;
    }

    public void incrementHackathonsWon() {
        hackathonsWon++;
        addBadge("Winner");
    }



}
