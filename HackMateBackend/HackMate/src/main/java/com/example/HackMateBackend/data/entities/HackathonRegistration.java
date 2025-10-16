package com.example.HackMateBackend.data.entities;

import com.example.HackMateBackend.data.enums.RegistrationStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "hackathon_registrations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class HackathonRegistration extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hackathon_id", nullable = false)
    private Hackathon hackathon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RegistrationStatus status;

    public static HackathonRegistration createRegistration(Hackathon hackathon, User user) {
        HackathonRegistration registration = new HackathonRegistration();
        registration.setHackathon(hackathon);
        registration.setUser(user);
        registration.setStatus(RegistrationStatus.REGISTERED);
        return registration;
    }

    public static HackathonRegistration createStarred(Hackathon hackathon, User user) {
        HackathonRegistration registration = new HackathonRegistration();
        registration.setHackathon(hackathon);
        registration.setUser(user);
        registration.setStatus(RegistrationStatus.STARRED);
        return registration;
    }

    public void updateStatus(RegistrationStatus newStatus) {
        this.status = newStatus;
    }
}