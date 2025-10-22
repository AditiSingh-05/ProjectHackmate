package com.example.HackMateBackend.dtos.joinrequest;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessJoinRequestResponseDto {
    private boolean success;
    private String message;
    private String action; // ACCEPTED, REJECTED
    private String requesterName;
    private String teamName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime processedAt;
}

