package com.example.HackMateBackend.services.implementations;

import com.example.HackMateBackend.data.entities.User;
import com.example.HackMateBackend.data.enums.Roles;
import com.example.HackMateBackend.repositories.UserRepository;
import com.example.HackMateBackend.services.interfaces.AdminManagementService;
import lombok.RequiredArgsConstructor;
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
            Long creatorId,
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
            return new UserRoleInfoDto(
                    user.get().getEmail(),
                    user.get().getRole(),
                    user.get().isEmailVerified(),
                    user.get().isProfileSetup(),
                    user.get().getCreatedAt(),
                    user.get().getLastLoginAt()
            );
        }else{
            throw new RuntimeException("User not found with the id " + userId);
        }


    }

    @Override
    public UserRoleInfoDto getUserRoleInfoByEmail(String email) {
        return null;
    }

    @Override
    public List<UserRoleInfoDto> getAllAdmins() {
        return List.of();
    }

    @Override
    public List<UserRoleInfoDto> getAllUsers() {
        return List.of();
    }
}
