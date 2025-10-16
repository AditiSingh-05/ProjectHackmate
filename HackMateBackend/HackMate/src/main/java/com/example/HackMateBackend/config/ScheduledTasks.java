package com.example.HackMateBackend.config;

import com.example.HackMateBackend.services.interfaces.JoinRequestService;
import com.example.HackMateBackend.services.interfaces.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class ScheduledTasks {

    private final JoinRequestService joinRequestService;
    private final NotificationService notificationService;

    // Run every hour to expire old join requests
    @Scheduled(fixedRate = 3600000) // 1 hour = 3600000 ms
    public void expireOldJoinRequests() {
        log.info("Running scheduled task: expiring old join requests");
        try {
            joinRequestService.expireOldRequests();
        } catch (Exception e) {
            log.error("Error in expiring old join requests", e);
        }
    }

    // Run daily to cleanup expired notifications
    @Scheduled(cron = "0 0 2 * * ?") // Run at 2 AM daily
    public void cleanupExpiredNotifications() {
        log.info("Running scheduled task: cleaning up expired notifications");
        try {
            notificationService.cleanupExpiredNotifications();
        } catch (Exception e) {
            log.error("Error in cleaning up expired notifications", e);
        }
    }

    // Run every 6 hours to send deadline reminders (mock implementation)
    @Scheduled(fixedRate = 21600000) // 6 hours = 21600000 ms
    public void sendDeadlineReminders() {
        log.info("Running scheduled task: sending deadline reminders");
        try {
            // In a real implementation, you'd fetch hackathons with approaching deadlines
            // and send notifications to registered users
            log.info("Deadline reminders task completed");
        } catch (Exception e) {
            log.error("Error in sending deadline reminders", e);
        }
    }
}