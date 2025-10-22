package com.example.HackMateBackend.dtos.hackathon;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationToggleRequestDto {
    @NotNull(message = "Hackathon ID is required")
    private Long hackathonId;
    private boolean register;
}
