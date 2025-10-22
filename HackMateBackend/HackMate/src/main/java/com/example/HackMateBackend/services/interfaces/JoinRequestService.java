package com.example.HackMateBackend.services.interfaces;

import com.example.HackMateBackend.dtos.joinrequest.*;

public interface JoinRequestService {

    // Join request operations
    SendJoinRequestResponseDto sendJoinRequest(SendJoinRequestDto request, Long userId);
    ProcessJoinRequestResponseDto processJoinRequest(ProcessJoinRequestDto request, Long leaderId);
    CancelJoinRequestResponseDto cancelJoinRequest(CancelJoinRequestDto request, Long userId);

    // Data retrieval
    MyJoinRequestsResponseDto getMyJoinRequests(Long userId);
    TeamJoinRequestsResponseDto getTeamJoinRequests(Long teamId, Long leaderId);
    JoinRequestStatsDto getJoinRequestStats(Long userId);

    // Utility methods
    boolean hasActiveJoinRequest(Long userId);
    boolean canSendJoinRequest(Long userId, Long teamId);
    void expireOldRequests();
}