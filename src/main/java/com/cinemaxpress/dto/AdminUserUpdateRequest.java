package com.cinemaxpress.dto;

import lombok.Data;

@Data
public class AdminUserUpdateRequest {
    private String name;
    private String phone;
    private String role;
    private String status;
}
