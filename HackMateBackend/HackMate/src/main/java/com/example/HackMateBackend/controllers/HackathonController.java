package com.example.HackMateBackend.controllers;

import com.example.HackMateBackend.dtos.HackathonDto.*;
import com.example.HackMateBackend.services.implementations.CustomUserDetailService;
import com.example.HackMateBackend.services.interfaces.HackathonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class HackathonController {

    private final HackathonService hackathonService;

    // Public endpoints
    @GetMapping("/feed")
    public ResponseEntity<HackathonListResponseDto> getPublicFeed(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "false") boolean showExpired,
            @RequestParam(defaultValue = "deadline") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        log.info("Public hackathon feed requested - search: {}, page: {}, size: {}", search, page, size);

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
            log.error("Error fetching public hackathon feed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<HackathonDetailsResponseDto> getHackathonDetails(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetailService.UserPrincipal userPrincipal) {

        log.info("Hackathon details requested for ID: {}", id);

        try {
            Long userId = userPrincipal != null ? userPrincipal.getId() : null;
            HackathonDetailsResponseDto response = hackathonService.getHackathonDetails(id, userId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error fetching hackathon details for ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("Unexpected error fetching hackathon details", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // User endpoints
    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('CREATOR')")
    public ResponseEntity<CreateHackathonResponseDto> createHackathon(
            @Valid @RequestBody CreateHackathonRequestDto request,
            @AuthenticationPrincipal CustomUserDetailService.UserPrincipal userPrincipal) {

        log.info("Create hackathon request by user: {}", userPrincipal.getId());

        try {
            CreateHackathonResponseDto response = hackathonService.createHackathon(request, userPrincipal.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            log.error("Error creating hackathon", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CreateHackathonResponseDto(false, e.getMessage(), null, null, null));
        } catch (Exception e) {
            log.error("Unexpected error creating hackathon", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CreateHackathonResponseDto(false, "Internal server error", null, null, null));
        }
    }

    @PostMapping("/register")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<RegistrationToggleResponseDto> toggleRegistration(
            @Valid @RequestBody RegistrationToggleRequestDto request,
            @AuthenticationPrincipal CustomUserDetailService.UserPrincipal userPrincipal) {

        log.info("Registration toggle for hackathon: {} by user: {}", request.getHackathonId(), userPrincipal.getId());

        try {
            RegistrationToggleResponseDto response = hackathonService.toggleRegistration(request, userPrincipal.getId());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error toggling registration", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new RegistrationToggleResponseDto(false, e.getMessage(), false, null));
        }
    }

    @PostMapping("/star")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<StarToggleResponseDto> toggleStar(
            @Valid @RequestBody StarToggleRequestDto request,
            @AuthenticationPrincipal CustomUserDetailService.UserPrincipal userPrincipal) {

        log.info("Star toggle for hackathon: {} by user: {}", request.getHackathonId(), userPrincipal.getId());

        try {
            StarToggleResponseDto response = hackathonService.toggleStar(request, userPrincipal.getId());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error toggling star", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new StarToggleResponseDto(false, e.getMessage(), false, null));
        }
    }

    @GetMapping("/my-registered")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<HackathonListResponseDto> getMyRegisteredHackathons(
            @AuthenticationPrincipal CustomUserDetailService.UserPrincipal userPrincipal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        log.info("Fetching registered hackathons for user: {}", userPrincipal.getId());

        try {
            Pageable pageable = PageRequest.of(page, size);
            HackathonListResponseDto response = hackathonService.getUserRegisteredHackathons(userPrincipal.getId(), pageable);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching registered hackathons", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/my-starred")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<HackathonListResponseDto> getMyStarredHackathons(
            @AuthenticationPrincipal CustomUserDetailService.UserPrincipal userPrincipal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        log.info("Fetching starred hackathons for user: {}", userPrincipal.getId());

        try {
            Pageable pageable = PageRequest.of(page, size);
            HackathonListResponseDto response = hackathonService.getUserStarredHackathons(userPrincipal.getId(), pageable);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching starred hackathons", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Admin endpoints
    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HackathonListResponseDto> getPendingHackathons() {
        log.info("Fetching pending hackathons for admin review");

        try {
            HackathonListResponseDto response = hackathonService.getPendingHackathons();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching pending hackathons", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/approve/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CreateHackathonResponseDto> approveHackathon(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetailService.UserPrincipal userPrincipal) {

        log.info("Approving hackathon: {} by admin: {}", id, userPrincipal.getId());

        try {
            CreateHackathonResponseDto response = hackathonService.approveHackathon(id, userPrincipal.getId());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error approving hackathon", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CreateHackathonResponseDto(false, e.getMessage(), null, null, null));
        }
    }

    @PostMapping("/reject/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CreateHackathonResponseDto> rejectHackathon(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetailService.UserPrincipal userPrincipal) {

        log.info("Rejecting hackathon: {} by admin: {}", id, userPrincipal.getId());

        try {
            CreateHackathonResponseDto response = hackathonService.rejectHackathon(id, userPrincipal.getId());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error rejecting hackathon", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CreateHackathonResponseDto(false, e.getMessage(), null, null, null));
        }
    }

    // AI endpoints
    @PostMapping("/ai-extract")
    @PreAuthorize("hasRole('USER') or hasRole('CREATOR')")
    public ResponseEntity<AIExtractionResponseDto> extractHackathonData(
            @Valid @RequestBody AIExtractionRequestDto request) {

        log.info("AI extraction request received");

        try {
            AIExtractionResponseDto response = hackathonService.extractHackathonData(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error in AI extraction", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AIExtractionResponseDto(false, null, null, null, null,
                            null, null, null, null, 0.0, "Internal server error"));
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Hackathon service is running");
    }
}