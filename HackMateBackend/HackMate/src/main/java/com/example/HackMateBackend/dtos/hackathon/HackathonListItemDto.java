package com.example.HackMateBackend.dtos.hackathon;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HackathonListItemDto {
    private Long hackathonId;
    private String title;
    private String description;
    private String posterUrl;
    private List<String> tags;
    private String organizer;
    private String location;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deadline;

    private String prizePool;
    private long viewCount;
    private long registrationCount;
    private long teamCount;
    private boolean isRegistered;
    private boolean isStarred;
    private String urgencyLevel;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime postedAt;
}