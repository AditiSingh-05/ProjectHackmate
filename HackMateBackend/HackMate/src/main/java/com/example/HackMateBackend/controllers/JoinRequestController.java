package com.example.HackMateBackend.controllers;

import com.example.HackMateBackend.dtos.JoinRequestDto.*;
import com.example.HackMateBackend.services.implementations.CustomUserDetailService;
import com.example.HackMateBackend.services.interfaces.JoinRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/join-requests")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('USER')")
public class JoinRequestController {

    private final JoinRequestService joinRequestService;

    @PostMapping
    public ResponseEntity<SendJoinRequestResponseDto> sendJoinRequest(
            @Valid @RequestBody SendJoinRequestDto request,
            @AuthenticationPrincipal CustomUserDetailService.UserPrincipal userPrincipal) {

        log.info("Send join request by user: {} for team: {}", userPrincipal.getId(), request.getTeamId());

        try {
            SendJoinRequestResponseDto response = joinRequestService.sendJoinRequest(request, userPrincipal.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            log.error("Error sending join request", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new SendJoinRequestResponseDto(false, e.getMessage(), null, null, null, null, null));
        } catch (Exception e) {
            log.error("Unexpected error sending join request", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new SendJoinRequestResponseDto(false, "Internal server error", null, null, null, null, null));
        }
    }

    @PostMapping("/process")
    public ResponseEntity<ProcessJoinRequestResponseDto> processJoinRequest(
            @Valid @RequestBody ProcessJoinRequestDto request,
            @AuthenticationPrincipal CustomUserDetailService.UserPrincipal userPrincipal) {

        log.info("Process join request: {} by leader: {}", request.getJoinRequestId(), userPrincipal.getId());

        try {
            ProcessJoinRequestResponseDto response = joinRequestService.processJoinRequest(request, userPrincipal.getId());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error processing join request", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ProcessJoinRequestResponseDto(false, e.getMessage(), null, null, null, null));
        } catch (Exception e) {
            log.error("Unexpected error processing join request", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ProcessJoinRequestResponseDto(false, "Internal server error", null, null, null, null));
        }
    }

    @PostMapping("/cancel")
    public ResponseEntity<CancelJoinRequestResponseDto> cancelJoinRequest(
            @Valid @RequestBody CancelJoinRequestDto request,
            @AuthenticationPrincipal CustomUserDetailService.UserPrincipal userPrincipal) {

        log.info("Cancel join request: {} by user: {}", request.getJoinRequestId(), userPrincipal.getId());

        try {
            CancelJoinRequestResponseDto response = joinRequestService.cancelJoinRequest(request, userPrincipal.getId());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error cancelling join request", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CancelJoinRequestResponseDto(false, e.getMessage(), null));
        } catch (Exception e) {
            log.error("Unexpected error cancelling join request", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CancelJoinRequestResponseDto(false, "Internal server error", null));
        }
    }

    @GetMapping("/my")
    public ResponseEntity<MyJoinRequestsResponseDto> getMyJoinRequests(
            @AuthenticationPrincipal CustomUserDetailService.UserPrincipal userPrincipal) {

        log.info("Fetching join requests for user: {}", userPrincipal.getId());

        try {
            MyJoinRequestsResponseDto response = joinRequestService.getMyJoinRequests(userPrincipal.getId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching user join requests", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/team/{teamId}")
    public ResponseEntity<TeamJoinRequestsResponseDto> getTeamJoinRequests(
            @PathVariable Long teamId,
            @AuthenticationPrincipal CustomUserDetailService.UserPrincipal userPrincipal) {

        log.info("Fetching join requests for team: {} by leader: {}", teamId, userPrincipal.getId());

        try {
            TeamJoinRequestsResponseDto response = joinRequestService.getTeamJoinRequests(teamId, userPrincipal.getId());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error fetching team join requests", e);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            log.error("Unexpected error fetching team join requests", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<JoinRequestStatsDto> getJoinRequestStats(
            @AuthenticationPrincipal CustomUserDetailService.UserPrincipal userPrincipal) {

        log.info("Fetching join request stats for user: {}", userPrincipal.getId());

        try {
            JoinRequestStatsDto response = joinRequestService.getJoinRequestStats(userPrincipal.getId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching join request stats", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Join request service is running");
    }
}