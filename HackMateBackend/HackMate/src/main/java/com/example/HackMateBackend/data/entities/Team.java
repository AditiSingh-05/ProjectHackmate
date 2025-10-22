package com.example.HackMateBackend.data.entities;

import com.example.HackMateBackend.data.enums.Roles;
import com.example.HackMateBackend.data.enums.TeamRole;
import com.example.HackMateBackend.data.enums.TeamStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "teams")
@EqualsAndHashCode(callSuper = true)
public class Team extends BaseEntity {

    @Column(name = "team_name", nullable = false)
    private String teamName;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "max_size", nullable = false)
    private int maxSize;

    @Column(name = "current_size", nullable = false)
    private int currentSize;

    @ElementCollection
    @CollectionTable(name = "team_skills_needed", joinColumns = @JoinColumn(name = "team_id"))
    @Column(name = "skill")
    private List<String> skillsNeeded = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "team_skills_filled", joinColumns = @JoinColumn(name = "team_id"))
    @Column(name = "skill")
    private List<String> skillsFilled = new ArrayList<>();

    //fields to be available only once the member is part of the team
    @Column(name = "contact_email")
    private String contactEmail;

    @Column(name = "contact_phone")
    private String contactPhone;

    @Column(name = "discord_server")
    private String discordServer;

    @Column(name = "whatsapp_group")
    private String whatsappGroup;

    @Column(name = "linkedin_group")
    private String linkedinGroup;

    @Column(name = "external_group_link")
    private String externalGroupLink;

    //auto accept any one can join
    @Column(name = "is_public", nullable = false)
    private boolean isPublic = true;

    @Column(name = "is_full", nullable = false)
    private boolean isFull = false;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hackathon_id", nullable = false)
    private Hackathon hackathon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leader_id", nullable = false)
    private User leader;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TeamMember> members = new ArrayList<>();

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<JoinRequest> joinRequests = new ArrayList<>();

    public Integer getAvailableSlots() {
        return maxSize - currentSize;
    }

    public TeamStatus getStatus() {
        if (!isActive) return TeamStatus.OPEN;
        if (isFull || currentSize >= maxSize) return TeamStatus.CLOSED;
        return TeamStatus.OPEN;
    }

    public void addMember(User user, String assignedRole) {
        if (skillsFilled.contains(assignedRole)) {
            throw new RuntimeException("Team already has this role");
        }
        if (currentSize >= maxSize) {
            throw new RuntimeException("Team is already full");
        }

        TeamMember member = new TeamMember();
        member.setTeam(this);
        member.setUser(user);
        member.setRole(TeamRole.MEMBER);
        member.setAssignedRole(assignedRole);
        member.setActive(true);
        skillsFilled.add(assignedRole);
        skillsNeeded.remove(assignedRole);


        this.members.add(member);
        this.currentSize++;

        if (this.currentSize >= this.maxSize) {
            this.isFull = true;
        }
    }

    public void removeMember(User user) {
        members.removeIf(member -> member.getUser().getId().equals(user.getId()));
        this.currentSize--;
        this.isFull = false;
    }

    public boolean isUserMember(Long userId) {
        return members.stream()
                .anyMatch(member -> member.getUser().getId().equals(userId) && member.isActive()
                );
    }

    public List<String> getMatchingSkills(List<String> userSkills) {
        if (userSkills == null || skillsNeeded == null) {
            return new ArrayList<>();
        }

        return userSkills.stream()
                .filter(skillsNeeded::contains)
                .distinct()
                .toList();
    }

    public int calculateMatchPercentage(List<String> userSkills) {
        if (skillsNeeded.isEmpty() || userSkills == null || userSkills.isEmpty()) {
            return 0;
        }

        List<String> matchingSkills = getMatchingSkills(userSkills);
        return (int) ((double) matchingSkills.size() / skillsNeeded.size() * 100);
    }

    public void setRolesNeeded(List<String> roles) {
        this.skillsNeeded = roles;
    }
    public List<Roles> getRolesNeeded() {
        return this.skillsNeeded.stream()
                .map(role -> {
                    try {
                        return Roles.valueOf(role.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        return null; // or handle unknown roles as needed
                    }
                })
                .filter(role -> role != null)
                .toList();
    }

    public void setAutoAccept(boolean autoAccept) {
        this.isPublic = autoAccept;
    }

    public boolean isUserLeader(Long leaderId) {
        return this.leader.getId().equals(leaderId);
    }

    public boolean isAutoAccept() {
        return this.isPublic;
    }
}



//Performance: With LAZY, related data is not loaded immediately when the parent entity
// is fetched. Instead, it is loaded only when it is accessed for the first time.

//FetchType.EAGER: Loads related entities immediately when the parent is loaded.


//The column hackathon_id is a primitive type The table does not store the full Hackathon object, only its primary key.
//In Java/JPA/Hibernate:
//The field private Hackathon hackathon; allows you to directly access the related Hackathon object in Java code.
//JPA/Hibernate automatically loads the Hackathon object from the database using the hackathon_id foreign key.


//mappedBy = "team": The TeamMember entity has a field called team that refers back to this Team entity. This indicates
// that TeamMember owns the foreign key column.
//cascade = CascadeType.ALL: Any operation (save, delete, etc.) performed on Team will cascade to all TeamMembers
// (e.g., deleting a Team deletes all its members).

//Stream
