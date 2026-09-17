package com.cinemaxpress.service;

import com.cinemaxpress.dto.CityRequest;
import com.cinemaxpress.entity.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CityService {
    Page<City> getAllCities(Pageable pageable);
    City getCityById(Long id);
    City createCity(CityRequest cityCreateRequest);
    City updateCity(Long id, CityRequest cityRequest);
    void deleteCity(Long id);
}
