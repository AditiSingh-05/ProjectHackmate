package com.example.HackMateBackend.repositories;

import com.example.HackMateBackend.data.entities.JoinRequest;
import com.example.HackMateBackend.data.entities.Team;
import com.example.HackMateBackend.data.entities.User;
import com.example.HackMateBackend.data.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface JoinRequestRepository extends JpaRepository<JoinRequest, Long> {

    @Query("SELECT jr FROM JoinRequest jr WHERE jr.id = :id AND jr.deleted = false")
    Optional<JoinRequest> findByIdAndNotDeleted(@Param("id") Long id);

    List<JoinRequest> findByRequesterAndDeletedFalseOrderByCreatedAtDesc(User requester);

    @Query("SELECT jr FROM JoinRequest jr WHERE jr.requester.id = :userId AND jr.deleted = false " +
            "AND jr.status = 'PENDING' AND jr.expiresAt > :now")
    List<JoinRequest> findActiveJoinRequestsByUser(@Param("userId") Long userId,
                                                   @Param("now") LocalDateTime now);

    @Query("SELECT jr FROM JoinRequest jr WHERE jr.requester.id = :userId AND jr.deleted = false " +
            "AND jr.status IN ('ACCEPTED', 'REJECTED')")
    List<JoinRequest> findPastJoinRequestsByUser(@Param("userId") Long userId);

    List<JoinRequest> findByTeamAndDeletedFalseOrderByCreatedAtDesc(Team team);

    @Query("SELECT jr FROM JoinRequest jr WHERE jr.team.id = :teamId AND jr.deleted = false " +
            "AND jr.status = 'PENDING' AND jr.expiresAt > :now")
    List<JoinRequest> findPendingJoinRequestsByTeam(@Param("teamId") Long teamId,
                                                    @Param("now") LocalDateTime now);

    @Query("SELECT jr FROM JoinRequest jr WHERE jr.team.id = :teamId AND jr.deleted = false " +
            "AND jr.status IN ('ACCEPTED', 'REJECTED')")
    List<JoinRequest> findProcessedJoinRequestsByTeam(@Param("teamId") Long teamId);

    @Query("SELECT CASE WHEN COUNT(jr) > 0 THEN true ELSE false END FROM JoinRequest jr " +
            "WHERE jr.requester.id = :userId AND jr.deleted = false " +
            "AND jr.status = 'PENDING' AND jr.expiresAt > :now")
    boolean hasActiveJoinRequest(@Param("userId") Long userId, @Param("now") LocalDateTime now);

    @Query("SELECT jr FROM JoinRequest jr WHERE jr.requester.id = :userId AND jr.deleted = false " +
            "AND jr.status = 'PENDING' AND jr.expiresAt > :now")
    Optional<JoinRequest> findActiveJoinRequestByUser(@Param("userId") Long userId,
                                                      @Param("now") LocalDateTime now);

    @Query("SELECT CASE WHEN COUNT(jr) > 0 THEN true ELSE false END FROM JoinRequest jr " +
            "WHERE jr.requester.id = :userId AND jr.team.id = :teamId AND jr.deleted = false " +
            "AND jr.status = 'PENDING' AND jr.expiresAt > :now")
    boolean hasActiveRequestForTeam(@Param("userId") Long userId, @Param("teamId") Long teamId,
                                    @Param("now") LocalDateTime now);

    @Query("SELECT jr FROM JoinRequest jr WHERE jr.deleted = false " +
            "AND jr.status = 'PENDING' AND jr.expiresAt <= :now")
    List<JoinRequest> findExpiredJoinRequests(@Param("now") LocalDateTime now);

    @Modifying
    @Query("UPDATE JoinRequest jr SET jr.status = 'REJECTED', jr.processedAt = :now " +
            "WHERE jr.deleted = false AND jr.status = 'PENDING' AND jr.expiresAt <= :now")
    int markExpiredRequestsAsRejected(@Param("now") LocalDateTime now);

    @Query("SELECT COUNT(jr) FROM JoinRequest jr WHERE jr.requester.id = :userId AND jr.deleted = false")
    long countTotalRequestsByUser(@Param("userId") Long userId);

    @Query("SELECT COUNT(jr) FROM JoinRequest jr WHERE jr.requester.id = :userId " +
            "AND jr.deleted = false AND jr.status = :status")
    long countRequestsByUserAndStatus(@Param("userId") Long userId, @Param("status") Status status);

    @Query("SELECT COUNT(jr) FROM JoinRequest jr WHERE jr.team.id = :teamId AND jr.deleted = false")
    long countTotalRequestsByTeam(@Param("teamId") Long teamId);

    @Query("SELECT COUNT(jr) FROM JoinRequest jr WHERE jr.team.id = :teamId " +
            "AND jr.deleted = false AND jr.status = 'PENDING' AND jr.expiresAt > :now")
    long countPendingRequestsByTeam(@Param("teamId") Long teamId, @Param("now") LocalDateTime now);

    @Query("SELECT CASE WHEN COUNT(jr) > 0 THEN true ELSE false END FROM JoinRequest jr " +
            "WHERE jr.id = :requestId AND jr.team.leader.id = :leaderId AND jr.deleted = false")
    boolean canLeaderAccessRequest(@Param("requestId") Long requestId, @Param("leaderId") Long leaderId);

    @Query("SELECT CASE WHEN COUNT(jr) > 0 THEN true ELSE false END FROM JoinRequest jr " +
            "WHERE jr.id = :requestId AND jr.requester.id = :userId AND jr.deleted = false")
    boolean canUserAccessRequest(@Param("requestId") Long requestId, @Param("userId") Long userId);
}