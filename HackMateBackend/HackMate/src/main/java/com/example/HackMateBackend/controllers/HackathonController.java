package com.example.HackMateBackend.controllers;

import com.example.HackMateBackend.dtos.hackathon.*;
import com.example.HackMateBackend.services.implementations.CustomUserDetailService.UserPrincipal;
import com.example.HackMateBackend.services.interfaces.HackathonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hackathons")
@RequiredArgsConstructor
public class HackathonController {

    private final HackathonService hackathonService;

    // === Public Endpoints (No login required) ===

    @GetMapping("/feed")
    public ResponseEntity<HackathonListResponseDto> getPublicFeed(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "false") boolean showExpired,
            @RequestParam(defaultValue = "deadline") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        HackathonFilterRequestDto filterRequest = new HackathonFilterRequestDto();
        filterRequest.setSearch(search);
        filterRequest.setShowExpired(showExpired);
        filterRequest.setSortBy(sortBy);
        filterRequest.setSortDirection(sortDirection);
        filterRequest.setPage(page);
        filterRequest.setSize(size);

        try {
            HackathonListResponseDto response = hackathonService.getPublicHackathonFeed(filterRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<HackathonDetailsResponseDto> getHackathonDetails(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        try {
            Long userId = userPrincipal != null ? userPrincipal.getId() : null;
            HackathonDetailsResponseDto response = hackathonService.getHackathonDetails(id, userId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // === User Endpoints (Login required) ===

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CreateHackathonResponseDto> createHackathon(
            @Valid @RequestBody CreateHackathonRequestDto request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        try {
            CreateHackathonResponseDto response = hackathonService.createHackathon(request, userPrincipal.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new CreateHackathonResponseDto(false, e.getMessage(), null, null, null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new CreateHackathonResponseDto(false, "Internal server error", null, null, null));
        }
    }

    @PostMapping("/register")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<RegistrationToggleResponseDto> toggleRegistration(
            @Valid @RequestBody RegistrationToggleRequestDto request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        try {
            RegistrationToggleResponseDto response = hackathonService.toggleRegistration(request, userPrincipal.getId());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new RegistrationToggleResponseDto(false, e.getMessage(), false, null));
        }
    }

    @PostMapping("/star")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<StarToggleResponseDto> toggleStar(
            @Valid @RequestBody StarToggleRequestDto request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        try {
            StarToggleResponseDto response = hackathonService.toggleStar(request, userPrincipal.getId());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new StarToggleResponseDto(false, e.getMessage(), false, null));
        }
    }

    @GetMapping("/my-registered")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<HackathonListResponseDto> getMyRegisteredHackathons(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        try {
            Pageable pageable = PageRequest.of(page, size);
            HackathonListResponseDto response = hackathonService.getUserRegisteredHackathons(
                    userPrincipal.getId(), pageable);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/my-starred")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<HackathonListResponseDto> getMyStarredHackathons(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        try {
            Pageable pageable = PageRequest.of(page, size);
            HackathonListResponseDto response = hackathonService.getUserStarredHackathons(
                    userPrincipal.getId(), pageable);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // === Admin Endpoints ===

    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HackathonListResponseDto> getPendingHackathons() {
        try {
            HackathonListResponseDto response = hackathonService.getPendingHackathons();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/approve/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CreateHackathonResponseDto> approveHackathon(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        try {
            CreateHackathonResponseDto response = hackathonService.approveHackathon(id, userPrincipal.getId());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new CreateHackathonResponseDto(false, e.getMessage(), null, null, null));
        }
    }

    @PostMapping("/reject/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CreateHackathonResponseDto> rejectHackathon(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        try {
            CreateHackathonResponseDto response = hackathonService.rejectHackathon(id, userPrincipal.getId());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new CreateHackathonResponseDto(false, e.getMessage(), null, null, null));
        }
    }

    // === AI Endpoints ===

    @PostMapping("/ai-extract")
    @PreAuthorize("hasRole('USER') or hasRole('CREATOR')")
    public ResponseEntity<AIExtractionResponseDto> extractHackathonData(
            @Valid @RequestBody AIExtractionRequestDto request) {

        try {
            AIExtractionResponseDto response = hackathonService.extractHackathonData(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new AIExtractionResponseDto(false, null, null, null, null,
                            null, null, null, null, 0.0, "Internal server error"));
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Hackathon service is running");
    }
}