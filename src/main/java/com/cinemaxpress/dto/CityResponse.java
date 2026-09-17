package com.cinemaxpress.dto;

import lombok.Data;

@Data
public class CityResponse {
    private Long id;
    private String name;
    private String state;
    private String country;
}