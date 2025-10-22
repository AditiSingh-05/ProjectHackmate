package com.example.HackMateBackend.controllers;

import com.example.HackMateBackend.dtos.profile.*;
import com.example.HackMateBackend.services.implementations.CustomUserDetailService;
import com.example.HackMateBackend.services.interfaces.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('USER')")
public class ProfileController {

    private final ProfileService profileService;


    @PostMapping("/setup")
    public ResponseEntity<ProfileSetupResponseDto> setupProfile(
            @Valid @RequestBody ProfileSetupRequestDto request,
            @AuthenticationPrincipal CustomUserDetailService.UserPrincipal userPrincipal) {

        log.info("Profile setup request by user: {}", userPrincipal.getId());

        try {
            ProfileSetupResponseDto response = profileService.setupProfile(request, userPrincipal.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            log.error("Error setting up profile", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ProfileSetupResponseDto(false, e.getMessage(), null, null, 0));
        } catch (Exception e) {
            log.error("Unexpected error setting up profile", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ProfileSetupResponseDto(false, "Internal server error", null, null, 0));
        }
    }

    @PutMapping
    public ResponseEntity<ProfileUpdateResponseDto> updateProfile(
            @Valid @RequestBody ProfileUpdateRequestDto request,
            @AuthenticationPrincipal CustomUserDetailService.UserPrincipal userPrincipal) {

        log.info("Profile update request by user: {}", userPrincipal.getId());

        try {
            ProfileUpdateResponseDto response = profileService.updateProfile(request, userPrincipal.getId());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error updating profile", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ProfileUpdateResponseDto(false, e.getMessage(), null));
        } catch (Exception e) {
            log.error("Unexpected error updating profile", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ProfileUpdateResponseDto(false, "Internal server error", null));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<PrivateProfileResponseDto> getMyProfile(
            @AuthenticationPrincipal CustomUserDetailService.UserPrincipal userPrincipal) {

        log.info("Private profile requested by user: {}", userPrincipal.getId());

        try {
            PrivateProfileResponseDto response = profileService.getMyProfile(userPrincipal.getId());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error fetching private profile", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("Unexpected error fetching private profile", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<PublicProfileResponseDto> getPublicProfile(
            @PathVariable Long userId,
            @AuthenticationPrincipal CustomUserDetailService.UserPrincipal userPrincipal) {

        log.info("Public profile for user: {} requested by: {}", userId, userPrincipal.getId());

        try {
            PublicProfileResponseDto response = profileService.getPublicProfile(userId, userPrincipal.getId());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error fetching public profile", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("Unexpected error fetching public profile", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/reviews")
    public ResponseEntity<AddReviewResponseDto> addReview(
            @Valid @RequestBody AddReviewRequestDto request,
            @AuthenticationPrincipal CustomUserDetailService.UserPrincipal userPrincipal) {

        log.info("Add review request by user: {} for user: {}", userPrincipal.getId(), request.getUserIdToReview());

        try {
            AddReviewResponseDto response = profileService.addReview(request, userPrincipal.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            log.error("Error adding review", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new AddReviewResponseDto(false, e.getMessage(), null, null));
        } catch (Exception e) {
            log.error("Unexpected error adding review", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AddReviewResponseDto(false, "Internal server error", null, null));
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Profile service is running");
    }


}