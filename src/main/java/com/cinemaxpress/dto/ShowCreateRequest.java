package com.cinemaxpress.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ShowCreateRequest {
    private Long movieId;
    private Long hallId;
    private String language;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal basePrice;
}