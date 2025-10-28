package com.example.HackMateBackend.dtos.hackathon;

import com.example.HackMateBackend.data.entities.User;
import com.example.HackMateBackend.data.enums.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public  class HackathonDetailsResponseDto {
    private Long hackathonId;
    private String title;
    private String description;
    private String registrationLink;
    private String posterUrl;
    private List<String> tags;
    private String organizer;
    private String location;
    private String prizePool;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deadline;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventStartDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventEndDate;

    private Integer maxTeamSize;
    private Integer minTeamSize;
    private String contactEmail;
    private long viewCount;
    private long registrationCount;
    private long teamCount;
    private String status;
//    private boolean isRegistered;
//    private boolean isStarred;
    private boolean isExpired;
    private String urgencyLevel;
    private String postedBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime postedAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime approvedAt;


    public HackathonDetailsResponseDto(Long id, String title, String description, String registrationLink, String posterUrl, List<String> tags, String organizer, String location, String prizePool, LocalDateTime deadline, LocalDateTime eventStartDate, LocalDateTime eventEndDate, Integer maxTeamSize, Integer minTeamSize, String contactEmail, Long viewCount, Long registrationCount, Long teamCount, String name, boolean isRegistered, boolean isStarred, boolean expired, String urgencyLevel, String email, LocalDateTime createdAt, LocalDateTime approvedAt) {
    }
}
