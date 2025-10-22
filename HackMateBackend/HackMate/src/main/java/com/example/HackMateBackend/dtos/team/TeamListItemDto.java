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
public class TeamListItemDto {
    private Long teamId;
    private String teamName;
    private String description;
    private Integer maxSize;
    private Integer currentSize;
    private Integer availableSlots;
    private String leaderName;
    private String leaderAvatarId;
    private List<Roles> rolesNeeded;
    private List<String> skillsNeeded;
    private TeamStatus status; // OPEN, FULL, CLOSED
    private boolean isPublic;
    private boolean autoAccept;
    private List<String> matchingSkills; // Skills that match current user
    private Integer matchPercentage;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
