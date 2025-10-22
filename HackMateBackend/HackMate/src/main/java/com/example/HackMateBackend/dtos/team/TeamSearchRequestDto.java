package com.example.HackMateBackend.dtos.team;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamSearchRequestDto {
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

