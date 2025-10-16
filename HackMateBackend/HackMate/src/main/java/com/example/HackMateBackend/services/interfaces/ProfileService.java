package com.example.HackMateBackend.services.interfaces;

import com.example.HackMateBackend.dtos.ProfileDto.*;
import com.example.HackMateBackend.data.entities.Profile;

import java.util.Optional;

public interface ProfileService {

    // Profile management
    ProfileSetupResponseDto setupProfile(ProfileSetupRequestDto request, Long userId);
    ProfileUpdateResponseDto updateProfile(ProfileUpdateRequestDto request, Long userId);
    PrivateProfileResponseDto getMyProfile(Long userId);
    PublicProfileResponseDto getPublicProfile(Long profileUserId, Long viewerId);

    // Review system
    AddReviewResponseDto addReview(AddReviewRequestDto request, Long reviewerId);

    // Utility methods
    Optional<Profile> findByUserId(Long userId);
    boolean isProfileSetupComplete(Long userId);
    int calculateProfileCompletion(Long userId);
}