package com.cinemaxpress.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ShowResponse {
    private Long showId;
    private Long movieId;
    private String movieTitle;
    private String moviePosterUrl;
    private String movieCertification;
    private Long hallId;
    private String hallName;
    private String theatreName;
    private String city;
    private String language;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal basePrice;
    private Integer availableSeats;
    private String status;
}
