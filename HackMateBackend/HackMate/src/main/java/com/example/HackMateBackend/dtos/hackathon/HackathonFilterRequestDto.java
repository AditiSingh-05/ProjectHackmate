package com.example.HackMateBackend.dtos.hackathon;

import com.example.HackMateBackend.data.enums.RegistrationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HackathonFilterRequestDto {
    private String search;
    private List<String> tags;
    private RegistrationStatus status;
    private String urgencyLevel;
    private String organizer;
    private String location;
    private boolean showExpired = false;
    private String sortBy = "deadline";
    private String sortDirection = "asc";
    private int page = 0;
    private int size = 20;
}
