package com.example.HackMateBackend.controllers;

import com.example.HackMateBackend.dtos.profile.*;
import com.example.HackMateBackend.services.implementations.CustomUserDetailService.UserPrincipal;
import com.example.HackMateBackend.services.interfaces.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping("/setup")
    public ResponseEntity<ProfileSetupResponseDto> setupProfile(
            @Valid @RequestBody ProfileSetupRequestDto request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        try {
            ProfileSetupResponseDto response = profileService.setupProfile(request, userPrincipal.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ProfileSetupResponseDto(false, e.getMessage(), null, null, 0));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ProfileSetupResponseDto(false, "Internal server error", null, null, 0));
        }
    }

    @PutMapping
    public ResponseEntity<ProfileUpdateResponseDto> updateProfile(
            @Valid @RequestBody ProfileUpdateRequestDto request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        try {
            ProfileUpdateResponseDto response = profileService.updateProfile(request, userPrincipal.getId());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ProfileUpdateResponseDto(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ProfileUpdateResponseDto(false, "Internal server error", null));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<PrivateProfileResponseDto> getMyProfile(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        try {
            PrivateProfileResponseDto response = profileService.getMyProfile(userPrincipal.getId());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<PublicProfileResponseDto> getPublicProfile(
            @PathVariable Long userId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        try {
            PublicProfileResponseDto response = profileService.getPublicProfile(userId, userPrincipal.getId());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/reviews")
    public ResponseEntity<AddReviewResponseDto> addReview(
            @Valid @RequestBody AddReviewRequestDto request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        try {
            AddReviewResponseDto response = profileService.addReview(request, userPrincipal.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new AddReviewResponseDto(false, e.getMessage(), null, null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new AddReviewResponseDto(false, "Internal server error", null, null));
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Profile service is running");
    }
}