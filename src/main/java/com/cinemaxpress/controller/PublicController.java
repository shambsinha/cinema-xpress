package com.cinemaxpress.controller;

import com.cinemaxpress.dto.CityResponse;
import com.cinemaxpress.dto.MovieResponse;
import com.cinemaxpress.dto.ShowResponse;
import com.cinemaxpress.dto.ShowSeatResponse;
import com.cinemaxpress.dto.TheatreResponse;
import com.cinemaxpress.service.CityService;
import com.cinemaxpress.service.MovieService;
import com.cinemaxpress.service.ShowService;
import com.cinemaxpress.service.TheatreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class PublicController {

    private final MovieService movieService;
    private final ShowService showService;
    private final CityService cityService;
    private final TheatreService theatreService;

    @GetMapping("/cities")
    public ResponseEntity<Page<CityResponse>> getAllCities(Pageable pageable) {
        return ResponseEntity.ok(cityService.getAllCities(pageable));
    }

    @GetMapping("/theatres")
    public ResponseEntity<List<TheatreResponse>> getTheatresByCity(@RequestParam Long cityId) {
        return ResponseEntity.ok(theatreService.getTheatresByCity(cityId));
    }

    @GetMapping("/movies")
    public ResponseEntity<Page<MovieResponse>> getAllMovies(Pageable pageable) {
        return ResponseEntity.ok(movieService.getActiveMovies(pageable));
    }

    @GetMapping("/movies/{movieId}/shows")
    public ResponseEntity<List<ShowResponse>> getShowsForMovie(@PathVariable Long movieId) {
        return ResponseEntity.ok(showService.getShowsForMovie(movieId));
    }

    @GetMapping("/shows/{showId}/seats")
    public ResponseEntity<List<ShowSeatResponse>> getShowSeats(@PathVariable Long showId) {
        return ResponseEntity.ok(showService.getShowSeats(showId));
    }
}
