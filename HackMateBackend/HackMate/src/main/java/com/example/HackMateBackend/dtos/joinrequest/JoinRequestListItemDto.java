package com.example.HackMateBackend.dtos.joinrequest;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JoinRequestListItemDto {
    private Long joinRequestId;
    private Long teamId;
    private String teamName;
    private String hackathonTitle;

    // Requester information (for team leaders viewing requests)
    private Long requesterId;
    private String requesterName;
    private String requesterAvatarId;
    private String requesterCollege;
    private String requesterYear;
    private String requestedRole;
    private List<String> requesterSkills;
    private String message;

    // Matching information
    private List<String> matchingSkills;
    private int matchPercentage;

    private String status; // PENDING, ACCEPTED, REJECTED, EXPIRED

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sentAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expiresAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime processedAt;

    private boolean isExpired;
}

