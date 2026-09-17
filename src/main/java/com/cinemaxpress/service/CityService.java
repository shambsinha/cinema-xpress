package com.cinemaxpress.service;

import com.cinemaxpress.dto.CityRequest;
import com.cinemaxpress.dto.CityResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CityService {
    Page<CityResponse> getAllCities(Pageable pageable);
    CityResponse getCityById(Long id);
    CityResponse createCity(CityRequest cityCreateRequest);
    CityResponse updateCity(Long id, CityRequest cityRequest);
    void deleteCity(Long id);
}
