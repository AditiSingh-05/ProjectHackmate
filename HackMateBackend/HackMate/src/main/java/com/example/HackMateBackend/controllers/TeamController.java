package com.example.HackMateBackend.controllers;

import com.example.HackMateBackend.dtos.team.*;
import com.example.HackMateBackend.services.implementations.CustomUserDetailService.UserPrincipal;
import com.example.HackMateBackend.services.interfaces.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    public ResponseEntity<CreateTeamResponseDto> createTeam(
            @Valid @RequestBody CreateTeamRequestDto request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        try {
            CreateTeamResponseDto response = teamService.createTeam(request, userPrincipal.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new CreateTeamResponseDto(false, e.getMessage(), null, null, null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new CreateTeamResponseDto(false, "Internal server error", null, null, null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamDetailsResponseDto> getTeamDetails(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        try {
            TeamDetailsResponseDto response = teamService.getTeamDetails(id, userPrincipal.getId());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping
    public ResponseEntity<UpdateTeamResponseDto> updateTeam(
            @Valid @RequestBody UpdateTeamRequestDto request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        try {
            UpdateTeamResponseDto response = teamService.updateTeam(request, userPrincipal.getId());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new UpdateTeamResponseDto(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new UpdateTeamResponseDto(false, "Internal server error", null));
        }
    }

    @PostMapping("/search")
    public ResponseEntity<TeamSearchResponseDto> searchTeams(
            @Valid @RequestBody TeamSearchRequestDto searchRequest,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        try {
            TeamSearchResponseDto response = teamService.searchTeams(searchRequest, userPrincipal.getId());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/my-teams")
    public ResponseEntity<TeamSearchResponseDto> getMyTeams(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        try {
            TeamSearchResponseDto response = teamService.getUserTeams(userPrincipal.getId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{teamId}/members/{userId}")
    public ResponseEntity<RemoveMemberResponseDto> removeMember(
            @PathVariable Long teamId,
            @PathVariable Long userId,
            @RequestParam(required = false) String reason,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        try {
            RemoveMemberRequestDto request = new RemoveMemberRequestDto(teamId, userId, reason);
            RemoveMemberResponseDto response = teamService.removeMember(request, userPrincipal.getId());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new RemoveMemberResponseDto(false, e.getMessage(), null, null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new RemoveMemberResponseDto(false, "Internal server error", null, null));
        }
    }

    @PostMapping("/leave")
    public ResponseEntity<LeaveTeamResponseDto> leaveTeam(
            @Valid @RequestBody LeaveTeamRequestDto request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        try {
            LeaveTeamResponseDto response = teamService.leaveTeam(request, userPrincipal.getId());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new LeaveTeamResponseDto(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new LeaveTeamResponseDto(false, "Internal server error", null));
        }
    }

    @PostMapping("/export")
    public ResponseEntity<TeamExportResponseDto> exportTeamData(
            @Valid @RequestBody TeamExportRequestDto request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        try {
            TeamExportResponseDto response = teamService.exportTeamData(request, userPrincipal.getId());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new TeamExportResponseDto(false, e.getMessage(), null, null, null, null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new TeamExportResponseDto(false, "Internal server error", null, null, null, null));
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Team service is running");
    }
}