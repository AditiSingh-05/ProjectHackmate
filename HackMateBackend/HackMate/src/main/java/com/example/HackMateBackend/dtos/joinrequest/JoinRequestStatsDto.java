package com.example.HackMateBackend.dtos.joinrequest;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JoinRequestStatsDto {
    private int totalSent;
    private int totalAccepted;
    private int totalRejected;
    private int totalExpired;
    private int currentPending;
    private double acceptanceRate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime firstRequestSent;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastRequestSent;
}
