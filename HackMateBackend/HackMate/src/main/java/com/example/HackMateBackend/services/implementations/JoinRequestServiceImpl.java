package com.example.HackMateBackend.services.implementations;

import com.example.HackMateBackend.data.entities.*;
import com.example.HackMateBackend.data.enums.Status;
import com.example.HackMateBackend.dtos.joinrequest.*;
import com.example.HackMateBackend.repositories.*;
import com.example.HackMateBackend.services.interfaces.JoinRequestService;
import com.example.HackMateBackend.services.interfaces.NotificationService;
import com.example.HackMateBackend.services.interfaces.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class JoinRequestServiceImpl implements JoinRequestService {

    private final JoinRequestRepository joinRequestRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final ProfileRepository profileRepository;
    private final NotificationService notificationService;
    private final EmailService emailService;

    @Override
    public SendJoinRequestResponseDto sendJoinRequest(SendJoinRequestDto request, Long userId) {
        log.info("Sending join request for team: {} by user: {}", request.getTeamId(), userId);

        // Validate user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Validate team
        Team team = teamRepository.findByIdAndNotDeleted(request.getTeamId())
                .orElseThrow(() -> new RuntimeException("Team not found"));

        // Check if user already has an active join request
        if (hasActiveJoinRequest(userId)) {
            throw new RuntimeException("You already have an active join request. Please wait for it to be processed or cancel it first.");
        }

        // Check if user can send join request to this team
        if (!canSendJoinRequest(userId, request.getTeamId())) {
            throw new RuntimeException("You cannot send a join request to this team");
        }

        // Check if team is full or closed
        if (team.isFull() || !team.isActive()) {
            throw new RuntimeException("This team is not accepting new members");
        }

        // Check if user is already in a team for this hackathon
        if (teamRepository.isUserInAnyTeamForHackathon(userId, team.getHackathon().getId())) {
            throw new RuntimeException("You are already part of a team for this hackathon");
        }

        // Create join request
        JoinRequest joinRequest = new JoinRequest();
        joinRequest.setTeam(team);
        joinRequest.setRequester(user);
        joinRequest.setRequestedRole(request.getRequestedRole());
        joinRequest.setUserSkills(request.getUserSkills() != null ? request.getUserSkills() : new ArrayList<String>());
        joinRequest.setMessage(request.getMessage());
        joinRequest.setStatus(Status.PENDING);
        // Expiry is set in @PrePersist

        JoinRequest savedRequest = joinRequestRepository.save(joinRequest);

        // Create notification for team leader
        notificationService.createJoinRequestNotification(savedRequest);

        log.info("Join request created with ID: {} for team: {}", savedRequest.getId(), team.getId());

        return new SendJoinRequestResponseDto(
                true,
                "Join request sent successfully",
                savedRequest.getId(),
                team.getTeamName(),
                savedRequest.getStatus().name(),
                savedRequest.getCreatedAt(),
                savedRequest.getExpiresAt()
        );
    }

    @Override
    public ProcessJoinRequestResponseDto processJoinRequest(ProcessJoinRequestDto request, Long leaderId) {
        log.info("Processing join request: {} by leader: {}", request.getJoinRequestId(), leaderId);

        JoinRequest joinRequest = joinRequestRepository.findByIdAndNotDeleted(request.getJoinRequestId())
                .orElseThrow(() -> new RuntimeException("Join request not found"));

        // Verify that the user is the team leader
        if (!joinRequestRepository.canLeaderAccessRequest(request.getJoinRequestId(), leaderId)) {
            throw new RuntimeException("You are not authorized to process this join request");
        }

        // Check if request is still pending and not expired
        if (joinRequest.getStatus() != Status.PENDING) {
            throw new RuntimeException("Join request has already been processed");
        }

        if (joinRequest.isExpired()) {
            throw new RuntimeException("Join request has expired");
        }

        Team team = joinRequest.getTeam();
        User requester = joinRequest.getRequester();

        boolean isAccepted = "ACCEPT".equalsIgnoreCase(request.getAction());

        if (isAccepted) {
            // Check if team still has space
            if (team.isFull()) {
                throw new RuntimeException("Team is now full");
            }

            // Check if user is still available (not joined another team)
            if (teamRepository.isUserInAnyTeamForHackathon(requester.getId(), team.getHackathon().getId())) {
                throw new RuntimeException("User has already joined another team for this hackathon");
            }

            // Accept the request
            joinRequest.accept(leaderId, request.getResponseMessage());

            // Add user to team
            team.addMember(requester, joinRequest.getRequestedRole());
            teamRepository.save(team);

            // Send email notification to new member
            sendTeamJoinedEmail(requester, team);

            log.info("Join request accepted: {} for team: {}", joinRequest.getId(), team.getId());
        } else {
            // Reject the request
            joinRequest.reject(leaderId, request.getResponseMessage());
            log.info("Join request rejected: {}", joinRequest.getId());
        }

        joinRequestRepository.save(joinRequest);

        // Create notification for requester
        notificationService.createJoinRequestResponseNotification(joinRequest, isAccepted);

        // Get requester profile for name
        Profile requesterProfile = profileRepository.findByUserId(requester.getId()).orElse(null);
        String requesterName = requesterProfile != null ? requesterProfile.getFullName() : requester.getEmail();

        return new ProcessJoinRequestResponseDto(
                true,
                String.format("Join request %s successfully", isAccepted ? "accepted" : "rejected"),
                isAccepted ? "ACCEPTED" : "REJECTED",
                requesterName,
                team.getTeamName(),
                joinRequest.getProcessedAt()
        );
    }

    @Override
    public CancelJoinRequestResponseDto cancelJoinRequest(CancelJoinRequestDto request, Long userId) {
        log.info("Cancelling join request: {} by user: {}", request.getJoinRequestId(), userId);

        JoinRequest joinRequest = joinRequestRepository.findByIdAndNotDeleted(request.getJoinRequestId())
                .orElseThrow(() -> new RuntimeException("Join request not found"));

        // Verify that the user owns this join request
        if (!joinRequestRepository.canUserAccessRequest(request.getJoinRequestId(), userId)) {
            throw new RuntimeException("You are not authorized to cancel this join request");
        }

        // Check if request can be cancelled
        if (!joinRequest.canCancel()) {
            throw new RuntimeException("This join request cannot be cancelled");
        }

        // Cancel the request by marking as rejected
        joinRequest.reject(userId, "Cancelled by user");
        joinRequestRepository.save(joinRequest);

        log.info("Join request cancelled: {}", joinRequest.getId());

        return new CancelJoinRequestResponseDto(
                true,
                "Join request cancelled successfully",
                LocalDateTime.now()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public MyJoinRequestsResponseDto getMyJoinRequests(Long userId) {
        log.info("Fetching join requests for user: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<JoinRequest> allRequests = joinRequestRepository.findByRequesterAndDeletedFalseOrderByCreatedAtDesc(user);

        // Separate active and past requests
        List<MyJoinRequestItemDto> activeRequests = new ArrayList<>();
        List<MyJoinRequestItemDto> pastRequests = new ArrayList<>();

        for (JoinRequest request : allRequests) {
            MyJoinRequestItemDto dto = convertToMyJoinRequestItemDto(request);

            if (request.getStatus() == Status.PENDING && !request.isExpired()) {
                activeRequests.add(dto);
            } else {
                pastRequests.add(dto);
            }
        }

        boolean hasActiveRequest = !activeRequests.isEmpty();
        String nextAvailableTime = hasActiveRequest ? "You have an active request pending" : null;

        return new MyJoinRequestsResponseDto(
                activeRequests,
                pastRequests,
                hasActiveRequest,
                nextAvailableTime
        );
    }

    @Override
    @Transactional(readOnly = true)
    public TeamJoinRequestsResponseDto getTeamJoinRequests(Long teamId, Long leaderId) {
        log.info("Fetching join requests for team: {} by leader: {}", teamId, leaderId);

        Team team = teamRepository.findByIdAndNotDeleted(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        // Verify that the user is the team leader
        if (!team.isUserLeader(leaderId)) {
            throw new RuntimeException("You are not authorized to view these join requests");
        }

        LocalDateTime now = LocalDateTime.now();
        List<JoinRequest> pendingRequests = joinRequestRepository.findPendingJoinRequestsByTeam(teamId, now);
        List<JoinRequest> processedRequests = joinRequestRepository.findProcessedJoinRequestsByTeam(teamId);

        List<JoinRequestListItemDto> pendingDtos = pendingRequests.stream()
                .map(this::convertToListItemDto)
                .toList();

        List<JoinRequestListItemDto> processedDtos = processedRequests.stream()
                .map(this::convertToListItemDto)
                .toList();

        return new TeamJoinRequestsResponseDto(
                pendingDtos,
                processedDtos,
                pendingDtos.size(),
                processedDtos.size(),
                team.getTeamName(),
                team.getAvailableSlots()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public JoinRequestStatsDto getJoinRequestStats(Long userId) {
        log.info("Fetching join request statistics for user: {}", userId);

        long totalSent = joinRequestRepository.countTotalRequestsByUser(userId);
        long totalAccepted = joinRequestRepository.countRequestsByUserAndStatus(userId, Status.ACCEPTED);
        long totalRejected = joinRequestRepository.countRequestsByUserAndStatus(userId, Status.REJECTED);
        long totalExpired = 0; // Would need additional query for expired requests
        long currentPending = joinRequestRepository.countRequestsByUserAndStatus(userId, Status.PENDING);

        double acceptanceRate = totalSent > 0 ? (double) totalAccepted / totalSent * 100 : 0.0;

        // Get first and last request dates (mock implementation)
        LocalDateTime firstRequest = LocalDateTime.now().minusDays(30); // Mock
        LocalDateTime lastRequest = LocalDateTime.now(); // Mock

        return new JoinRequestStatsDto(
                (int) totalSent,
                (int) totalAccepted,
                (int) totalRejected,
                (int) totalExpired,
                (int) currentPending,
                acceptanceRate,
                firstRequest,
                lastRequest
        );
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasActiveJoinRequest(Long userId) {
        return joinRequestRepository.hasActiveJoinRequest(userId, LocalDateTime.now());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canSendJoinRequest(Long userId, Long teamId) {
        // Check if user already has an active request for this team
        if (joinRequestRepository.hasActiveRequestForTeam(userId, teamId, LocalDateTime.now())) {
            return false;
        }

        // Check if user is already a member of this team
        Team team = teamRepository.findByIdAndNotDeleted(teamId).orElse(null);
        if (team != null && team.isUserMember(userId)) {
            return false;
        }

        return true;
    }

    @Override
    public void expireOldRequests() {
        log.info("Expiring old join requests");

        int expiredCount = joinRequestRepository.markExpiredRequestsAsRejected(LocalDateTime.now());
        log.info("Expired {} join requests", expiredCount);
    }

    // Helper methods
    private MyJoinRequestItemDto convertToMyJoinRequestItemDto(JoinRequest request) {
        Team team = request.getTeam();

        return new MyJoinRequestItemDto(
                request.getId(),
                team.getId(),
                team.getTeamName(),
                team.getHackathon().getTitle(),
                request.getRequestedRole(),
                request.getUserSkills(),
                request.getMessage(),
                request.getStatus().name(),
                request.getResponseMessage(),
                request.getCreatedAt(),
                request.getExpiresAt(),
                request.getProcessedAt(),
                request.canCancel(),
                request.isExpired()
        );
    }

    private JoinRequestListItemDto convertToListItemDto(JoinRequest request) {
        User requester = request.getRequester();
        Profile requesterProfile = profileRepository.findByUserId(requester.getId()).orElse(null);

        List<String> matchingSkills = request.getMatchingSkills();
        int matchPercentage = request.getMatchPercentage();

        return new JoinRequestListItemDto(
                request.getId(),
                request.getTeam().getId(),
                request.getTeam().getTeamName(),
                request.getTeam().getHackathon().getTitle(),
                requester.getId(),
                requesterProfile != null ? requesterProfile.getFullName() : requester.getEmail(),
                requesterProfile != null ? requesterProfile.getAvatarId() : null,
                requesterProfile != null ? requesterProfile.getCollege() : null,
                requesterProfile != null ? requesterProfile.getYear() : null,
                request.getRequestedRole(),
                request.getUserSkills(),
                request.getMessage(),
                matchingSkills,
                matchPercentage,
                request.getStatus().name(),
                request.getCreatedAt(),
                request.getExpiresAt(),
                request.getProcessedAt(),
                request.isExpired()
        );
    }

    private void sendTeamJoinedEmail(User user, Team team) {
        try {
            Profile userProfile = profileRepository.findByUserId(user.getId()).orElse(null);
            String userName = userProfile != null ? userProfile.getFullName() : user.getEmail();

            String subject = String.format("Welcome to Team %s!", team.getTeamName());
            String message = buildTeamJoinedEmailMessage(userName, team);

            emailService.sendNotificationEmail(user.getEmail(), subject, message);

            log.info("Team joined email sent to: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send team joined email to: {}", user.getEmail(), e);
        }
    }

    private String buildTeamJoinedEmailMessage(String userName, Team team) {
        return String.format(
                "Hello %s,\n\n" +
                        "Congratulations! You have successfully joined team '%s' for the hackathon '%s'.\n\n" +
                        "Team Details:\n" +
                        "- Team Name: %s\n" +
                        "- Team Leader: %s\n" +
                        "- Hackathon: %s\n" +
                        "- Deadline: %s\n" +
                        "- Team Size: %d/%d\n\n" +
                        "You can now access team contact information and coordinate with your teammates.\n\n" +
                        "Best of luck with your hackathon!\n\n" +
                        "The HackMate Team",
                userName,
                team.getTeamName(),
                team.getHackathon().getTitle(),
                team.getTeamName(),
                team.getLeader().getEmail(), // You might want to get leader's full name from profile
                team.getHackathon().getTitle(),
                team.getHackathon().getDeadline().toString(),
                team.getCurrentSize(),
                team.getMaxSize()
        );
    }
}