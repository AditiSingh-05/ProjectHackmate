package com.example.HackMateBackend.repositories;

import com.example.HackMateBackend.data.entities.Hackathon;
import com.example.HackMateBackend.data.entities.Team;
import com.example.HackMateBackend.data.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    @Query("SELECT t FROM Team t WHERE t.id = :id AND t.deleted = false")
    Optional<Team> findByIdAndNotDeleted(@Param("id") Long id);

    List<Team> findByHackathonAndDeletedFalse(Hackathon hackathon);

    List<Team> findByLeaderAndDeletedFalse(User leader);

    @Query("SELECT t FROM Team t WHERE t.hackathon.id = :hackathonId AND t.deleted = false " +
            "AND t.isPublic = true AND t.isActive = true")
    Page<Team> findPublicTeamsByHackathon(@Param("hackathonId") Long hackathonId, Pageable pageable);

    @Query("SELECT t FROM Team t WHERE t.hackathon.id = :hackathonId AND t.deleted = false " +
            "AND t.isPublic = true AND t.isActive = true AND t.isFull = false")
    Page<Team> findOpenTeamsByHackathon(@Param("hackathonId") Long hackathonId, Pageable pageable);

    @Query("SELECT t FROM Team t WHERE t.hackathon.id = :hackathonId AND t.deleted = false " +
            "AND t.isPublic = true AND t.isActive = true " +
            "AND (LOWER(t.teamName) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(t.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Team> searchTeamsByHackathon(@Param("hackathonId") Long hackathonId,
                                      @Param("search") String search, Pageable pageable);

    @Query("SELECT t FROM Team t JOIN t.skillsNeeded s WHERE t.hackathon.id = :hackathonId " +
            "AND t.deleted = false AND t.isPublic = true AND t.isActive = true " +
            "AND t.isFull = false AND s IN :userSkills")
    Page<Team> findTeamsWithMatchingSkills(@Param("hackathonId") Long hackathonId,
                                           @Param("userSkills") List<String> userSkills,
                                           Pageable pageable);

    @Query("SELECT t FROM Team t WHERE t.hackathon.id = :hackathonId AND t.deleted = false " +
            "AND t.isPublic = true AND t.isActive = true AND t.isFull = true")
    Page<Team> findFullTeamsByHackathon(@Param("hackathonId") Long hackathonId, Pageable pageable);

    @Query("SELECT t FROM Team t JOIN t.members m WHERE m.user.id = :userId " +
            "AND m.active = true AND t.deleted = false")
    List<Team> findTeamsByMember(@Param("userId") Long userId);

    @Query("SELECT t FROM Team t JOIN t.members m WHERE m.user.id = :userId " +
            "AND m.active = true AND t.deleted = false AND t.hackathon.id = :hackathonId")
    Optional<Team> findUserTeamInHackathon(@Param("userId") Long userId,
                                           @Param("hackathonId") Long hackathonId);

    @Query("SELECT COUNT(t) FROM Team t WHERE t.hackathon.id = :hackathonId AND t.deleted = false")
    long countTeamsByHackathon(@Param("hackathonId") Long hackathonId);

    @Query("SELECT COUNT(t) FROM Team t WHERE t.leader.id = :userId AND t.deleted = false")
    long countTeamsByLeader(@Param("userId") Long userId);

    @Query("SELECT COUNT(t) FROM Team t JOIN t.members m WHERE m.user.id = :userId " +
            "AND m.active = true AND t.deleted = false")
    long countTeamsByMember(@Param("userId") Long userId);

    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM Team t " +
            "JOIN t.members m WHERE m.user.id = :userId AND m.active = true " +
            "AND t.hackathon.id = :hackathonId AND t.deleted = false")
    boolean isUserInAnyTeamForHackathon(@Param("userId") Long userId,
                                        @Param("hackathonId") Long hackathonId);

    @Query("SELECT t FROM Team t WHERE t.deleted = false AND t.isActive = true " +
            "AND t.currentSize < t.maxSize")
    List<Team> findTeamsWithAvailableSlots();

    @Query("SELECT t FROM Team t WHERE t.hackathon.id = :hackathonId AND t.deleted = false " +
            "AND t.isPublic = true " +
            "AND (:status = 'ALL' OR " +
            "    (:status = 'OPEN' AND t.isActive = true AND t.isFull = false) OR " +
            "    (:status = 'FULL' AND t.isFull = true) OR " +
            "    (:status = 'CLOSED' AND t.isActive = false)) " +
            "AND (:search IS NULL OR " +
            "    LOWER(t.teamName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "    LOWER(t.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Team> findTeamsWithFilters(@Param("hackathonId") Long hackathonId,
                                    @Param("status") String status,
                                    @Param("search") String search,
                                    Pageable pageable);
}