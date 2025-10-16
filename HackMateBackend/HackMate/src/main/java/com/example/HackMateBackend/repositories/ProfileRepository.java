package com.example.HackMateBackend.repositories;

import com.example.HackMateBackend.data.entities.Profile;
import com.example.HackMateBackend.data.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    Optional<Profile> findByUserAndDeletedFalse(User user);

    Optional<Profile> findByUser_IdAndDeletedFalse(Long userId);

    @Query("SELECT p FROM Profile p WHERE p.user.id = :userId AND p.deleted = false")
    Optional<Profile> findByUserId(@Param("userId") Long userId);

    // Search profiles by skills
    @Query("SELECT p FROM Profile p JOIN p.skills s WHERE s IN :skills AND p.deleted = false")
    List<Profile> findBySkillsIn(@Param("skills") List<String> skills);

    // Search profiles by college
    @Query("SELECT p FROM Profile p WHERE LOWER(p.college) LIKE LOWER(CONCAT('%', :college, '%')) AND p.deleted = false")
    List<Profile> findByCollegeLikeIgnoreCase(@Param("college") String college);

    // Find profiles by main skill
    List<Profile> findByMainSkillAndDeletedFalse(String mainSkill);

    // Find top rated profiles
    @Query("SELECT p FROM Profile p WHERE p.deleted = false AND p.totalReviews > 0 ORDER BY p.averageRating DESC")
    List<Profile> findTopRatedProfiles();

    // Find profiles with specific criteria
    @Query("SELECT p FROM Profile p WHERE p.deleted = false " +
            "AND p.hackathonsParticipated >= :minHackathons " +
            "ORDER BY p.hackathonsParticipated DESC")
    List<Profile> findExperiencedProfiles(@Param("minHackathons") int minHackathons);

    // Statistics
    @Query("SELECT COUNT(p) FROM Profile p WHERE p.deleted = false")
    long countActiveProfiles();

    @Query("SELECT AVG(p.averageRating) FROM Profile p WHERE p.deleted = false AND p.totalReviews > 0")
    Double getAverageRatingAcrossAllProfiles();
}