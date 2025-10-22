package com.example.HackMateBackend.dtos.hackathon;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AIExtractionResponseDto {
    private boolean success;
    private String extractedTitle;
    private String extractedDescription;
    private String extractedRegistrationLink;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime extractedDeadline;

    private List<String> extractedTags;
    private String extractedOrganizer;
    private String extractedLocation;
    private String extractedPrizePool;
    private double confidenceScore;
    private String errorMessage;
}

