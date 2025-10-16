package com.example.HackMateBackend.controllers;

import com.example.HackMateBackend.dtos.TeamDto.*;
import com.example.HackMateBackend.services.implementations.CustomUserDetailService;
import com.example.HackMateBackend.services.interfaces.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('USER')")
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    public ResponseEntity<CreateTeamResponseDto> createTeam(
            @Valid @RequestBody CreateTeamRequestDto request,
            @AuthenticationPrincipal CustomUserDetailService.UserPrincipal userPrincipal) {

        log.info("Create team request by user: {} for hackathon: {}", userPrincipal.getId(), request.getHackathonId());

        try {
            CreateTeamResponseDto response = teamService.createTeam(request, userPrincipal.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            log.error("Error creating team", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CreateTeamResponseDto(false, e.getMessage(), null, null, null));
        } catch (Exception e) {
            log.error("Unexpected error creating team", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CreateTeamResponseDto(false, "Internal server error", null, null, null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamDetailsResponseDto> getTeamDetails(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetailService.UserPrincipal userPrincipal) {

        log.info("Team details requested for ID: {} by user: {}", id, userPrincipal.getId());

        try {
            TeamDetailsResponseDto response = teamService.getTeamDetails(id, userPrincipal.getId());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error fetching team details for ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("Unexpected error fetching team details", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping
    public ResponseEntity<UpdateTeamResponseDto> updateTeam(
            @Valid @RequestBody UpdateTeamRequestDto request,
            @AuthenticationPrincipal CustomUserDetailService.UserPrincipal userPrincipal) {

        log.info("Update team request for team: {} by user: {}", request.getTeamId(), userPrincipal.getId());

        try {
            UpdateTeamResponseDto response = teamService.updateTeam(request, userPrincipal.getId());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error updating team", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new UpdateTeamResponseDto(false, e.getMessage(), null));
        } catch (Exception e) {
            log.error("Unexpected error updating team", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new UpdateTeamResponseDto(false, "Internal server error", null));
        }
    }

    @PostMapping("/search")
    public ResponseEntity<TeamSearchResponseDto> searchTeams(
            @Valid @RequestBody TeamSearchRequestDto searchRequest,
            @AuthenticationPrincipal CustomUserDetailService.UserPrincipal userPrincipal) {

        log.info("Team search request for hackathon: {} by user: {}",
                searchRequest.getHackathonId(), userPrincipal.getId());

        try {
            TeamSearchResponseDto response = teamService.searchTeams(searchRequest, userPrincipal.getId());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error searching teams", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            log.error("Unexpected error searching teams", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/my-teams")
    public ResponseEntity<TeamSearchResponseDto> getMyTeams(
            @AuthenticationPrincipal CustomUserDetailService.UserPrincipal userPrincipal) {

        log.info("Fetching teams for user: {}", userPrincipal.getId());

        try {
            TeamSearchResponseDto response = teamService.getUserTeams(userPrincipal.getId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching user teams", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{teamId}/members/{userId}")
    public ResponseEntity<RemoveMemberResponseDto> removeMember(
            @PathVariable Long teamId,
            @PathVariable Long userId,
            @RequestParam(required = false) String reason,
            @AuthenticationPrincipal CustomUserDetailService.UserPrincipal userPrincipal) {

        log.info("Remove member request for team: {} by leader: {}", teamId, userPrincipal.getId());

        try {
            RemoveMemberRequestDto request = new RemoveMemberRequestDto(teamId, userId, reason);
            RemoveMemberResponseDto response = teamService.removeMember(request, userPrincipal.getId());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error removing member", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new RemoveMemberResponseDto(false, e.getMessage(), null, null));
        } catch (Exception e) {
            log.error("Unexpected error removing member", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new RemoveMemberResponseDto(false, "Internal server error", null, null));
        }
    }

    @PostMapping("/leave")
    public ResponseEntity<LeaveTeamResponseDto> leaveTeam(
            @Valid @RequestBody LeaveTeamRequestDto request,
            @AuthenticationPrincipal CustomUserDetailService.UserPrincipal userPrincipal) {

        log.info("Leave team request for team: {} by user: {}", request.getTeamId(), userPrincipal.getId());

        try {
            LeaveTeamResponseDto response = teamService.leaveTeam(request, userPrincipal.getId());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error leaving team", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new LeaveTeamResponseDto(false, e.getMessage(), null));
        } catch (Exception e) {
            log.error("Unexpected error leaving team", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LeaveTeamResponseDto(false, "Internal server error", null));
        }
    }

    @PostMapping("/export")
    public ResponseEntity<TeamExportResponseDto> exportTeamData(
            @Valid @RequestBody TeamExportRequestDto request,
            @AuthenticationPrincipal CustomUserDetailService.UserPrincipal userPrincipal) {

        log.info("Export team data request for team: {} by user: {}", request.getTeamId(), userPrincipal.getId());

        try {
            TeamExportResponseDto response = teamService.exportTeamData(request, userPrincipal.getId());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error exporting team data", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new TeamExportResponseDto(false, e.getMessage(), null, null, null, null));
        } catch (Exception e) {
            log.error("Unexpected error exporting team data", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new TeamExportResponseDto(false, "Internal server error", null, null, null, null));
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Team service is running");
    }
}