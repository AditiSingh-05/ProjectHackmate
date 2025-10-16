package com.example.HackMateBackend.repositories;

import com.example.HackMateBackend.data.entities.Hackathon;
import com.example.HackMateBackend.data.entities.HackathonRegistration;
import com.example.HackMateBackend.data.entities.User;
import com.example.HackMateBackend.data.enums.RegistrationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HackathonRegistrationRepository extends JpaRepository<HackathonRegistration, Long> {

    Optional<HackathonRegistration> findByHackathonAndUserAndDeletedFalse(Hackathon hackathon, User user);

    @Query("SELECT hr FROM HackathonRegistration hr WHERE hr.hackathon.id = :hackathonId " +
            "AND hr.user.id = :userId AND hr.deleted = false")
    Optional<HackathonRegistration> findByHackathonIdAndUserId(@Param("hackathonId") Long hackathonId,
                                                               @Param("userId") Long userId);

    List<HackathonRegistration> findByUserAndStatusAndDeletedFalse(User user, RegistrationStatus status);

    List<HackathonRegistration> findByHackathonAndStatusAndDeletedFalse(Hackathon hackathon, RegistrationStatus status);

    @Query("SELECT COUNT(hr) FROM HackathonRegistration hr WHERE hr.hackathon.id = :hackathonId " +
            "AND hr.status = :status AND hr.deleted = false")
    long countByHackathonAndStatus(@Param("hackathonId") Long hackathonId,
                                   @Param("status") RegistrationStatus status);

    @Query("SELECT CASE WHEN COUNT(hr) > 0 THEN true ELSE false END FROM HackathonRegistration hr " +
            "WHERE hr.hackathon.id = :hackathonId AND hr.user.id = :userId " +
            "AND hr.status = :status AND hr.deleted = false")
    boolean existsByHackathonAndUserAndStatus(@Param("hackathonId") Long hackathonId,
                                              @Param("userId") Long userId,
                                              @Param("status") RegistrationStatus status);
}