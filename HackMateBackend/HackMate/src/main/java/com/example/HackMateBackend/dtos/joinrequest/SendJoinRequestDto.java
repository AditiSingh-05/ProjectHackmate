package com.example.HackMateBackend.dtos.joinrequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendJoinRequestDto {
    @NotNull(message = "Team ID is required")
    private Long teamId;

    @NotBlank(message = "Requested role is required")
    private String requestedRole;

    @NotNull(message = "User skills are required")
    @Size(min = 1, message = "At least one skill must be provided")
    private List<String> userSkills;

    @NotBlank(message = "Message is required")
    @Size(min = 10, max = 500, message = "Message must be between 10 and 500 characters")
    private String message;
}

