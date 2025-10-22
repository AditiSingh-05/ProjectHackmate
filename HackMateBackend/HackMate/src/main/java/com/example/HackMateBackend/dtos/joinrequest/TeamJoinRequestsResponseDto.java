package com.example.HackMateBackend.dtos.joinrequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamJoinRequestsResponseDto {
    private List<JoinRequestListItemDto> pendingRequests;
    private List<JoinRequestListItemDto> processedRequests;
    private int totalPending;
    private int totalProcessed;
    private String teamName;
    private Integer availableSlots;
}

