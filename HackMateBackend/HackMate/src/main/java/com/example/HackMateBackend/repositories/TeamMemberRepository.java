package com.example.HackMateBackend.repositories;

import com.example.HackMateBackend.data.entities.Team;
import com.example.HackMateBackend.data.entities.TeamMember;
import com.example.HackMateBackend.data.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {

    List<TeamMember> findByTeamAndActiveTrue(Team team);

    List<TeamMember> findByUserAndActiveTrue(User user);

    Optional<TeamMember> findByTeamAndUserAndActiveTrue(Team team, User user);

    @Query("SELECT tm FROM TeamMember tm WHERE tm.team.id = :teamId AND tm.user.id = :userId AND tm.active = true AND tm.deleted = false")
    Optional<TeamMember> findActiveByTeamAndUser(@Param("teamId") Long teamId, @Param("userId") Long userId);

    @Query("SELECT COUNT(tm) FROM TeamMember tm WHERE tm.team.id = :teamId AND tm.active = true AND tm.deleted = false")
    long countActiveByTeam(@Param("teamId") Long teamId);

    @Query("SELECT tm FROM TeamMember tm WHERE tm.user.id = :userId AND tm.active = true AND tm.deleted = false")
    List<TeamMember> findActiveByUser(@Param("userId") Long userId);

    boolean existsByTeamAndUserAndActiveTrue(Team team, User user);
}