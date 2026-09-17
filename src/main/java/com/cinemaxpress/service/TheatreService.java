package com.cinemaxpress.service;

import com.cinemaxpress.dto.TheatreRequest;
import com.cinemaxpress.dto.TheatreResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TheatreService {
    Page<TheatreResponse> getAllTheatres(Pageable pageable);
    List<TheatreResponse> getTheatresByCity(Long cityId);
    TheatreResponse getTheatreById(Long id);
    TheatreResponse createTheatre(TheatreRequest request);
    TheatreResponse updateTheatre(Long id, TheatreRequest request);
    void deleteTheatre(Long id);
}
