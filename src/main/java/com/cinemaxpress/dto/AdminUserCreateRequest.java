package com.cinemaxpress.dto;

import lombok.Data;

@Data
public class AdminUserCreateRequest {
    private String name;
    private String email;
    private String phone;
    private String password;
    private String role;
    private String status;
}
