package com.example.HackMateBackend.repositories;

import com.example.HackMateBackend.data.entities.Notification;
import com.example.HackMateBackend.data.entities.User;
import com.example.HackMateBackend.data.enums.Priority;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // User notifications
    Page<Notification> findByUserAndDeletedFalseOrderByCreatedAtDesc(User user, Pageable pageable);

    @Query("SELECT n FROM Notification n WHERE n.user.id = :userId AND n.deleted = false " +
            "ORDER BY n.createdAt DESC")
    Page<Notification> findByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId, Pageable pageable);

    // Unread notifications
    @Query("SELECT n FROM Notification n WHERE n.user.id = :userId AND n.isRead = false " +
            "AND n.deleted = false ORDER BY n.createdAt DESC")
    List<Notification> findUnreadByUser(@Param("userId") Long userId);

    @Query("SELECT COUNT(n) FROM Notification n WHERE n.user.id = :userId AND n.isRead = false " +
            "AND n.deleted = false")
    long countUnreadByUser(@Param("userId") Long userId);

    // Mark as read operations
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true, n.readAt = :readAt " +
            "WHERE n.id = :notificationId AND n.user.id = :userId")
    void markAsReadByUser(@Param("notificationId") Long notificationId,
                          @Param("userId") Long userId,
                          @Param("readAt") LocalDateTime readAt);

    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true, n.readAt = :readAt " +
            "WHERE n.user.id = :userId AND n.isRead = false AND n.deleted = false")
    int markAllAsReadByUser(@Param("userId") Long userId, @Param("readAt") LocalDateTime readAt);

    // Priority-based queries
    @Query("SELECT n FROM Notification n WHERE n.user.id = :userId AND n.priority = :priority " +
            "AND n.deleted = false ORDER BY n.createdAt DESC")
    List<Notification> findByUserAndPriority(@Param("userId") Long userId, @Param("priority") Priority priority);

    @Query("SELECT COUNT(n) FROM Notification n WHERE n.user.id = :userId AND n.priority = 'URGENT' " +
            "AND n.isRead = false AND n.deleted = false")
    long countUrgentUnreadByUser(@Param("userId") Long userId);

    // Type-based queries
    @Query("SELECT n FROM Notification n WHERE n.user.id = :userId AND n.type = :type " +
            "AND n.deleted = false ORDER BY n.createdAt DESC")
    List<Notification> findByUserAndType(@Param("userId") Long userId, @Param("type") String type);

    // Expired notifications
    @Query("SELECT n FROM Notification n WHERE n.expiresAt IS NOT NULL AND n.expiresAt <= :now " +
            "AND n.deleted = false")
    List<Notification> findExpiredNotifications(@Param("now") LocalDateTime now);

    @Modifying
    @Query("UPDATE Notification n SET n.deleted = true WHERE n.expiresAt IS NOT NULL " +
            "AND n.expiresAt <= :now AND n.deleted = false")
    int deleteExpiredNotifications(@Param("now") LocalDateTime now);

    // Statistics
    @Query("SELECT COUNT(n) FROM Notification n WHERE n.user.id = :userId AND n.deleted = false")
    long countTotalByUser(@Param("userId") Long userId);

    @Query("SELECT COUNT(n) FROM Notification n WHERE n.user.id = :userId AND n.deleted = false " +
            "AND n.createdAt >= :since")
    long countByUserSince(@Param("userId") Long userId, @Param("since") LocalDateTime since);

    // Clean up old notifications
    @Modifying
    @Query("UPDATE Notification n SET n.deleted = true WHERE n.createdAt <= :cutoffDate " +
            "AND n.isRead = true AND n.deleted = false")
    int deleteOldReadNotifications(@Param("cutoffDate") LocalDateTime cutoffDate);
}