package com.example.HackMateBackend.repositories;

import com.example.HackMateBackend.data.entities.Review;
import com.example.HackMateBackend.data.entities.Profile;
import com.example.HackMateBackend.data.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByReviewedProfileAndDeletedFalseOrderByCreatedAtDesc(Profile reviewedProfile);

    @Query("SELECT r FROM Review r WHERE r.reviewedProfile.user.id = :userId AND r.isPublic = true " +
            "AND r.deleted = false ORDER BY r.createdAt DESC")
    List<Review> findPublicReviewsByUser(@Param("userId") Long userId);

    List<Review> findByReviewerAndDeletedFalseOrderByCreatedAtDesc(User reviewer);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.reviewedProfile.user.id = :userId AND r.deleted = false")
    long countReviewsByUser(@Param("userId") Long userId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.reviewedProfile.user.id = :userId AND r.deleted = false")
    Double getAverageRatingByUser(@Param("userId") Long userId);

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Review r " +
            "WHERE r.reviewer.id = :reviewerId AND r.reviewedProfile.user.id = :reviewedUserId " +
            "AND r.deleted = false")
    boolean hasReviewedUser(@Param("reviewerId") Long reviewerId, @Param("reviewedUserId") Long reviewedUserId);

    @Query("SELECT r FROM Review r WHERE r.reviewer = :reviewer AND r.reviewedProfile.user = :reviewedUser AND r.deleted = false")
    Optional<Review> findByReviewerAndReviewedUserAndDeletedFalse(@Param("reviewer") User reviewer, @Param("reviewedUser") User reviewedUser);
}
