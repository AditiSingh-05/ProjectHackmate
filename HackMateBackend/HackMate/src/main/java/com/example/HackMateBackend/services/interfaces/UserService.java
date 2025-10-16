package com.example.HackMateBackend.services.interfaces;

import com.example.HackMateBackend.dtos.AuthenticationDto.*;
import com.example.HackMateBackend.data.entities.User;

import java.util.Optional;


public interface UserService {


    SignupResponseDto registerUser(SignupRequestDto signupRequest);


    LoginResponseDto loginUser(LoginRequestDto loginRequest);


    ChangePasswordResponseDto changePassword(Long userId, ChangePasswordRequestDto request);


    ForgotPasswordResponseDto forgotPassword(ForgotPasswordRequestDto request);


    ResetPasswordResponseDto resetPassword(ResetPasswordRequestDto request);


    EmailVerificationResponseDto verifyEmail(EmailVerificationRequestDto request);


    EmailVerificationResponseDto resendEmailVerification(String email);


    ProfileSetupStatusDto getProfileSetupStatus(Long userId);


    Optional<User> findByEmail(String email);


    Optional<User> findById(Long id);


    boolean emailExists(String email);


    boolean validateResetToken(String token);


    void updateLastLogin(Long userId);
}