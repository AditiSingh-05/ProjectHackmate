package com.example.HackMateBackend.services.implementations;

import com.example.HackMateBackend.data.entities.*;
import com.example.HackMateBackend.dtos.ProfileDto.*;
import com.example.HackMateBackend.repositories.*;
import com.example.HackMateBackend.services.interfaces.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public ProfileSetupResponseDto setupProfile(ProfileSetupRequestDto request, Long userId) {
        log.info("Setting up profile for user: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if profile already exists
        Optional<Profile> existingProfile = profileRepository.findByUserId(userId);
        if (existingProfile.isPresent()) {
            throw new RuntimeException("Profile already exists. Use update profile instead.");
        }

        // Create new profile
        Profile profile = new Profile();
        profile.setUser(user);
        profile.setFullName(request.getFullName());
        profile.setBio(request.getBio());
        profile.setHackathonsParticipated(request.getHackathonsParticipated());
        profile.setHackathonsWon(request.getHackathonsWon());
        profile.setCollege(request.getCollege());
        profile.setYear(request.getYear());
        profile.setGithubProfile(request.getGithubProfile());
        profile.setLinkedinProfile(request.getLinkedinProfile());
        profile.setPortfolioUrl(request.getPortfolioUrl());
        profile.setAvatarId(request.getAvatarId());
        profile.setMainSkill(request.getMainSkill());

        Profile savedProfile = profileRepository.save(profile);

        // Mark user profile as setup
        user.markProfileAsSetup();
        userRepository.save(user);

        int completionPercentage = savedProfile.calculateCompletionPercentage();

        log.info("Profile setup completed for user: {} with completion: {}%", userId, completionPercentage);

        return new ProfileSetupResponseDto(
                true,
                "Profile setup completed successfully",
                userId,
                savedProfile.getFullName(),
                completionPercentage
        );
    }

    @Override
    public ProfileUpdateResponseDto updateProfile(ProfileUpdateRequestDto request, Long userId) {
        log.info("Updating profile for user: {}", userId);

        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found. Please complete profile setup first."));

        // Update fields if provided
        if (request.getFullName() != null) profile.setFullName(request.getFullName());
        if (request.getBio() != null) profile.setBio(request.getBio());
        if (request.getCollege() != null) profile.setCollege(request.getCollege());
        if (request.getYear() != null) profile.setYear(request.getYear());
        if (request.getGithubProfile() != null) profile.setGithubProfile(request.getGithubProfile());
        if (request.getLinkedinProfile() != null) profile.setLinkedinProfile(request.getLinkedinProfile());
        if (request.getPortfolioUrl() != null) profile.setPortfolioUrl(request.getPortfolioUrl());
        if (request.getMainSkill() != null) profile.setMainSkill(request.getMainSkill());

        profileRepository.save(profile);

        log.info("Profile updated for user: {}", userId);

        return new ProfileUpdateResponseDto(
                true,
                "Profile updated successfully",
                LocalDateTime.now()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PrivateProfileResponseDto getMyProfile(Long userId) {
        log.info("Fetching private profile for user: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found. Please complete profile setup."));

        List<Review> recentReviews = reviewRepository.findByReviewedProfileAndDeletedFalseOrderByCreatedAtDesc(profile)
                .stream()
                .limit(5)
                .toList();

        List<ReviewDto> reviewDtos = recentReviews.stream()
                .map(this::convertToReviewDto)
                .toList();

        return new PrivateProfileResponseDto(
                user.getId(),
                user.getEmail(),
                profile.getFullName(),
                profile.getBio(),
                profile.getHackathonsParticipated(),
                profile.getHackathonsWon(),
                profile.getCollege(),
                profile.getYear(),
                profile.getGithubProfile(),
                profile.getLinkedinProfile(),
                profile.getPortfolioUrl(),
                profile.getAvatarId(),
                profile.getMainSkill(),
                profile.getAverageRating(),
                profile.getTotalReviews(),
                reviewDtos
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PublicProfileResponseDto getPublicProfile(Long profileUserId, Long viewerId) {
        log.info("Fetching public profile for user: {} viewed by: {}", profileUserId, viewerId);

        User profileUser = userRepository.findById(profileUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Profile profile = profileRepository.findByUserId(profileUserId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        List<Review> publicReviews = reviewRepository.findPublicReviewsByUser(profileUserId)
                .stream()
                .limit(10)
                .toList();

        List<ReviewDto> reviewDtos = publicReviews.stream()
                .map(this::convertToReviewDto)
                .toList();

        return new PublicProfileResponseDto(
                profileUser.getId(),
                profile.getFullName(),
                profile.getBio(),
                profile.getHackathonsParticipated(),
                profile.getHackathonsWon(),
                profile.getCollege(),
                profile.getYear(),
                profile.getGithubProfile(),
                profile.getLinkedinProfile(),
                profile.getPortfolioUrl(),
                profile.getAvatarId(),
                profile.getMainSkill(),
                profile.getAverageRating(),
                profile.getTotalReviews(),
                reviewDtos,
                profile.getBadges(),
                profileUser.getCreatedAt()
        );
    }

    @Override
    public AddReviewResponseDto addReview(AddReviewRequestDto request, Long reviewerId) {
        log.info("Adding review for user: {} by reviewer: {}", request.getUserIdToReview(), reviewerId);

        User reviewer = userRepository.findById(reviewerId)
                .orElseThrow(() -> new RuntimeException("Reviewer not found"));

        User reviewedUser = userRepository.findById(request.getUserIdToReview())
                .orElseThrow(() -> new RuntimeException("User to review not found"));

        // Check if reviewer has already reviewed this user
        if (reviewRepository.hasReviewedUser(reviewerId, request.getUserIdToReview())) {
            throw new RuntimeException("You have already reviewed this user");
        }

        // Cannot review yourself
        if (reviewerId.equals(request.getUserIdToReview())) {
            throw new RuntimeException("You cannot review yourself");
        }

        // Create review
        Review review = new Review();
        review.setReviewer(reviewer);

        // Get the profile of the user being reviewed
        Profile reviewedUserProfile = profileRepository.findByUserId(request.getUserIdToReview())
                .orElseThrow(() -> new RuntimeException("Reviewed user profile not found"));

        review.setReviewedProfile(reviewedUserProfile);
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setPublic(true); // Default to public

        // Set hackathon if provided
        if (request.getHackathonId() != null) {
            // In a real implementation, you'd fetch and set the hackathon
            // review.setHackathon(hackathon);
        }

        Review savedReview = reviewRepository.save(review);

        // Update user's average rating (we already have the profile)
        reviewedUserProfile.updateRating(request.getRating());
        profileRepository.save(reviewedUserProfile);

        log.info("Review added with ID: {} for user: {}", savedReview.getId(), request.getUserIdToReview());

        return new AddReviewResponseDto(
                true,
                "Review added successfully",
                savedReview.getId(),
                savedReview.getCreatedAt()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Profile> findByUserId(Long userId) {
        return profileRepository.findByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isProfileSetupComplete(Long userId) {
        return profileRepository.findByUserId(userId).isPresent();
    }

    @Override
    @Transactional(readOnly = true)
    public int calculateProfileCompletion(Long userId) {
        Profile profile = profileRepository.findByUserId(userId).orElse(null);
        if (profile == null) {
            return 0;
        }
        return profile.calculateCompletionPercentage();
    }

    // Helper methods
    private ReviewDto convertToReviewDto(Review review) {
        Profile reviewerProfile = profileRepository.findByUserId(review.getReviewer().getId()).orElse(null);
        String reviewerName = reviewerProfile != null ? reviewerProfile.getFullName() : review.getReviewer().getEmail();
        String reviewerAvatarId = reviewerProfile != null ? reviewerProfile.getAvatarId() : null;
        String hackathonName = review.getHackathon() != null ? review.getHackathon().getTitle() : null;

        return new ReviewDto(
                review.getId(),
                reviewerName,
                reviewerAvatarId,
                review.getRating(),
                review.getComment(),
                hackathonName,
                review.getCreatedAt()
        );
    }
}