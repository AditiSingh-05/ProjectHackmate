package com.example.HackMateBackend.services.interfaces;

import com.example.HackMateBackend.dtos.team.*;
import com.example.HackMateBackend.data.entities.Team;

import java.util.Optional;

public interface TeamService {

    // Team CRUD operations
    CreateTeamResponseDto createTeam(CreateTeamRequestDto request, Long userId);
    TeamDetailsResponseDto getTeamDetails(Long teamId, Long userId);
    UpdateTeamResponseDto updateTeam(UpdateTeamRequestDto request, Long userId);
    TeamSearchResponseDto searchTeams(TeamSearchRequestDto searchRequest, Long userId);

    // Member management
    RemoveMemberResponseDto removeMember(RemoveMemberRequestDto request, Long leaderId);
    LeaveTeamResponseDto leaveTeam(LeaveTeamRequestDto request, Long userId);

    // Team data
    TeamSearchResponseDto getUserTeams(Long userId);
    TeamExportResponseDto exportTeamData(TeamExportRequestDto request, Long userId);

    // Utility methods
    Optional<Team> findById(Long id);
    boolean isUserInTeamForHackathon(Long userId, Long hackathonId);
    boolean isUserTeamLeader(Long userId, Long teamId);
    boolean isUserTeamMember(Long userId, Long teamId);
    Team getUserTeamForHackathon(Long userId, Long hackathonId);
}