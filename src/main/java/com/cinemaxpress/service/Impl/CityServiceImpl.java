package com.cinemaxpress.service.Impl;

import com.cinemaxpress.dto.CityRequest;
import com.cinemaxpress.entity.City;
import com.cinemaxpress.exception.ResourceNotFoundException;
import com.cinemaxpress.repository.CityRepository;
import com.cinemaxpress.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;

    @Override
    public Page<City> getAllCities(Pageable pageable) {
        return cityRepository.findAll(pageable);
    }

    @Override
    public City getCityById(Long id) {

        return cityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("City not found with id: " + id));
    }

    @Override
    public City createCity(CityRequest cityRequest) {
        if(cityRequest.getName() == null || cityRequest.getState() == null || cityRequest.getCountry() == null) {
            throw new IllegalArgumentException("City name, state, and country cannot be null");
        }
        City city = new City();
        city.setName(cityRequest.getName());
        city.setState(cityRequest.getState());
        city.setCountry(cityRequest.getCountry());
        return cityRepository.save(city);
    }

    @Override
    public City updateCity(Long id, CityRequest cityRequest) {
        if(cityRequest.getName() == null || cityRequest.getState() == null || cityRequest.getCountry() == null) {
            throw new IllegalArgumentException("City name, state, and country cannot be null");
        }

        City city = getCityById(id);
        city.setName(cityRequest.getName());
        city.setState(cityRequest.getState());
        city.setCountry(cityRequest.getCountry());
        return cityRepository.save(city);
    }

    @Override
    public void deleteCity(Long id) {
        cityRepository.deleteById(id);
    }
}