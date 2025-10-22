package com.example.HackMateBackend.services.interfaces;

import com.example.HackMateBackend.dtos.authentication.SignupResponseDto;
import com.example.HackMateBackend.dtos.authentication.SignupRequestDto;
import com.example.HackMateBackend.dtos.authentication.LoginResponseDto;
import com.example.HackMateBackend.dtos.authentication.LoginRequestDto;
import com.example.HackMateBackend.dtos.authentication.ChangePasswordResponseDto;
import com.example.HackMateBackend.dtos.authentication.ChangePasswordRequestDto;
import com.example.HackMateBackend.dtos.authentication.ForgotPasswordResponseDto;
import com.example.HackMateBackend.dtos.authentication.ForgotPasswordRequestDto;
import com.example.HackMateBackend.dtos.authentication.ResetPasswordResponseDto;
import com.example.HackMateBackend.dtos.authentication.ResetPasswordRequestDto;
import com.example.HackMateBackend.dtos.authentication.EmailVerificationResponseDto;
import com.example.HackMateBackend.dtos.authentication.EmailVerificationRequestDto;
import com.example.HackMateBackend.dtos.authentication.ProfileSetupStatusDto;

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


    boolean isEmailVerified(String email);


    void updateLastLogin(Long userId);
}