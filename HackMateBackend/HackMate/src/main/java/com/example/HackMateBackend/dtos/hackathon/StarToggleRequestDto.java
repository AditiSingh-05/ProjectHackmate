package com.example.HackMateBackend.dtos.hackathon;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StarToggleRequestDto {
    @NotNull(message = "Hackathon ID is required")
    private Long hackathonId;
    private boolean star;
}