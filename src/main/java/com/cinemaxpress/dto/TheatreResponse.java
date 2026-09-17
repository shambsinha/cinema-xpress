package com.cinemaxpress.dto;

import lombok.Builder;
import lombok.Data;
import java.io.Serializable;

@Data
@Builder
public class TheatreResponse implements Serializable {
    private Long id;
    private Long cityId;
    private String cityName;
    private Long managerId;
    private String managerName;
    private String name;
    private String address;
    private String status;
}
