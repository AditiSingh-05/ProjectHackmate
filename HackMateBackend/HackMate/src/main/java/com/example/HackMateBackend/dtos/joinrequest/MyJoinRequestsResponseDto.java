package com.example.HackMateBackend.dtos.joinrequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyJoinRequestsResponseDto {
    private List<MyJoinRequestItemDto> activeRequests;
    private List<MyJoinRequestItemDto> pastRequests;
    private boolean hasActiveRequest; // Can only have 1 active request
    private String nextAvailableRequestTime; // If cooldown period exists
}

