package com.example.HackMateBackend.dtos.hackathon;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AIExtractionRequestDto {
    @NotBlank(message = "Message text is required for extraction")
    @Size(min = 50, message = "Message must be at least 50 characters for proper extraction")
    private String messageText;
}


