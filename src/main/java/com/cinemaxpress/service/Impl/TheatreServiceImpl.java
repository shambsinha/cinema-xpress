package com.cinemaxpress.service.Impl;

import com.cinemaxpress.dto.TheatreRequest;
import com.cinemaxpress.dto.TheatreResponse;
import com.cinemaxpress.entity.City;
import com.cinemaxpress.entity.Theatre;
import com.cinemaxpress.entity.User;
import com.cinemaxpress.enums.Role;
import com.cinemaxpress.exception.BusinessException;
import com.cinemaxpress.exception.ResourceNotFoundException;
import com.cinemaxpress.repository.CityRepository;
import com.cinemaxpress.repository.TheatreRepository;
import com.cinemaxpress.repository.UserRepository;
import com.cinemaxpress.service.TheatreService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TheatreServiceImpl implements TheatreService {

    private final TheatreRepository theatreRepository;
    private final CityRepository cityRepository;
    private final UserRepository userRepository;

    @Override
    @Cacheable(cacheNames = "theatresCache", key = "'all:' + #pageable.pageNumber + ':' + #pageable.pageSize")
    public Page<TheatreResponse> getAllTheatres(Pageable pageable) {
        return theatreRepository.findAll(pageable).map(this::toResponse);
    }

    @Override
    @Cacheable(cacheNames = "theatresCache", key = "'city:' + #cityId")
    public List<TheatreResponse> getTheatresByCity(Long cityId) {
        return theatreRepository.findByCityId(cityId).stream().map(this::toResponse).toList();
    }

    @Override
    @Cacheable(cacheNames = "theatresCache", key = "#id")
    public TheatreResponse getTheatreById(Long id) {
        return toResponse(findTheatreOrThrow(id));
    }

    @Override
    @CacheEvict(cacheNames = "theatresCache", allEntries = true)
    public TheatreResponse createTheatre(TheatreRequest request) {
        Theatre theatre = new Theatre();
        applyRequest(theatre, request);
        return toResponse(theatreRepository.save(theatre));
    }

    @Override
    @CacheEvict(cacheNames = "theatresCache", allEntries = true)
    public TheatreResponse updateTheatre(Long id, TheatreRequest request) {
        Theatre theatre = findTheatreOrThrow(id);
        applyRequest(theatre, request);
        return toResponse(theatreRepository.save(theatre));
    }

    @Override
    @CacheEvict(cacheNames = "theatresCache", allEntries = true)
    public void deleteTheatre(Long id) {
        if (!theatreRepository.existsById(id)) {
            throw new ResourceNotFoundException("Theatre not found with id: " + id);
        }
        theatreRepository.deleteById(id);
    }

    private void applyRequest(Theatre theatre, TheatreRequest request) {
        City city = cityRepository.findById(request.getCityId())
                .orElseThrow(() -> new ResourceNotFoundException("City not found with id: " + request.getCityId()));

        User manager = userRepository.findById(request.getManagerId())
                .orElseThrow(() -> new ResourceNotFoundException("Manager not found with id: " + request.getManagerId()));
        if (manager.getRole() != Role.MANAGER) {
            throw new BusinessException("Assigned user must have the MANAGER role");
        }

        theatre.setCity(city);
        theatre.setManager(manager);
        theatre.setName(request.getName());
        theatre.setAddress(request.getAddress());
        if (request.getStatus() != null) {
            theatre.setStatus(request.getStatus());
        }
    }

    private Theatre findTheatreOrThrow(Long id) {
        return theatreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theatre not found with id: " + id));
    }

    private TheatreResponse toResponse(Theatre theatre) {
        return TheatreResponse.builder()
                .id(theatre.getId())
                .cityId(theatre.getCity().getId())
                .cityName(theatre.getCity().getName())
                .managerId(theatre.getManager().getId())
                .managerName(theatre.getManager().getName())
                .name(theatre.getName())
                .address(theatre.getAddress())
                .status(theatre.getStatus())
                .build();
    }
}
