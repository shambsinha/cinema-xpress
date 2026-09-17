package com.cinemaxpress.dto;

import lombok.Builder;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
public class MovieResponse implements Serializable {
    private Long id;
    private String title;
    private String description;
    private String genre;
    private Integer durationMinutes;
    private LocalDate releaseDate;
    private String certification;
    private String posterUrl;
    private String status;
}
