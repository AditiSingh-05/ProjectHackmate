package com.example.HackMateBackend.dtos.authentication;

import com.example.HackMateBackend.data.enums.Roles;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SignupResponseDto {
    private Long userId;
    private String email;
    private String message;
    private Roles role;
    private boolean isEmailVerified;
    private String verificationEmailSent;
}

