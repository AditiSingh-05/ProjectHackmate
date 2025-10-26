package com.example.HackMateBackend.controllers;


import com.example.HackMateBackend.dtos.adminmanagement.ChangeUserRoleRequestDto;
import com.example.HackMateBackend.dtos.adminmanagement.ChangeUserRoleResponseDto;
import com.example.HackMateBackend.dtos.adminmanagement.ListOfUsersDto;
import com.example.HackMateBackend.dtos.adminmanagement.UserRoleInfoDto;
import com.example.HackMateBackend.services.implementations.AdminManagementServiceImpl;
import com.example.HackMateBackend.services.implementations.CustomUserDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('CREATOR')")
@RequestMapping("/api/creator")
public class CreatorController{

    @Autowired
    private AdminManagementServiceImpl adminManagementService;
    @PostMapping("/change-role")
    public ResponseEntity<ChangeUserRoleResponseDto> changeUserRole(
            @Valid @RequestBody ChangeUserRoleRequestDto request
            ){
        try{
            ChangeUserRoleResponseDto response = adminManagementService.changeUserRole(
                    request
            );
            return ResponseEntity.ok(response);
        }catch(Exception e){
            return ResponseEntity
                    .badRequest()
                    .body(
                            new ChangeUserRoleResponseDto(
                                    false,
                                    e.getMessage(),
                                    null,
                                    null,
                                    null,
                                    null
                            )
                    );
        }
    }

    public ResponseEntity<UserRoleInfoDto> getUserRoleInfo(
             Long userId
    ){
        try{
            UserRoleInfoDto response = adminManagementService.getUserRoleInfo(userId);
            return ResponseEntity.ok(response);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(
                    new UserRoleInfoDto(
                            "An error occurred while fetching the data: " + e.getMessage(),
                            null,
                            false,
                            false,
                            null,
                            null
                    )
            );
        }
    }

    public ResponseEntity<UserRoleInfoDto> getUserRoleInfoByEmail(
            String email
    ){
        try{
            UserRoleInfoDto response = adminManagementService.getUserRoleInfoByEmail(email);
            return ResponseEntity.ok(response);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(
                    new UserRoleInfoDto(
                            "An error occurred while fetching the data: " + e.getMessage(),
                            null,
                            false,
                            false,
                            null,
                            null
                    )
            );
        }
    }

    public ResponseEntity<ListOfUsersDto> getAllAdmins(){
        try{
            ListOfUsersDto response = adminManagementService.getAllAdmins();
            return ResponseEntity.ok(response);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(
                    null
            );
        }
    }

    public ResponseEntity<ListOfUsersDto> getAllUsers(){
        try{
            ListOfUsersDto response = adminManagementService.getAllUsers();
            return ResponseEntity.ok(response);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(
                    null
            );
        }
    }



}

