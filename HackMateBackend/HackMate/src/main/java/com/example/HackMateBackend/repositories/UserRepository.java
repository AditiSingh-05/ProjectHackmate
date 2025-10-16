package com.example.HackMateBackend.repositories;

import com.example.HackMateBackend.data.entities.User;
import com.example.HackMateBackend.data.enums.Roles;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);

    Optional<User> findByEmailVerificationToken(String Token);

    Optional<User> findByPasswordResetToken(String token);

    @Query("SELECT u FROM User u WHERE u.passwordResetToken IS NOT NULL AND u.passwordResetExpiresAt > :now")
    List<User> findUsersWithValidPasswordResetToken(@Param("now") LocalDateTime now);

    List<User> findByRole(Roles role);

    List<User> findByProfileSetup(boolean profileSetup);

    List<User> findByEmailVerified(boolean emailVerified);

    long countByRole(Roles role);

    long countByProfileSetup(boolean profileSetup);


    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.lastLoginAt = :loginTime WHERE u.id = :userId")
    void updateLastLoginTime(@Param("userId") Long userId, @Param("loginTime") LocalDateTime loginTime);

    @Query("SELECT u FROM User u WHERE u.lastLoginAt < :cutoffDate OR u.lastLoginAt IS NULL")
    List<User> findInactiveUsers(@Param("cutoffDate") LocalDateTime cutoffDate);


    @Query("SELECT u FROM User u WHERE u.createdAt BETWEEN :startDate AND :endDate")
    List<User> findUsersCreatedBetween(@Param("startDate") LocalDateTime startDate,
                                       @Param("endDate") LocalDateTime endDate);


    @Query("SELECT u FROM User u WHERE u.emailVerified = :verified AND u.createdAt > :since")
    List<User> findByEmailVerificationStatusSince(@Param("verified") boolean verified,
                                                  @Param("since") LocalDateTime since);


    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.passwordResetToken = NULL, u.passwordResetExpiresAt = NULL WHERE u.passwordResetExpiresAt < :now")
    int clearExpiredPasswordResetTokens(@Param("now") LocalDateTime now);

    @Query("SELECT " +
            "COUNT(u) as totalUsers, " +
            "SUM(CASE WHEN u.emailVerified = true THEN 1 ELSE 0 END) as verifiedUsers, " +
            "SUM(CASE WHEN u.profileSetup = true THEN 1 ELSE 0 END) as profileSetupUsers " +
            "FROM User u")
    Object[] getUserStatistics();



}
