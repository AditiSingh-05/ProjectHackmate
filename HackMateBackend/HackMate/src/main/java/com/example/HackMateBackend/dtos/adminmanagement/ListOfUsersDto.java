package com.example.HackMateBackend.dtos.adminmanagement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListOfUsersDto {
    List<UserRoleInfoDto> users;


}
