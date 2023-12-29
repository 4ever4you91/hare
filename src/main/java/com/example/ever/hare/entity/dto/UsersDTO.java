package com.example.ever.hare.entity.dto;

import com.example.ever.hare.entity.Role;
import lombok.Data;

@Data
public class UsersDTO {
    private Long id;
    private String userName;
    private Role role;
    private Boolean isActive;
    private SessionDTO session;
}
