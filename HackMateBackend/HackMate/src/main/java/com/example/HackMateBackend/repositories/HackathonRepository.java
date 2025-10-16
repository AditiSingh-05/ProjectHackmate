package com.example.HackMateBackend.repositories;

import com.example.HackMateBackend.data.entities.Hackathon;
import com.example.HackMateBackend.data.entities.User;
import com.example.HackMateBackend.data.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface HackathonRepository extends JpaRepository<Hackathon,Long>{

    @Query("SELECT h FROM Hackathon h WHERE h.status = 'ACCEPTED' AND h.deleted = false ORDER BY h.deadline ASC")
    Page<Hackathon> findApprovedHackathons(Pageable pageable);

    @Query("SELECT h FROM Hackathon h WHERE h.status = 'ACCEPTED' AND h.deleted = false AND h.deadline > :now ORDER BY h.deadline ASC")
    Page<Hackathon> findActiveHackathons(@Param("now") LocalDateTime now, Pageable pageable);

    @Query("SELECT h FROM Hackathon h WHERE h.status = 'ACCEPTED' AND h.deleted = false " +
            "AND (LOWER(h.title) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(h.description) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(h.organizer) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "ORDER BY h.deadline ASC")
    Page<Hackathon> searchHackathons(@Param("search") String search, Pageable pageable);

    @Query("SELECT h FROM Hackathon h JOIN h.tags t WHERE h.status = 'ACCEPTED' AND h.deleted = false " +
            "AND t IN :tags ORDER BY h.deadline ASC")
    Page<Hackathon> findByTagsIn(@Param("tags") List<String> tags, Pageable pageable);

    @Query("SELECT h FROM Hackathon h WHERE h.status = 'ACCEPTED' AND h.deleted = false " +
            "AND LOWER(h.organizer) = LOWER(:organizer) ORDER BY h.deadline ASC")
    Page<Hackathon> findByOrganizer(@Param("organizer") String organizer, Pageable pageable);

    @Query("SELECT h FROM Hackathon h WHERE h.status = 'ACCEPTED' AND h.deleted = false " +
            "AND LOWER(h.location) LIKE LOWER(CONCAT('%', :location, '%')) ORDER BY h.deadline ASC")
    Page<Hackathon> findByLocation(@Param("location") String location, Pageable pageable);

    List<Hackathon> findByStatusAndDeletedFalse(Status status);

    @Query("SELECT h FROM Hackathon h WHERE h.status = 'PENDING' AND h.deleted = false ORDER BY h.createdAt ASC")
    List<Hackathon> findPendingHackathons();

    List<Hackathon> findByPostedByAndDeletedFalse(User postedBy);

    @Query("SELECT h FROM Hackathon h JOIN h.registrations r WHERE r.user.id = :userId " +
            "AND r.status = 'REGISTERED' AND h.deleted = false ORDER BY h.deadline ASC")
    List<Hackathon> findRegisteredHackathonsByUser(@Param("userId") Long userId);

    @Query("SELECT h FROM Hackathon h JOIN h.registrations r WHERE r.user.id = :userId " +
            "AND r.status = 'STARRED' AND h.deleted = false ORDER BY h.deadline ASC")
    List<Hackathon> findStarredHackathonsByUser(@Param("userId") Long userId);

    @Query("SELECT h FROM Hackathon h WHERE h.status = 'ACCEPTED' AND h.deleted = false " +
            "AND h.deadline BETWEEN :start AND :end")
    List<Hackathon> findHackathonsWithDeadlineBetween(@Param("start") LocalDateTime start,
                                                      @Param("end") LocalDateTime end);

    @Query("SELECT h FROM Hackathon h WHERE h.status = 'ACCEPTED' AND h.deleted = false " +
            "AND h.deadline < :now")
    List<Hackathon> findExpiredHackathons(@Param("now") LocalDateTime now);

    @Query("SELECT COUNT(h) FROM Hackathon h WHERE h.status = 'ACCEPTED' AND h.deleted = false")
    long countApprovedHackathons();

    @Query("SELECT COUNT(h) FROM Hackathon h WHERE h.status = 'PENDING' AND h.deleted = false")
    long countPendingHackathons();

    @Query("SELECT COUNT(h) FROM Hackathon h WHERE h.postedBy.id = :userId AND h.deleted = false")
    long countByPostedBy(@Param("userId") Long userId);

    @Modifying
    @Query("UPDATE Hackathon h SET h.viewCount = h.viewCount + 1 WHERE h.id = :hackathonId")
    void incrementViewCount(@Param("hackathonId") Long hackathonId);

    @Modifying
    @Query("UPDATE Hackathon h SET h.registrationCount = h.registrationCount + 1 WHERE h.id = :hackathonId")
    void incrementRegistrationCount(@Param("hackathonId") Long hackathonId);

    @Modifying
    @Query("UPDATE Hackathon h SET h.registrationCount = h.registrationCount - 1 WHERE h.id = :hackathonId AND h.registrationCount > 0")
    void decrementRegistrationCount(@Param("hackathonId") Long hackathonId);

    @Modifying
    @Query("UPDATE Hackathon h SET h.teamCount = h.teamCount + 1 WHERE h.id = :hackathonId")
    void incrementTeamCount(@Param("hackathonId") Long hackathonId);

    @Modifying
    @Query("UPDATE Hackathon h SET h.teamCount = h.teamCount - 1 WHERE h.id = :hackathonId AND h.teamCount > 0")
    void decrementTeamCount(@Param("hackathonId") Long hackathonId);

    @Query("SELECT h FROM Hackathon h WHERE h.id = :id AND h.deleted = false")
    Optional<Hackathon> findByIdAndNotDeleted(@Param("id") Long id);

    @Query("SELECT h FROM Hackathon h WHERE h.id = :id AND h.status = 'ACCEPTED' AND h.deleted = false")
    Optional<Hackathon> findApprovedById(@Param("id") Long id);




}
