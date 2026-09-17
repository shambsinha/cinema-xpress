package com.cinemaxpress.dto;

import lombok.Data;

@Data
public class TheatreRequest {
    private Long cityId;
    private Long managerId;
    private String name;
    private String address;
    private String status;
}
