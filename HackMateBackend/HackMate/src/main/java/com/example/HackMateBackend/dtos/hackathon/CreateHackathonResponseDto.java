package com.example.HackMateBackend.dtos.hackathon;

import com.example.HackMateBackend.data.enums.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateHackathonResponseDto {
    private boolean success;
    private String message;
    private Long hackathonId;
    private Status status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime submittedAt;
}

