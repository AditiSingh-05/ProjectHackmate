package com.example.HackMateBackend.services.implementations;

import com.example.HackMateBackend.data.entities.User;
import com.example.HackMateBackend.data.enums.Roles;
import com.example.HackMateBackend.dtos.adminmanagement.ListOfUsersDto;
import com.example.HackMateBackend.repositories.UserRepository;
import com.example.HackMateBackend.services.interfaces.AdminManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.HackMateBackend.dtos.adminmanagement.ChangeUserRoleRequestDto;
import com.example.HackMateBackend.dtos.adminmanagement.ChangeUserRoleResponseDto;
import com.example.HackMateBackend.dtos.adminmanagement.UserRoleInfoDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminManagementServiceImpl implements AdminManagementService {

    private final UserRepository userRepository;

    @Override
    public ChangeUserRoleResponseDto changeUserRole(
            ChangeUserRoleRequestDto request
    ) {
        Optional<User> user = userRepository.findByEmailIgnoreCase(request.getEmail());
        if(user.isPresent()){
            Roles previousRole = user.get().getRole();
            if(previousRole == request.getNewRole()){
                throw new RuntimeException("User already has the role: " + request.getNewRole());
            }
            user.get().setRole(request.getNewRole());
            user.get().setUpdatedAt(LocalDateTime.now());
            userRepository.save(user.get());
            return new ChangeUserRoleResponseDto(
                    true,
                    "User role updated successfully",
                    user.get().getEmail(),
                    previousRole,
                    request.getNewRole(),
                    LocalDateTime.now()
            );
        }else{
            throw new RuntimeException("User not found with email: " + request.getEmail());
        }
    }

    @Override
    public UserRoleInfoDto getUserRoleInfo(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            return mapToUSerRoleInfoDto(user.get());
        }else{
            throw new RuntimeException("User not found with the id " + userId);
        }


    }

    @Override
    public UserRoleInfoDto getUserRoleInfoByEmail(String email) {
        Optional<User> user = userRepository.findByEmailIgnoreCase(email);
        if (user.isPresent()) {
            return mapToUSerRoleInfoDto(user.get());
        }else{
            throw new RuntimeException("User not found with the email:" + email);
        }
    }

    @Override
    public ListOfUsersDto getAllAdmins() {
        List<User> admins = userRepository.findByRole(Roles.ADMIN);
        return mapToListOfUsersDto(admins);

    }

    @Override
    public ListOfUsersDto getAllUsers() {
        List<User> users = userRepository.findByRole(Roles.USER);
        return mapToListOfUsersDto(users);
    }


    public UserRoleInfoDto mapToUSerRoleInfoDto(User user){
        return new UserRoleInfoDto(
                user.getEmail(),
                user.getRole(),
                user.isEmailVerified(),
                user.isProfileSetup(),
                user.getCreatedAt(),
                user.getLastLoginAt()
        );
    }

    public ListOfUsersDto mapToListOfUsersDto(List<User> users){
        List<UserRoleInfoDto> userRoleInfoDtos = users.stream()
                .map(this::mapToUSerRoleInfoDto)
                .toList();
        return new ListOfUsersDto(userRoleInfoDtos);
    }
}
