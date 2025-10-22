package com.example.HackMateBackend.controllers;

import com.example.HackMateBackend.dtos.adminmanagement.ChangeUserRoleRequestDto;
import com.example.HackMateBackend.dtos.adminmanagement.ChangeUserRoleResponseDto;
import com.example.HackMateBackend.dtos.adminmanagement.GetUserByEmailRequestDto;
import com.example.HackMateBackend.dtos.adminmanagement.UserRoleInfoDto;
import com.example.HackMateBackend.services.implementations.CustomUserDetailService;
import com.example.HackMateBackend.services.interfaces.AdminManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/creator/admin_control")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('CREATOR')")
public class CreatorAdminController {

    private final AdminManagementService adminManagementService;

    @PostMapping("/change-role")
    public ResponseEntity<ChangeUserRoleResponseDto> changeUserRole(
            @Valid @RequestBody ChangeUserRoleRequestDto request,
            @AuthenticationPrincipal CustomUserDetailService.UserPrincipal userPrincipal) {

        log.info("Creator {} requesting role change for user {}", userPrincipal.getId(), request.getEmail());

        try {
            ChangeUserRoleResponseDto response = adminManagementService.changeUserRole(
                    userPrincipal.getId(), request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error changing user role: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ChangeUserRoleResponseDto(false, e.getMessage(), null, null, null, null));
        }
    }

    /**
     * Get role information for a specific user by user ID
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<UserRoleInfoDto> getUserRoleInfo(@PathVariable Long userId) {
        log.info("Fetching user role info for user ID: {}", userId);

        try {
            UserRoleInfoDto response = adminManagementService.getUserRoleInfo(userId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error fetching user role info: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Get role information for a specific user by email
     */
    @PostMapping("/user/by-email")
    public ResponseEntity<UserRoleInfoDto> getUserRoleInfoByEmail(
            @Valid @RequestBody GetUserByEmailRequestDto request) {
        log.info("Fetching user role info for email: {}", request.getEmail());

        try {
            UserRoleInfoDto response = adminManagementService.getUserRoleInfoByEmail(request.getEmail());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error fetching user role info: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Get list of all admins
     */
    @GetMapping("/admins")
    public ResponseEntity<List<UserRoleInfoDto>> getAllAdmins() {
        log.info("Fetching all admin users");

        try {
            List<UserRoleInfoDto> admins = adminManagementService.getAllAdmins();
            return ResponseEntity.ok(admins);
        } catch (Exception e) {
            log.error("Error fetching admins: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get list of all users with their roles
     */
    @GetMapping("/users")
    public ResponseEntity<List<UserRoleInfoDto>> getAllUsers() {
        log.info("Fetching all users");

        try {
            List<UserRoleInfoDto> users = adminManagementService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            log.error("Error fetching users: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
