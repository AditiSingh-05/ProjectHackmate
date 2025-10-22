package com.example.HackMateBackend.dtos.joinrequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessJoinRequestDto {
    @NotNull(message = "Join request ID is required")
    private Long joinRequestId;

    @NotBlank(message = "Action is required")
    private String action; // ACCEPT, REJECT

    @Size(max = 200, message = "Response message cannot exceed 200 characters")
    private String responseMessage;
}

