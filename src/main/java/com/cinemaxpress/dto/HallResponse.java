package com.cinemaxpress.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HallResponse {
    private Long id;
    private Long theatreId;
    private String theatreName;
    private String name;
    private Integer capacity;
    private String status;
}
