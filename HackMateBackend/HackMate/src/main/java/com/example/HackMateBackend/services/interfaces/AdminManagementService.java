package com.example.HackMateBackend.services.interfaces;

import com.example.HackMateBackend.dtos.adminmanagement.ChangeUserRoleRequestDto;
import com.example.HackMateBackend.dtos.adminmanagement.ChangeUserRoleResponseDto;
import com.example.HackMateBackend.dtos.adminmanagement.ListOfUsersDto;
import com.example.HackMateBackend.dtos.adminmanagement.UserRoleInfoDto;

import java.util.List;

public interface AdminManagementService {

    ChangeUserRoleResponseDto changeUserRole( ChangeUserRoleRequestDto request);

    UserRoleInfoDto getUserRoleInfo(Long userId);

    UserRoleInfoDto getUserRoleInfoByEmail(String email);

    ListOfUsersDto getAllAdmins();

    ListOfUsersDto getAllUsers();
}
