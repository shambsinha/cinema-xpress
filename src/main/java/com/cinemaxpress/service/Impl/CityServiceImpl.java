package com.cinemaxpress.service.Impl;

import com.cinemaxpress.dto.CityRequest;
import com.cinemaxpress.dto.CityResponse;
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
    public Page<CityResponse> getAllCities(Pageable pageable) {
        return cityRepository.findAll(pageable).map(this::toResponse);
    }

    @Override
    public CityResponse getCityById(Long id) {
        return toResponse(findCityOrThrow(id));
    }

    @Override
    public CityResponse createCity(CityRequest cityRequest) {
        if (cityRequest.getName() == null || cityRequest.getState() == null || cityRequest.getCountry() == null) {
            throw new IllegalArgumentException("City name, state, and country cannot be null");
        }
        City city = new City();
        city.setName(cityRequest.getName());
        city.setState(cityRequest.getState());
        city.setCountry(cityRequest.getCountry());
        return toResponse(cityRepository.save(city));
    }

    @Override
    public CityResponse updateCity(Long id, CityRequest cityRequest) {
        if (cityRequest.getName() == null || cityRequest.getState() == null || cityRequest.getCountry() == null) {
            throw new IllegalArgumentException("City name, state, and country cannot be null");
        }

        City city = findCityOrThrow(id);
        city.setName(cityRequest.getName());
        city.setState(cityRequest.getState());
        city.setCountry(cityRequest.getCountry());
        return toResponse(cityRepository.save(city));
    }

    @Override
    public void deleteCity(Long id) {
        if (!cityRepository.existsById(id)) {
            throw new ResourceNotFoundException("City not found with id: " + id);
        }
        cityRepository.deleteById(id);
    }

    private City findCityOrThrow(Long id) {
        return cityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("City not found with id: " + id));
    }

    private CityResponse toResponse(City city) {
        CityResponse response = new CityResponse();
        response.setId(city.getId());
        response.setName(city.getName());
        response.setState(city.getState());
        response.setCountry(city.getCountry());
        return response;
    }
}
