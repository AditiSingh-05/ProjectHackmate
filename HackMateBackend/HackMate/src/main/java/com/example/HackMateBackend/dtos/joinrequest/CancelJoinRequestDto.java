package com.example.HackMateBackend.dtos.joinrequest;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CancelJoinRequestDto {
    @NotNull(message = "Join request ID is required")
    private Long joinRequestId;

    private String reason;
}

