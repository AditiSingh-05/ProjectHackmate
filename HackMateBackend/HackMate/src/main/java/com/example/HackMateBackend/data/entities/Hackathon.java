package com.example.HackMateBackend.data.entities;

import com.example.HackMateBackend.data.enums.Status;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.Collate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hackathons")
public class Hackathon extends BaseEntity{

    @Column(name = "title",nullable = false,length = 200)
    private String title;

    @Column(name = "description",columnDefinition = "TEXT")
    private String description;

    @Column(name = "registration_link",nullable = false)
    private String registrationLink;

    @Column(name = "deadline",nullable = false)
    private LocalDateTime deadline;

    @Column(name = "poster_url")
    private String posterUrl;

    @ElementCollection
    @CollectionTable(name = "hackathon_tags",joinColumns = @JoinColumn(name = "hackathon_id"))
    @Column(name = "tags")
    private List<String> tags = new ArrayList<>();

    @Column(name = "organizer")
    private String organizer;

    @Column(name = "location")
    private String location;

    @Column(name = "prize_pool")
    private String prizePool;

    @Column(name = "event_start_date")
    private LocalDateTime eventStartDate;

    @Column(name = "event_end_date")
    private LocalDateTime eventEndDate;

    @Column(name = "max_team_size")
    private Integer maxTeamSize;

    @Column(name = "min_team_size")
    private Integer minTeamSize;

    @Column(name = "contact_email")
    private String contactEmail;


    //won't be displayed but for later ai use
    @Column(name = "original_message", columnDefinition = "TEXT")
    private String originalMessage;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status = Status.PENDING;

    @Column(name = "view_count", nullable = false)
    private Long viewCount = 0L;

    @Column(name = "registration_count", nullable = false)
    private Long registrationCount = 0L;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "approved_by")
    private Long approvedBy;

    @Column(name = "team_count", nullable = false)
    private Long teamCount = 0L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posted_by", nullable = false)
    private User postedBy;

    @OneToMany(mappedBy = "hackathon", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Team> teams = new ArrayList<>();

    @OneToMany(mappedBy = "hackathon", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<HackathonRegistration> registrations = new ArrayList<>();

    public void incrementViewCount(){
        this.viewCount++;
    }

    public void incrementRegistrationCount(){
        this.registrationCount++;
    }

    public void incrementTeamCount() {
        this.teamCount++;
    }

    public void decrementTeamCount() {
        if (this.teamCount > 0) {
            this.teamCount--;
        }
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.deadline);
    }

    public String getUrgencyLevel() {
        LocalDateTime now = LocalDateTime.now();
        long hoursUntilDeadline = java.time.Duration.between(now, deadline).toHours();

        if (hoursUntilDeadline <= 24) return "URGENT";
        if (hoursUntilDeadline <= 72) return "HIGH";
        if (hoursUntilDeadline <= 168) return "MEDIUM";
        return "LOW";
    }


    public void approve(Long approvedBy) {
        this.status = Status.ACCEPTED;
        this.approvedAt = LocalDateTime.now();
        this.approvedBy = approvedBy;
    }

    public void reject() {
        this.status = Status.REJECTED;
    }

}
//@ElementCollection : Marks the field as a collection of basic types (here, String).
//@CollectionTable :  Specifies the name of the table (hackathon_tags) that will store the tags.
//joinColumns defines the foreign key (hackathon_id) linking each tag row to the parent Hackathon entity.