package com.cinemaxpress.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class MovieRequest {
    private String title;
    private String description;
    private String genre;
    private Integer durationMinutes;
    private LocalDate releaseDate;
    private String certification;
    private String posterUrl;
    private String status;
}
