package com.example.HackMateBackend.dtos.team;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamMemberDto {
    private Long userId;
    private String fullName;
    private String avatarId;
    private String role; // LEADER, MEMBER
    private String assignedRole; // Frontend, Backend, etc.
    private List<String> skills;
    private String college;
    private String year;

    private String email;
    private String phone;
    private String discordUsername;
    private String githubProfile;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime joinedAt;

    private boolean active;
}

