package com.example.HackMateBackend.dtos;

import com.example.HackMateBackend.data.enums.Roles;
import com.example.HackMateBackend.data.enums.TeamStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;

public class TeamDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateTeamRequestDto {
        @NotNull(message = "Hackathon ID is required")
        private Long hackathonId;

        @NotBlank(message = "Team name is required")
        @Size(min = 3, max = 100, message = "Team name must be between 3 and 100 characters")
        private String teamName;

        @NotBlank(message = "Team description is required")
        @Size(min = 10, max = 1000, message = "Team description must be between 10 and 1000 characters")
        private String description;

        @NotNull(message = "Maximum team size is required")
        @Min(value = 2, message = "Team must have at least 2 members")
        @Max(value = 10, message = "Team cannot have more than 10 members")
        private Integer maxSize;

        @NotNull(message = "At least one required role must be specified")
        @Size(min = 1, message = "At least one role is required")
        private List<String> rolesNeeded;

        private List<String> skillsNeeded;
        private String contactEmail;
        private String contactPhone;
        private String discordServer;
        private String whatsappGroup;
        private String linkedinGroup;
        private String externalGroupLink;
        private boolean isPublic = true;
        private boolean autoAccept = false;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateTeamResponseDto {
        private boolean success;
        private String message;
        private Long teamId;
        private String teamName;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createdAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeamListItemDto {
        private Long teamId;
        private String teamName;
        private String description;
        private Integer maxSize;
        private Integer currentSize;
        private Integer availableSlots;
        private String leaderName;
        private String leaderAvatarId;
        private List<String> rolesNeeded;
        private List<String> skillsNeeded;
        private String status; // OPEN, FULL, CLOSED
        private boolean isPublic;
        private boolean autoAccept;
        private List<String> matchingSkills; // Skills that match current user
        private int matchPercentage;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createdAt;

        public TeamListItemDto(Long id, String teamName, String description, int maxSize, int currentSize, Integer availableSlots, String leaderName, String leaderAvatarId, List<Roles> rolesNeeded, List<String> skillsNeeded, TeamStatus status, boolean aPublic, boolean autoAccept, List<String> matchingSkills, int matchPercentage, LocalDateTime createdAt) {

        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeamDetailsResponseDto {
        private Long teamId;
        private String teamName;
        private String description;
        private Integer maxSize;
        private Integer currentSize;
        private Integer availableSlots;
        private String status;
        private boolean isPublic;
        private boolean autoAccept;
        private List<String> rolesNeeded;
        private List<String> skillsNeeded;

        private TeamMemberDto leader;

        private List<TeamMemberDto> members;

        private HackathonSummaryDto hackathon;

        private boolean isUserMember;
        private boolean isUserLeader;
        private boolean hasPendingRequest;
        private boolean canJoin;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createdAt;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime updatedAt;

        public TeamDetailsResponseDto(Long id, String teamName, String description, int maxSize, int currentSize, Integer availableSlots, TeamStatus status, boolean aPublic, boolean autoAccept, List<Roles> rolesNeeded, List<String> skillsNeeded, TeamMemberDto leaderDto, List<TeamMemberDto> memberDtos, HackathonSummaryDto hackathonSummary, boolean isUserMember, boolean isUserLeader, boolean hasPendingRequest, boolean canJoin, LocalDateTime createdAt, LocalDateTime updatedAt) {

        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeamMemberDto {
        private Long userId;
        private String fullName;
        private String avatarId;
        private String role; // LEADER, MEMBER
        private String assignedRole; // Frontend, Backend, etc.
        private List<String> skills;
        private String college;
        private String year;

        private String email;
        private String phone;
        private String discordUsername;
        private String githubProfile;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime joinedAt;

        private boolean active;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HackathonSummaryDto {
        private Long hackathonId;
        private String title;
        private String organizer;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime deadline;

        private String urgencyLevel;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateTeamRequestDto {
        @NotNull(message = "Team ID is required")
        private Long teamId;

        private String teamName;
        private String description;
        private List<String> rolesNeeded;
        private List<String> skillsNeeded;
        private String contactEmail;
        private String contactPhone;
        private String discordServer;
        private String whatsappGroup;
        private String linkedinGroup;
        private String externalGroupLink;
        private boolean isPublic;
        private boolean autoAccept;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateTeamResponseDto {
        private boolean success;
        private String message;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime updatedAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeamSearchRequestDto {
        private Long hackathonId; // Required: search teams for specific hackathon
        private List<String> userSkills; // For smart matching
        private String search; // Search in team name and description
        private String status = "OPEN"; // OPEN, FULL, CLOSED, ALL
        private boolean publicOnly = true;
        private String sortBy = "matchPercentage"; // matchPercentage, createdAt, availableSlots
        private String sortDirection = "desc"; // asc, desc
        private int page = 0;
        private int size = 20;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeamSearchResponseDto {
        private List<TeamListItemDto> teams;
        private int currentPage;
        private int totalPages;
        private long totalElements;
        private boolean hasNext;
        private boolean hasPrevious;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RemoveMemberRequestDto {
        @NotNull(message = "Team ID is required")
        private Long teamId;

        @NotNull(message = "User ID is required")
        private Long userIdToRemove;

        private String reason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RemoveMemberResponseDto {
        private boolean success;
        private String message;
        private String removedMemberName;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime removedAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LeaveTeamRequestDto {
        @NotNull(message = "Team ID is required")
        private Long teamId;

        private String reason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LeaveTeamResponseDto {
        private boolean success;
        private String message;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime leftAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeamExportRequestDto {
        @NotNull(message = "Team ID is required")
        private Long teamId;

        private String format = "PDF"; // PDF, CSV
        private boolean includeContactInfo = true;
        private boolean includeHackathonDetails = true;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeamExportResponseDto {
        private boolean success;
        private String message;
        private String downloadUrl;
        private String fileName;
        private String fileFormat;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime generatedAt;
    }
}