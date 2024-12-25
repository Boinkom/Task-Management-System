package com.example.project.dto;

import com.example.project.enumiration.Role;
import com.example.project.enumiration.TaskStatus;
import lombok.Data;

import java.util.List;


@Data
public class UserDTO {

    private String email;
    private String password;
    private Role role;
}