package com.example.HackMateBackend.services.implementations;

import com.example.HackMateBackend.data.entities.*;
import com.example.HackMateBackend.data.enums.TeamRole;
import com.example.HackMateBackend.dtos.team.*;
import com.example.HackMateBackend.repositories.*;
import com.example.HackMateBackend.services.interfaces.TeamService;
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

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final HackathonRepository hackathonRepository;
    private final ProfileRepository profileRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final JoinRequestRepository joinRequestRepository;

    @Override
    public CreateTeamResponseDto createTeam(CreateTeamRequestDto request, Long userId) {
        log.info("Creating team for hackathon: {} by user: {}", request.getHackathonId(), userId);

        // Validate user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Validate hackathon
        Hackathon hackathon = hackathonRepository.findApprovedById(request.getHackathonId())
                .orElseThrow(() -> new RuntimeException("Hackathon not found or not approved"));

        // Check if user already has a team for this hackathon
        if (teamRepository.isUserInAnyTeamForHackathon(userId, request.getHackathonId())) {
            throw new RuntimeException("You already have a team for this hackathon");
        }

        // Create team
        Team team = new Team();
        team.setTeamName(request.getTeamName());
        team.setDescription(request.getDescription());
        team.setMaxSize(request.getMaxSize());
        team.setCurrentSize(1); // Leader is first member
        team.setRolesNeeded(request.getRolesNeeded() != null ? request.getRolesNeeded() : new ArrayList<>());
        team.setSkillsNeeded(request.getSkillsNeeded() != null ? request.getSkillsNeeded() : new ArrayList<>());
        team.setContactEmail(request.getContactEmail());
        team.setContactPhone(request.getContactPhone());
        team.setDiscordServer(request.getDiscordServer());
        team.setWhatsappGroup(request.getWhatsappGroup());
        team.setLinkedinGroup(request.getLinkedinGroup());
        team.setExternalGroupLink(request.getExternalGroupLink());
        team.setPublic(request.isPublic());
        team.setAutoAccept(request.isAutoAccept());
        team.setHackathon(hackathon);
        team.setLeader(user);

        Team savedTeam = teamRepository.save(team);

        // Create team member entry for leader
        TeamMember leaderMember = new TeamMember();
        leaderMember.setTeam(savedTeam);
        leaderMember.setUser(user);
        leaderMember.setRole(TeamRole.LEADER);
        leaderMember.setAssignedRole("Leader");
        leaderMember.setActive(true);
        teamMemberRepository.save(leaderMember);

        // Update hackathon team count
        hackathonRepository.incrementTeamCount(hackathon.getId());

        log.info("Team created with ID: {} for hackathon: {}", savedTeam.getId(), hackathon.getId());

        return new CreateTeamResponseDto(
                true,
                "Team created successfully",
                savedTeam.getId(),
                savedTeam.getTeamName(),
                savedTeam.getCreatedAt()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public TeamDetailsResponseDto getTeamDetails(Long teamId, Long userId) {
        log.info("Fetching team details for team: {} by user: {}", teamId, userId);

        Team team = teamRepository.findByIdAndNotDeleted(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        // Check if user can view this team
        if (!team.isPublic() && !team.isUserMember(userId) && !team.isUserLeader(userId)) {
            throw new RuntimeException("Access denied to private team");
        }

        boolean isUserMember = team.isUserMember(userId);
        boolean isUserLeader = team.isUserLeader(userId);
        boolean hasPendingRequest = false;
        boolean canJoin = false;

        if (!isUserMember && !isUserLeader) {
            hasPendingRequest = joinRequestRepository.hasActiveRequestForTeam(
                    userId, teamId, LocalDateTime.now());
            canJoin = !hasPendingRequest && !team.isFull() && team.isActive() &&
                    !teamRepository.isUserInAnyTeamForHackathon(userId, team.getHackathon().getId());
        }

        return convertToTeamDetailsDto(team, isUserMember, isUserLeader, hasPendingRequest, canJoin);
    }

    @Override
    public UpdateTeamResponseDto updateTeam(UpdateTeamRequestDto request, Long userId) {
        log.info("Updating team: {} by user: {}", request.getTeamId(), userId);

        Team team = teamRepository.findByIdAndNotDeleted(request.getTeamId())
                .orElseThrow(() -> new RuntimeException("Team not found"));

        // Check if user is the leader
        if (!team.isUserLeader(userId)) {
            throw new RuntimeException("Only team leader can update team details");
        }

        // Update team details
        if (request.getTeamName() != null) team.setTeamName(request.getTeamName());
        if (request.getDescription() != null) team.setDescription(request.getDescription());
        if (request.getRolesNeeded() != null) team.setRolesNeeded(request.getRolesNeeded());
        if (request.getSkillsNeeded() != null) team.setSkillsNeeded(request.getSkillsNeeded());
        if (request.getContactEmail() != null) team.setContactEmail(request.getContactEmail());
        if (request.getContactPhone() != null) team.setContactPhone(request.getContactPhone());
        if (request.getDiscordServer() != null) team.setDiscordServer(request.getDiscordServer());
        if (request.getWhatsappGroup() != null) team.setWhatsappGroup(request.getWhatsappGroup());
        if (request.getLinkedinGroup() != null) team.setLinkedinGroup(request.getLinkedinGroup());
        if (request.getExternalGroupLink() != null) team.setExternalGroupLink(request.getExternalGroupLink());

        team.setPublic(request.isPublic());
        team.setAutoAccept(request.isAutoAccept());

        teamRepository.save(team);

        log.info("Team updated: {}", team.getId());

        return new UpdateTeamResponseDto(
                true,
                "Team updated successfully",
                LocalDateTime.now()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public TeamSearchResponseDto searchTeams(TeamSearchRequestDto searchRequest, Long userId) {
        log.info("Searching teams for hackathon: {} by user: {}", searchRequest.getHackathonId(), userId);

        // Validate hackathon
        hackathonRepository.findApprovedById(searchRequest.getHackathonId())
                .orElseThrow(() -> new RuntimeException("Hackathon not found"));

        // Create pageable with sorting
        Sort sort = createSortFromRequest(searchRequest.getSortBy(), searchRequest.getSortDirection());
        Pageable pageable = PageRequest.of(searchRequest.getPage(), searchRequest.getSize(), sort);

        Page<Team> teamPage;

        // Apply filters based on search criteria
        if (searchRequest.getSearch() != null && !searchRequest.getSearch().trim().isEmpty()) {
            teamPage = teamRepository.searchTeamsByHackathon(
                    searchRequest.getHackathonId(), searchRequest.getSearch().trim(), pageable);
        } else if (searchRequest.getUserSkills() != null && !searchRequest.getUserSkills().isEmpty()) {
            teamPage = teamRepository.findTeamsWithMatchingSkills(
                    searchRequest.getHackathonId(), searchRequest.getUserSkills(), pageable);
        } else {
            teamPage = teamRepository.findTeamsWithFilters(
                    searchRequest.getHackathonId(),
                    searchRequest.getStatus(),
                    searchRequest.getSearch(),
                    pageable);
        }

        // Convert to DTOs with skill matching
        List<TeamListItemDto> teamDtos = teamPage.getContent().stream()
                .map(team -> convertToListItemDto(team, searchRequest.getUserSkills()))
                .toList();

        return new TeamSearchResponseDto(
                teamDtos,
                teamPage.getNumber(),
                teamPage.getTotalPages(),
                teamPage.getTotalElements(),
                teamPage.hasNext(),
                teamPage.hasPrevious()
        );
    }

    @Override
    public RemoveMemberResponseDto removeMember(RemoveMemberRequestDto request, Long leaderId) {
        log.info("Removing member from team: {} by leader: {}", request.getTeamId(), leaderId);

        Team team = teamRepository.findByIdAndNotDeleted(request.getTeamId())
                .orElseThrow(() -> new RuntimeException("Team not found"));

        // Check if user is the leader
        if (!team.isUserLeader(leaderId)) {
            throw new RuntimeException("Only team leader can remove members");
        }

        User userToRemove = userRepository.findById(request.getUserIdToRemove())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Cannot remove leader
        if (team.isUserLeader(request.getUserIdToRemove())) {
            throw new RuntimeException("Cannot remove team leader");
        }

        // Check if user is actually a member
        if (!team.isUserMember(request.getUserIdToRemove())) {
            throw new RuntimeException("User is not a member of this team");
        }

        // Remove member
        team.removeMember(userToRemove);
        teamRepository.save(team);

        log.info("Member removed from team: {}", team.getId());

        return new RemoveMemberResponseDto(
                true,
                "Member removed successfully",
                userToRemove.getEmail(), // You might want to get full name from profile
                LocalDateTime.now()
        );
    }

    @Override
    public LeaveTeamResponseDto leaveTeam(LeaveTeamRequestDto request, Long userId) {
        log.info("User {} leaving team: {}", userId, request.getTeamId());

        Team team = teamRepository.findByIdAndNotDeleted(request.getTeamId())
                .orElseThrow(() -> new RuntimeException("Team not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if user is a member
        if (!team.isUserMember(userId)) {
            throw new RuntimeException("You are not a member of this team");
        }

        // Leader cannot leave - must transfer leadership or delete team
        if (team.isUserLeader(userId)) {
            throw new RuntimeException("Team leader cannot leave. Transfer leadership or delete team.");
        }

        // Remove member
        team.removeMember(user);
        teamRepository.save(team);

        log.info("User {} left team: {}", userId, team.getId());

        return new LeaveTeamResponseDto(
                true,
                "You have left the team successfully",
                LocalDateTime.now()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public TeamSearchResponseDto getUserTeams(Long userId) {
        log.info("Fetching teams for user: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Team> userTeams = teamRepository.findTeamsByMember(userId);

        List<TeamListItemDto> teamDtos = userTeams.stream()
                .map(team -> convertToListItemDto(team, null))
                .toList();

        return new TeamSearchResponseDto(
                teamDtos, 0, 1, teamDtos.size(), false, false
        );
    }

    @Override
    public TeamExportResponseDto exportTeamData(TeamExportRequestDto request, Long userId) {
        log.info("Exporting team data for team: {} by user: {}", request.getTeamId(), userId);

        Team team = teamRepository.findByIdAndNotDeleted(request.getTeamId())
                .orElseThrow(() -> new RuntimeException("Team not found"));

        // Check if user has access
        if (!team.isUserMember(userId) && !team.isUserLeader(userId)) {
            throw new RuntimeException("Access denied");
        }

        // In a real implementation, you would generate PDF/CSV here
        // For now, return a mock response
        String fileName = String.format("team_%s_%s.%s",
                team.getTeamName().replaceAll("\\s+", "_"),
                LocalDateTime.now().toString().substring(0, 10),
                request.getFormat().toLowerCase());

        return new TeamExportResponseDto(
                true,
                "Team data exported successfully",
                "/exports/" + fileName, // Mock URL
                fileName,
                request.getFormat(),
                LocalDateTime.now()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Team> findById(Long id) {
        return teamRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isUserInTeamForHackathon(Long userId, Long hackathonId) {
        return teamRepository.isUserInAnyTeamForHackathon(userId, hackathonId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isUserTeamLeader(Long userId, Long teamId) {
        return teamRepository.findByIdAndNotDeleted(teamId)
                .map(team -> team.isUserLeader(userId))
                .orElse(false);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isUserTeamMember(Long userId, Long teamId) {
        return teamRepository.findByIdAndNotDeleted(teamId)
                .map(team -> team.isUserMember(userId))
                .orElse(false);
    }

    @Override
    @Transactional(readOnly = true)
    public Team getUserTeamForHackathon(Long userId, Long hackathonId) {
        return teamRepository.findUserTeamInHackathon(userId, hackathonId).orElse(null);
    }

    // Helper methods
    private Sort createSortFromRequest(String sortBy, String sortDirection) {
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDirection) ?
                Sort.Direction.DESC : Sort.Direction.ASC;

        return switch (sortBy) {
            case "createdAt" -> Sort.by(direction, "createdAt");
            case "teamName" -> Sort.by(direction, "teamName");
            case "availableSlots" -> Sort.by(direction, "maxSize").and(Sort.by(Sort.Direction.ASC, "currentSize"));
            case "matchPercentage" -> Sort.by(direction, "createdAt"); // Default sort for match percentage
            default -> Sort.by(Sort.Direction.DESC, "createdAt");
        };
    }

    private TeamListItemDto convertToListItemDto(Team team, List<String> userSkills) {
        List<String> matchingSkills = team.getMatchingSkills(userSkills);
        int matchPercentage = team.calculateMatchPercentage(userSkills);

        // Get leader profile for display
        Profile leaderProfile = profileRepository.findByUserId(team.getLeader().getId()).orElse(null);

        return new TeamListItemDto(
                team.getId(),
                team.getTeamName(),
                team.getDescription(),
                team.getMaxSize(),
                team.getCurrentSize(),
                team.getAvailableSlots(),
                leaderProfile != null ? leaderProfile.getFullName() : team.getLeader().getEmail(),
                leaderProfile != null ? leaderProfile.getAvatarId() : null,
                team.getRolesNeeded(),
                team.getSkillsNeeded(),
                team.getStatus(),
                team.isPublic(),
                team.isAutoAccept(),
                matchingSkills,
                matchPercentage,
                team.getCreatedAt()
        );
    }

    private TeamDetailsResponseDto convertToTeamDetailsDto(Team team, boolean isUserMember,
                                                           boolean isUserLeader, boolean hasPendingRequest,
                                                           boolean canJoin) {
        // Convert leader to DTO
        TeamMemberDto leaderDto = convertToMemberDto(team.getLeader(), TeamRole.LEADER, "Leader");

        // Convert members to DTOs
        List<TeamMemberDto> memberDtos = team.getMembers().stream()
                .filter(TeamMember::isActive)
                .map(member -> convertToMemberDto(member.getUser(), member.getRole(), member.getAssignedRole()))
                .toList();

        // Convert hackathon to summary DTO
        HackathonSummaryDto hackathonSummary = new HackathonSummaryDto(
                team.getHackathon().getId(),
                team.getHackathon().getTitle(),
                team.getHackathon().getOrganizer(),
                team.getHackathon().getDeadline(),
                team.getHackathon().getUrgencyLevel()
        );

        return new TeamDetailsResponseDto(
                team.getId(),
                team.getTeamName(),
                team.getDescription(),
                team.getMaxSize(),
                team.getCurrentSize(),
                team.getAvailableSlots(),
                team.getStatus(),
                team.isPublic(),
                team.isAutoAccept(),
                team.getRolesNeeded(),
                team.getSkillsNeeded(),
                leaderDto,
                memberDtos,
                hackathonSummary,
                isUserMember,
                isUserLeader,
                hasPendingRequest,
                canJoin,
                team.getCreatedAt(),
                team.getUpdatedAt()
        );
    }

    private TeamMemberDto convertToMemberDto(User user, TeamRole role, String assignedRole) {
        Profile profile = profileRepository.findByUserId(user.getId()).orElse(null);

        return new TeamMemberDto(
                user.getId(),
                profile != null ? profile.getFullName() : user.getEmail(),
                profile != null ? profile.getAvatarId() : null,
                role.name(),
                assignedRole,
                profile != null ? profile.getSkills() : new ArrayList<>(),
                profile != null ? profile.getCollege() : null,
                profile != null ? profile.getYear() : null,
                user.getEmail(),
                null, // Phone hidden for privacy
                null, // Discord hidden for privacy
                profile != null ? profile.getGithubProfile() : null,
                LocalDateTime.now(), // Mock joined date
                true
        );
    }
}