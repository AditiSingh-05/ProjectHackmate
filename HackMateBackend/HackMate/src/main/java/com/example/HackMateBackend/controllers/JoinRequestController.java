package com.example.HackMateBackend.controllers;

import com.example.HackMateBackend.dtos.joinrequest.*;
import com.example.HackMateBackend.services.implementations.CustomUserDetailService.UserPrincipal;
import com.example.HackMateBackend.services.interfaces.JoinRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/join-requests")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class JoinRequestController {

    private final JoinRequestService joinRequestService;

    @PostMapping
    public ResponseEntity<SendJoinRequestResponseDto> sendJoinRequest(
            @Valid @RequestBody SendJoinRequestDto request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        try {
            SendJoinRequestResponseDto response = joinRequestService.sendJoinRequest(request, userPrincipal.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new SendJoinRequestResponseDto(false, e.getMessage(), null, null, null, null, null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new SendJoinRequestResponseDto(false, "Internal server error", null, null, null, null, null));
        }
    }

    @PostMapping("/process")
    public ResponseEntity<ProcessJoinRequestResponseDto> processJoinRequest(
            @Valid @RequestBody ProcessJoinRequestDto request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        try {
            ProcessJoinRequestResponseDto response = joinRequestService.processJoinRequest(request, userPrincipal.getId());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ProcessJoinRequestResponseDto(false, e.getMessage(), null, null, null, null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ProcessJoinRequestResponseDto(false, "Internal server error", null, null, null, null));
        }
    }

    @PostMapping("/cancel")
    public ResponseEntity<CancelJoinRequestResponseDto> cancelJoinRequest(
            @Valid @RequestBody CancelJoinRequestDto request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        try {
            CancelJoinRequestResponseDto response = joinRequestService.cancelJoinRequest(request, userPrincipal.getId());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new CancelJoinRequestResponseDto(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new CancelJoinRequestResponseDto(false, "Internal server error", null));
        }
    }

    @GetMapping("/my")
    public ResponseEntity<MyJoinRequestsResponseDto> getMyJoinRequests(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        try {
            MyJoinRequestsResponseDto response = joinRequestService.getMyJoinRequests(userPrincipal.getId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/team/{teamId}")
    public ResponseEntity<TeamJoinRequestsResponseDto> getTeamJoinRequests(
            @PathVariable Long teamId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        try {
            TeamJoinRequestsResponseDto response = joinRequestService.getTeamJoinRequests(teamId, userPrincipal.getId());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<JoinRequestStatsDto> getJoinRequestStats(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        try {
            JoinRequestStatsDto response = joinRequestService.getJoinRequestStats(userPrincipal.getId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Join request service is running");
    }
}