package com.cinemaxpress.service.Impl;

import com.cinemaxpress.dto.MovieRequest;
import com.cinemaxpress.dto.MovieResponse;
import com.cinemaxpress.entity.Movie;
import com.cinemaxpress.exception.ResourceNotFoundException;
import com.cinemaxpress.repository.MovieRepository;
import com.cinemaxpress.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private static final String ACTIVE_STATUS = "ACTIVE";

    private final MovieRepository movieRepository;

    @Override
    @Cacheable(cacheNames = "moviesCache", key = "'all:' + #pageable.pageNumber + ':' + #pageable.pageSize")
    public Page<MovieResponse> getAllMovies(Pageable pageable) {
        return movieRepository.findAll(pageable).map(this::toResponse);
    }

    @Override
    @Cacheable(cacheNames = "moviesCache", key = "'active:' + #pageable.pageNumber + ':' + #pageable.pageSize")
    public Page<MovieResponse> getActiveMovies(Pageable pageable) {
        return movieRepository.findByStatus(ACTIVE_STATUS, pageable).map(this::toResponse);
    }

    @Override
    @Cacheable(cacheNames = "moviesCache", key = "#id")
    public MovieResponse getMovieById(Long id) {
        return toResponse(findMovieOrThrow(id));
    }

    @Override
    @CacheEvict(cacheNames = "moviesCache", allEntries = true)
    public MovieResponse createMovie(MovieRequest request) {
        Movie movie = new Movie();
        applyRequest(movie, request);
        return toResponse(movieRepository.save(movie));
    }

    @Override
    @CacheEvict(cacheNames = "moviesCache", allEntries = true)
    public MovieResponse updateMovie(Long id, MovieRequest request) {
        Movie movie = findMovieOrThrow(id);
        applyRequest(movie, request);
        return toResponse(movieRepository.save(movie));
    }

    @Override
    @CacheEvict(cacheNames = "moviesCache", allEntries = true)
    public void deleteMovie(Long id) {
        if (!movieRepository.existsById(id)) {
            throw new ResourceNotFoundException("Movie not found with id: " + id);
        }
        movieRepository.deleteById(id);
    }

    private void applyRequest(Movie movie, MovieRequest request) {
        movie.setTitle(request.getTitle());
        movie.setDescription(request.getDescription());
        movie.setGenre(request.getGenre());
        movie.setDurationMinutes(request.getDurationMinutes());
        movie.setReleaseDate(request.getReleaseDate());
        movie.setCertification(request.getCertification());
        movie.setPosterUrl(request.getPosterUrl());
        if (request.getStatus() != null) {
            movie.setStatus(request.getStatus());
        }
    }

    private Movie findMovieOrThrow(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + id));
    }

    private MovieResponse toResponse(Movie movie) {
        return MovieResponse.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .description(movie.getDescription())
                .genre(movie.getGenre())
                .durationMinutes(movie.getDurationMinutes())
                .releaseDate(movie.getReleaseDate())
                .certification(movie.getCertification())
                .posterUrl(movie.getPosterUrl())
                .status(movie.getStatus())
                .build();
    }
}
