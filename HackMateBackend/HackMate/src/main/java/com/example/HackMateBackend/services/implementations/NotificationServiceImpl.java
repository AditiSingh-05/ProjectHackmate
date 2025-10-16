package com.example.HackMateBackend.services.implementations;

import com.example.HackMateBackend.data.entities.*;
import com.example.HackMateBackend.data.enums.Priority;
import com.example.HackMateBackend.dtos.NotificationDto.*;
import com.example.HackMateBackend.repositories.*;
import com.example.HackMateBackend.services.interfaces.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public NotificationListResponseDto getNotifications(Long userId, int page, int size) {
        log.info("Fetching notifications for user: {}, page: {}, size: {}", userId, page, size);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Pageable pageable = PageRequest.of(page, size);
        Page<Notification> notificationPage = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);

        List<NotificationItemDto> notificationDtos = notificationPage.getContent().stream()
                .map(this::convertToNotificationItemDto)
                .toList();

        long unreadCount = notificationRepository.countUnreadByUser(userId);

        return new NotificationListResponseDto(
                notificationDtos,
                (int) unreadCount,
                (int) notificationPage.getTotalElements(),
                notificationPage.hasNext(),
                page,
                size
        );
    }

    @Override
    public MarkAsReadResponseDto markAsRead(MarkAsReadRequestDto request, Long userId) {
        log.info("Marking notification as read: {} by user: {}", request.getNotificationId(), userId);

        LocalDateTime readAt = LocalDateTime.now();
        notificationRepository.markAsReadByUser(request.getNotificationId(), userId, readAt);

        return new MarkAsReadResponseDto(
                true,
                "Notification marked as read",
                readAt
        );
    }

    @Override
    public MarkAllAsReadResponseDto markAllAsRead(Long userId) {
        log.info("Marking all notifications as read for user: {}", userId);

        LocalDateTime readAt = LocalDateTime.now();
        int markedCount = notificationRepository.markAllAsReadByUser(userId, readAt);

        return new MarkAllAsReadResponseDto(
                true,
                String.format("Marked %d notifications as read", markedCount),
                markedCount,
                readAt
        );
    }

    @Override
    public DeleteNotificationResponseDto deleteNotification(DeleteNotificationRequestDto request, Long userId) {
        log.info("Deleting notification: {} by user: {}", request.getNotificationId(), userId);

        Notification notification = notificationRepository.findById(request.getNotificationId())
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        if (!notification.getUser().getId().equals(userId)) {
            throw new RuntimeException("You can only delete your own notifications");
        }

        notification.setDeleted(true);
        notificationRepository.save(notification);

        return new DeleteNotificationResponseDto(
                true,
                "Notification deleted successfully",
                LocalDateTime.now()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationPreferencesDto getNotificationPreferences(Long userId) {
        log.info("Fetching notification preferences for user: {}", userId);

        // In a real implementation, you'd have a NotificationPreferences entity
        // For now, return default preferences
        return new NotificationPreferencesDto(
                true,  // emailNotifications
                true,  // pushNotifications
                true,  // hackathonDeadlineReminders
                true,  // joinRequestUpdates
                true,  // teamUpdates
                true,  // generalAnnouncements
                24     // reminderHoursBefore
        );
    }

    @Override
    public UpdateNotificationPreferencesResponseDto updateNotificationPreferences(
            UpdateNotificationPreferencesRequestDto request, Long userId) {
        log.info("Updating notification preferences for user: {}", userId);

        // In a real implementation, you'd update NotificationPreferences entity
        // For now, return success response
        return new UpdateNotificationPreferencesResponseDto(
                true,
                "Notification preferences updated successfully",
                LocalDateTime.now()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationStatsDto getNotificationStats(Long userId) {
        log.info("Fetching notification stats for user: {}", userId);

        long totalNotifications = notificationRepository.countTotalByUser(userId);
        long unreadCount = notificationRepository.countUnreadByUser(userId);
        long todayCount = notificationRepository.countByUserSince(userId, LocalDateTime.now().withHour(0).withMinute(0));
        long weekCount = notificationRepository.countByUserSince(userId, LocalDateTime.now().minusDays(7));
        long urgentCount = notificationRepository.countUrgentUnreadByUser(userId);

        return new NotificationStatsDto(
                (int) totalNotifications,
                (int) unreadCount,
                (int) todayCount,
                (int) weekCount,
                (int) urgentCount,
                (int) urgentCount, // Mock high priority count
                LocalDateTime.now().minusDays(1) // Mock last notification time
        );
    }

    @Override
    public void createJoinRequestNotification(JoinRequest joinRequest) {
        log.info("Creating join request notification for team leader: {}", joinRequest.getTeam().getLeader().getId());

        Notification notification = Notification.createJoinRequestNotification(
                joinRequest.getTeam().getLeader(), joinRequest);
        notificationRepository.save(notification);
    }

    @Override
    public void createJoinRequestResponseNotification(JoinRequest joinRequest, boolean accepted) {
        log.info("Creating join request response notification for user: {}", joinRequest.getRequester().getId());

        Notification notification = Notification.createJoinRequestResponseNotification(
                joinRequest.getRequester(), joinRequest, accepted);
        notificationRepository.save(notification);
    }

    @Override
    public void createHackathonDeadlineNotification(Hackathon hackathon, Long userId, int hoursRemaining) {
        log.info("Creating deadline notification for hackathon: {} user: {}", hackathon.getId(), userId);

        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            Notification notification = Notification.createHackathonDeadlineNotification(
                    user, hackathon, hoursRemaining);
            notificationRepository.save(notification);
        }
    }

    @Override
    public void createTeamUpdateNotification(Long teamId, String message) {
        log.info("Creating team update notification for team: {}", teamId);
        // Implementation would create notifications for all team members
    }

    @Override
    public void cleanupExpiredNotifications() {
        log.info("Cleaning up expired notifications");

        int deletedCount = notificationRepository.deleteExpiredNotifications(LocalDateTime.now());
        log.info("Deleted {} expired notifications", deletedCount);

        // Also clean up old read notifications (older than 30 days)
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(30);
        int oldDeletedCount = notificationRepository.deleteOldReadNotifications(cutoffDate);
        log.info("Deleted {} old read notifications", oldDeletedCount);
    }

    // Helper methods
    private NotificationItemDto convertToNotificationItemDto(Notification notification) {
        return new NotificationItemDto(
                notification.getId(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getType(),
                notification.getPriority(),
                notification.isRead(),
                notification.getActionUrl(),
                notification.getRelatedEntityId(),
                notification.getRelatedEntityType(),
                notification.getCreatedAt(),
                notification.getReadAt(),
                notification.getExpiresAt(),
                notification.isExpired()
        );
    }
}