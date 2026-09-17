package com.cinemaxpress.dto;

import lombok.Data;

@Data
public class HallCreateRequest {
    private Long theatreId;
    private String name;
    private Integer capacity;
}