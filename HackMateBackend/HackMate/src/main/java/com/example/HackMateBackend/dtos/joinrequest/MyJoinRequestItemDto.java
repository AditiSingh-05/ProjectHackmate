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
public class MyJoinRequestItemDto {
    private Long joinRequestId;
    private Long teamId;
    private String teamName;
    private String hackathonTitle;
    private String requestedRole;
    private List<String> userSkills;
    private String message;
    private String status; // PENDING, ACCEPTED, REJECTED, EXPIRED
    private String responseMessage; // From team leader

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sentAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expiresAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime processedAt;

    private boolean canCancel;
    private boolean isExpired;
}

