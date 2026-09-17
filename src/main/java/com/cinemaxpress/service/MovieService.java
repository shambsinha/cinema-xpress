package com.cinemaxpress.service;

import com.cinemaxpress.dto.MovieRequest;
import com.cinemaxpress.dto.MovieResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MovieService {
    Page<MovieResponse> getAllMovies(Pageable pageable);
    Page<MovieResponse> getActiveMovies(Pageable pageable);
    MovieResponse getMovieById(Long id);
    MovieResponse createMovie(MovieRequest request);
    MovieResponse updateMovie(Long id, MovieRequest request);
    void deleteMovie(Long id);
}
