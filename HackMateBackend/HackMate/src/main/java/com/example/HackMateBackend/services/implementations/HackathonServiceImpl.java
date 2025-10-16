package com.example.HackMateBackend.services.implementations;

import com.example.HackMateBackend.data.entities.*;
import com.example.HackMateBackend.data.enums.RegistrationStatus;
import com.example.HackMateBackend.data.enums.Status;
import com.example.HackMateBackend.dtos.HackathonDto.*;
import com.example.HackMateBackend.repositories.*;
import com.example.HackMateBackend.services.interfaces.HackathonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class HackathonServiceImpl implements HackathonService {

    private final HackathonRepository hackathonRepository;
    private final UserRepository userRepository;
    private final HackathonRegistrationRepository registrationRepository;
    private final TeamRepository teamRepository;
    private final ProfileRepository profileRepository;

    @Override
    @Transactional(readOnly = true)
    public HackathonListResponseDto getPublicHackathonFeed(HackathonFilterRequestDto filterRequest) {
        log.info("Fetching public hackathon feed with filters: {}", filterRequest);

        Sort sort = createSortFromRequest(filterRequest.getSortBy(), filterRequest.getSortDirection());
        Pageable pageable = PageRequest.of(filterRequest.getPage(), filterRequest.getSize(), sort);

        Page<Hackathon> hackathonPage;

        if (!filterRequest.isShowExpired()) {
            hackathonPage = hackathonRepository.findActiveHackathons(LocalDateTime.now(), pageable);
        } else {
            hackathonPage = hackathonRepository.findApprovedHackathons(pageable);
        }

        if (filterRequest.getSearch() != null && !filterRequest.getSearch().trim().isEmpty()) {
            hackathonPage = hackathonRepository.searchHackathons(filterRequest.getSearch().trim(), pageable);
        }

        List<HackathonListItemDto> hackathonDtos = hackathonPage.getContent().stream()
                .map(this::convertToListItemDto)
                .toList();

        return new HackathonListResponseDto(
                hackathonDtos,
                hackathonPage.getNumber(),
                hackathonPage.getTotalPages(),
                hackathonPage.getTotalElements(),
                hackathonPage.hasNext(),
                hackathonPage.hasPrevious()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public HackathonDetailsResponseDto getHackathonDetails(Long hackathonId, Long userId) {
        log.info("Fetching hackathon details for ID: {} by user: {}", hackathonId, userId);

        Hackathon hackathon = hackathonRepository.findApprovedById(hackathonId)
                .orElseThrow(() -> new RuntimeException("Hackathon not found or not approved"));

        hackathonRepository.incrementViewCount(hackathonId);

        boolean isRegistered = false;
        boolean isStarred = false;

        if (userId != null) {
            Optional<HackathonRegistration> registration = registrationRepository
                    .findByHackathonIdAndUserId(hackathonId, userId);

            if (registration.isPresent()) {
                RegistrationStatus status = registration.get().getStatus();
                isRegistered = status == RegistrationStatus.REGISTERED;
                isStarred = status == RegistrationStatus.STARRED;
            }
        }

        return convertToDetailsDto(hackathon, isRegistered, isStarred);
    }

    @Override
    public CreateHackathonResponseDto createHackathon(CreateHackathonRequestDto request, Long userId) {
        log.info("Creating hackathon by user: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Hackathon hackathon = new Hackathon();
        hackathon.setTitle(request.getTitle());
        hackathon.setDescription(request.getDescription());
        hackathon.setRegistrationLink(request.getRegistrationLink());
        hackathon.setDeadline(request.getDeadline());
        hackathon.setPosterUrl(request.getPosterUrl());
        hackathon.setTags(request.getTags() != null ? request.getTags() : new ArrayList<>());
        hackathon.setOrganizer(request.getOrganizer());
        hackathon.setLocation(request.getLocation());
        hackathon.setPrizePool(request.getPrizePool());
        hackathon.setEventStartDate(request.getEventStartDate());
        hackathon.setEventEndDate(request.getEventEndDate());
        hackathon.setMaxTeamSize(request.getMaxTeamSize());
        hackathon.setMinTeamSize(request.getMinTeamSize());
        hackathon.setContactEmail(request.getContactEmail());
        hackathon.setOriginalMessage(request.getOriginalMessage());
        hackathon.setPostedBy(user);
        hackathon.setStatus(Status.PENDING);

        Hackathon savedHackathon = hackathonRepository.save(hackathon);

        log.info("Hackathon created with ID: {} by user: {}", savedHackathon.getId(), userId);

        return new CreateHackathonResponseDto(
                true,
                "Hackathon submitted successfully. It will be reviewed by admins.",
                savedHackathon.getId(),
                savedHackathon.getStatus(),
                savedHackathon.getCreatedAt()
        );
    }

    @Override
    public RegistrationToggleResponseDto toggleRegistration(RegistrationToggleRequestDto request, Long userId) {
        log.info("Toggling registration for hackathon: {} by user: {}", request.getHackathonId(), userId);

        Hackathon hackathon = hackathonRepository.findApprovedById(request.getHackathonId())
                .orElseThrow(() -> new RuntimeException("Hackathon not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<HackathonRegistration> existingRegistration =
                registrationRepository.findByHackathonAndUserAndDeletedFalse(hackathon, user);

        if (request.isRegister()) {
            if (existingRegistration.isPresent()) {
                HackathonRegistration registration = existingRegistration.get();
                if (registration.getStatus() != RegistrationStatus.REGISTERED) {
                    registration.setStatus(RegistrationStatus.REGISTERED);
                    registrationRepository.save(registration);
                    hackathonRepository.incrementRegistrationCount(hackathon.getId());
                }
            } else {
                HackathonRegistration registration = HackathonRegistration.createRegistration(hackathon, user);
                registrationRepository.save(registration);
                hackathonRepository.incrementRegistrationCount(hackathon.getId());
            }

            return new RegistrationToggleResponseDto(
                    true,
                    "Successfully registered for hackathon",
                    true,
                    LocalDateTime.now()
            );
        } else {
            if (existingRegistration.isPresent() &&
                    existingRegistration.get().getStatus() == RegistrationStatus.REGISTERED) {
                registrationRepository.delete(existingRegistration.get());
                hackathonRepository.decrementRegistrationCount(hackathon.getId());
            }

            return new RegistrationToggleResponseDto(
                    true,
                    "Successfully unregistered from hackathon",
                    false,
                    LocalDateTime.now()
            );
        }
    }

    @Override
    public StarToggleResponseDto toggleStar(StarToggleRequestDto request, Long userId) {
        log.info("Toggling star for hackathon: {} by user: {}", request.getHackathonId(), userId);

        Hackathon hackathon = hackathonRepository.findApprovedById(request.getHackathonId())
                .orElseThrow(() -> new RuntimeException("Hackathon not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<HackathonRegistration> existingRegistration =
                registrationRepository.findByHackathonAndUserAndDeletedFalse(hackathon, user);

        if (request.isStar()) {
            // Star hackathon
            if (existingRegistration.isPresent()) {
                HackathonRegistration registration = existingRegistration.get();
                if (registration.getStatus() != RegistrationStatus.STARRED) {
                    registration.setStatus(RegistrationStatus.STARRED);
                    registrationRepository.save(registration);
                }
            } else {
                HackathonRegistration registration = HackathonRegistration.createStarred(hackathon, user);
                registrationRepository.save(registration);
            }

            return new StarToggleResponseDto(
                    true,
                    "Hackathon starred successfully",
                    true,
                    LocalDateTime.now()
            );
        } else {
            // Unstar hackathon
            if (existingRegistration.isPresent() &&
                    existingRegistration.get().getStatus() == RegistrationStatus.STARRED) {
                registrationRepository.delete(existingRegistration.get());
            }

            return new StarToggleResponseDto(
                    true,
                    "Hackathon unstarred successfully",
                    false,
                    LocalDateTime.now()
            );
        }
    }

    @Override
    @Transactional(readOnly = true)
    public HackathonListResponseDto getUserRegisteredHackathons(Long userId, Pageable pageable) {
        log.info("Fetching registered hackathons for user: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Hackathon> registeredHackathons = hackathonRepository.findRegisteredHackathonsByUser(userId);

        List<HackathonListItemDto> hackathonDtos = registeredHackathons.stream()
                .map(hackathon -> convertToListItemDto(hackathon, true, false))
                .toList();

        return new HackathonListResponseDto(
                hackathonDtos,
                0, 1, hackathonDtos.size(),
                false, false
        );
    }

    @Override
    @Transactional(readOnly = true)
    public HackathonListResponseDto getUserStarredHackathons(Long userId, Pageable pageable) {
        log.info("Fetching starred hackathons for user: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Hackathon> starredHackathons = hackathonRepository.findStarredHackathonsByUser(userId);

        List<HackathonListItemDto> hackathonDtos = starredHackathons.stream()
                .map(hackathon -> convertToListItemDto(hackathon, false, true))
                .toList();

        return new HackathonListResponseDto(
                hackathonDtos,
                0, 1, hackathonDtos.size(),
                false, false
        );
    }

    @Override
    @Transactional(readOnly = true)
    public HackathonListResponseDto getPendingHackathons() {
        log.info("Fetching pending hackathons for admin review");

        List<Hackathon> pendingHackathons = hackathonRepository.findPendingHackathons();

        List<HackathonListItemDto> hackathonDtos = pendingHackathons.stream()
                .map(this::convertToListItemDto)
                .toList();

        return new HackathonListResponseDto(
                hackathonDtos,
                0, 1, hackathonDtos.size(),
                false, false
        );
    }

    @Override
    public CreateHackathonResponseDto approveHackathon(Long hackathonId, Long adminId) {
        log.info("Approving hackathon: {} by admin: {}", hackathonId, adminId);

        Hackathon hackathon = hackathonRepository.findById(hackathonId)
                .orElseThrow(() -> new RuntimeException("Hackathon not found"));

        if (hackathon.getStatus() != Status.PENDING) {
            throw new RuntimeException("Hackathon is not in pending state");
        }

        hackathon.approve(adminId);
        hackathonRepository.save(hackathon);

        log.info("Hackathon approved: {}", hackathonId);

        return new CreateHackathonResponseDto(
                true,
                "Hackathon approved successfully",
                hackathon.getId(),
                hackathon.getStatus(),
                hackathon.getApprovedAt()
        );
    }

    @Override
    public CreateHackathonResponseDto rejectHackathon(Long hackathonId, Long adminId) {
        log.info("Rejecting hackathon: {} by admin: {}", hackathonId, adminId);

        Hackathon hackathon = hackathonRepository.findById(hackathonId)
                .orElseThrow(() -> new RuntimeException("Hackathon not found"));

        if (hackathon.getStatus() != Status.PENDING) {
            throw new RuntimeException("Hackathon is not in pending state");
        }

        hackathon.reject();
        hackathonRepository.save(hackathon);

        log.info("Hackathon rejected: {}", hackathonId);

        return new CreateHackathonResponseDto(
                true,
                "Hackathon rejected",
                hackathon.getId(),
                hackathon.getStatus(),
                LocalDateTime.now()
        );
    }

    @Override
    public AIExtractionResponseDto extractHackathonData(AIExtractionRequestDto request) {
        log.info("Extracting hackathon data from message text");

        try {
            // Simple AI extraction using regex patterns
            String text = request.getMessageText();

            String title = extractTitle(text);
            String description = extractDescription(text);
            String registrationLink = extractRegistrationLink(text);
            LocalDateTime deadline = extractDeadline(text);
            List<String> tags = extractTags(text);
            String organizer = extractOrganizer(text);
            String location = extractLocation(text);
            String prizePool = extractPrizePool(text);

            double confidence = calculateConfidence(title, description, registrationLink, deadline);

            return new AIExtractionResponseDto(
                    true, title, description, registrationLink, deadline,
                    tags, organizer, location, prizePool, confidence, null
            );
        } catch (Exception e) {
            log.error("Error extracting hackathon data", e);
            return new AIExtractionResponseDto(
                    false, null, null, null, null,
                    null, null, null, null, 0.0, e.getMessage()
            );
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Hackathon> findById(Long id) {
        return hackathonRepository.findById(id);
    }

    @Override
    public void incrementViewCount(Long hackathonId) {
        hackathonRepository.incrementViewCount(hackathonId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isUserRegistered(Long hackathonId, Long userId) {
        return registrationRepository.existsByHackathonAndUserAndStatus(
                hackathonId, userId, RegistrationStatus.REGISTERED);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isUserStarred(Long hackathonId, Long userId) {
        return registrationRepository.existsByHackathonAndUserAndStatus(
                hackathonId, userId, RegistrationStatus.STARRED);
    }

    // Helper methods
    private Sort createSortFromRequest(String sortBy, String sortDirection) {
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDirection) ?
                Sort.Direction.DESC : Sort.Direction.ASC;

        return switch (sortBy) {
            case "deadline" -> Sort.by(direction, "deadline");
            case "title" -> Sort.by(direction, "title");
            case "createdAt" -> Sort.by(direction, "createdAt");
            case "viewCount" -> Sort.by(direction, "viewCount");
            default -> Sort.by(Sort.Direction.ASC, "deadline");
        };
    }

    private HackathonListItemDto convertToListItemDto(Hackathon hackathon) {
        return convertToListItemDto(hackathon, false, false);
    }

    private HackathonListItemDto convertToListItemDto(Hackathon hackathon, boolean isRegistered, boolean isStarred) {
        return new HackathonListItemDto(
                hackathon.getId(),
                hackathon.getTitle(),
                hackathon.getDescription(),
                hackathon.getPosterUrl(),
                hackathon.getTags(),
                hackathon.getOrganizer(),
                hackathon.getLocation(),
                hackathon.getDeadline(),
                hackathon.getPrizePool(),
                hackathon.getViewCount(),
                hackathon.getRegistrationCount(),
                hackathon.getTeamCount(),
                isRegistered,
                isStarred,
                hackathon.getUrgencyLevel(),
                hackathon.getCreatedAt()
        );
    }

    private HackathonDetailsResponseDto convertToDetailsDto(Hackathon hackathon, boolean isRegistered, boolean isStarred) {
        return new HackathonDetailsResponseDto(
                hackathon.getId(),
                hackathon.getTitle(),
                hackathon.getDescription(),
                hackathon.getRegistrationLink(),
                hackathon.getPosterUrl(),
                hackathon.getTags(),
                hackathon.getOrganizer(),
                hackathon.getLocation(),
                hackathon.getPrizePool(),
                hackathon.getDeadline(),
                hackathon.getEventStartDate(),
                hackathon.getEventEndDate(),
                hackathon.getMaxTeamSize(),
                hackathon.getMinTeamSize(),
                hackathon.getContactEmail(),
                hackathon.getViewCount(),
                hackathon.getRegistrationCount(),
                hackathon.getTeamCount(),
                hackathon.getStatus().name(),
                isRegistered,
                isStarred,
                hackathon.isExpired(),
                hackathon.getUrgencyLevel(),
                hackathon.getPostedBy().getEmail(),
                hackathon.getCreatedAt(),
                hackathon.getApprovedAt()
        );
    }

    // AI Extraction Helper Methods
    private String extractTitle(String text) {
        // Look for patterns like "Hackathon: Title" or lines that look like titles
        Pattern titlePattern = Pattern.compile("(?i)(?:hackathon[:\\s]*)?([A-Z][\\w\\s]{5,50})",
                Pattern.MULTILINE);
        Matcher matcher = titlePattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return null;
    }

    private String extractDescription(String text) {
        // Return the first 200 characters as description
        if (text.length() > 200) {
            return text.substring(0, 200) + "...";
        }
        return text;
    }

    private String extractRegistrationLink(String text) {
        // Look for URLs
        Pattern urlPattern = Pattern.compile("https?://[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&:/~\\+#]*[\\w\\-\\@?^=%&/~\\+#])?");
        Matcher matcher = urlPattern.matcher(text);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    private LocalDateTime extractDeadline(String text) {
        // Look for date patterns
        Pattern datePattern = Pattern.compile("(?i)(?:deadline|due|submit|end)[:\\s]*([\\d]{1,2}[/-][\\d]{1,2}[/-][\\d]{2,4})");
        Matcher matcher = datePattern.matcher(text);
        if (matcher.find()) {
            try {
                // Simple date parsing - in production, use proper date parsing library
                String dateStr = matcher.group(1);
                // Return a placeholder deadline (7 days from now)
                return LocalDateTime.now().plusDays(7);
            } catch (Exception e) {
                log.warn("Could not parse date: {}", matcher.group(1));
            }
        }
        return LocalDateTime.now().plusDays(30); // Default to 30 days
    }

    private List<String> extractTags(String text) {
        List<String> tags = new ArrayList<>();
        String lowerText = text.toLowerCase();

        if (lowerText.contains("ai") || lowerText.contains("artificial intelligence")) tags.add("AI");
        if (lowerText.contains("web") || lowerText.contains("website")) tags.add("Web Development");
        if (lowerText.contains("mobile") || lowerText.contains("app")) tags.add("Mobile");
        if (lowerText.contains("blockchain")) tags.add("Blockchain");
        if (lowerText.contains("ml") || lowerText.contains("machine learning")) tags.add("Machine Learning");
        if (lowerText.contains("iot")) tags.add("IoT");

        return tags;
    }

    private String extractOrganizer(String text) {
        // Look for patterns like "organized by", "by", etc.
        Pattern organizerPattern = Pattern.compile("(?i)(?:organized by|by|host[ed]* by)[:\\s]*([A-Za-z\\s]{3,50})");
        Matcher matcher = organizerPattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return null;
    }

    private String extractLocation(String text) {
        Pattern locationPattern = Pattern.compile("(?i)(?:location|venue|at)[:\\s]*([A-Za-z\\s,]{3,50})");
        Matcher matcher = locationPattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        if (text.toLowerCase().contains("online") || text.toLowerCase().contains("virtual")) {
            return "Online";
        }
        return null;
    }

    private String extractPrizePool(String text) {
        Pattern prizePattern = Pattern.compile("(?i)(?:prize|reward|cash)[:\\s]*([\\$₹€£]?[\\d,]+[\\$₹€£]?)");
        Matcher matcher = prizePattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return null;
    }

    private double calculateConfidence(String title, String description, String registrationLink, LocalDateTime deadline) {
        int score = 0;
        if (title != null && !title.isEmpty()) score += 25;
        if (description != null && !description.isEmpty()) score += 25;
        if (registrationLink != null && !registrationLink.isEmpty()) score += 30;
        if (deadline != null) score += 20;

        return score / 100.0;
    }
}