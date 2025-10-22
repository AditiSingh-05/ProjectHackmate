package com.example.HackMateBackend.services.interfaces;

import com.example.HackMateBackend.dtos.hackathon.CreateHackathonRequestDto;
import com.example.HackMateBackend.dtos.hackathon.CreateHackathonResponseDto;
import com.example.HackMateBackend.dtos.hackathon.HackathonListResponseDto;
import com.example.HackMateBackend.dtos.hackathon.HackathonDetailsResponseDto;
import com.example.HackMateBackend.dtos.hackathon.RegistrationToggleRequestDto;
import com.example.HackMateBackend.dtos.hackathon.RegistrationToggleResponseDto;
import com.example.HackMateBackend.dtos.hackathon.StarToggleRequestDto;
import com.example.HackMateBackend.dtos.hackathon.StarToggleResponseDto;
import com.example.HackMateBackend.dtos.hackathon.HackathonFilterRequestDto;
import com.example.HackMateBackend.dtos.hackathon.AIExtractionRequestDto;
import com.example.HackMateBackend.dtos.hackathon.AIExtractionResponseDto;
import com.example.HackMateBackend.dtos.hackathon.HackathonListItemDto;

import com.example.HackMateBackend.data.entities.Hackathon;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface HackathonService {

    // Public endpoints
    HackathonListResponseDto getPublicHackathonFeed(HackathonFilterRequestDto filterRequest);
    HackathonDetailsResponseDto getHackathonDetails(Long hackathonId, Long userId);

    // User endpoints
    CreateHackathonResponseDto createHackathon(CreateHackathonRequestDto request, Long userId);
    RegistrationToggleResponseDto toggleRegistration(RegistrationToggleRequestDto request, Long userId);
    StarToggleResponseDto toggleStar(StarToggleRequestDto request, Long userId);
    HackathonListResponseDto getUserRegisteredHackathons(Long userId, Pageable pageable);
    HackathonListResponseDto getUserStarredHackathons(Long userId, Pageable pageable);

    // Admin endpoints
    HackathonListResponseDto getPendingHackathons();
    CreateHackathonResponseDto approveHackathon(Long hackathonId, Long adminId);
    CreateHackathonResponseDto rejectHackathon(Long hackathonId, Long adminId);

    // AI endpoints
    AIExtractionResponseDto extractHackathonData(AIExtractionRequestDto request);

    // Utility methods
    Optional<Hackathon> findById(Long id);
    void incrementViewCount(Long hackathonId);
    boolean isUserRegistered(Long hackathonId, Long userId);
    boolean isUserStarred(Long hackathonId, Long userId);
}