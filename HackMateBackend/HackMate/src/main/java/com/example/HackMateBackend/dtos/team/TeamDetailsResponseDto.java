package com.example.HackMateBackend.dtos.team;

import com.example.HackMateBackend.data.enums.Roles;
import com.example.HackMateBackend.data.enums.TeamStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamDetailsResponseDto {
    private Long teamId;
    private String teamName;
    private String description;
    private Integer maxSize;
    private Integer currentSize;
    private Integer availableSlots;
    private TeamStatus status;
    private boolean isPublic;
    private boolean autoAccept;
    private List<Roles> rolesNeeded;
    private List<String> skillsNeeded;

    private TeamMemberDto leader;

    private List<TeamMemberDto> members;

    private HackathonSummaryDto hackathon;

    private boolean isUserMember;
    private boolean isUserLeader;
    private boolean hasPendingRequest;
    private boolean canJoin;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}
