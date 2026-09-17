package com.cinemaxpress.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class Movie {
    private Long id;
    private String title;
    private String description;
    private String genre;
    private Integer durationMinutes;
    private LocalDate releaseDate;
    private String posterUrl;
}