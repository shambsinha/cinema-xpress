package com.cinemaxpress.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String role;
    private String status;
    private LocalDateTime createdAt;
}
