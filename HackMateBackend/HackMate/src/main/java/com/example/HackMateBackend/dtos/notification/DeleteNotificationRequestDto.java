package com.example.HackMateBackend.dtos.notification;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteNotificationRequestDto {
    @NotNull(message = "Notification ID is required")
    private Long notificationId;
}

