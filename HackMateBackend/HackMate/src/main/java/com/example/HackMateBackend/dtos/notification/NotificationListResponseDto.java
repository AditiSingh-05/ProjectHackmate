package com.example.HackMateBackend.dtos.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationListResponseDto {
    private List<NotificationItemDto> notifications;
    private int unreadCount;
    private int totalCount;
    private boolean hasMore;
    private int page;
    private int size;
}