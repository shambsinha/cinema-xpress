package com.cinemaxpress.controller;

import com.cinemaxpress.dto.ShowResponse;
import com.cinemaxpress.dto.ShowSeatResponse;
import com.cinemaxpress.entity.Movie;
import com.cinemaxpress.entity.Show;
import com.cinemaxpress.entity.ShowSeat;
import com.cinemaxpress.repository.MovieRepository;
import com.cinemaxpress.repository.ShowRepository;
import com.cinemaxpress.repository.ShowSeatRepository;
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

    private final MovieRepository movieRepository;
    private final ShowRepository showRepository;
    private final ShowSeatRepository showSeatRepository;

    @GetMapping("/movies")
    public ResponseEntity<Page<Movie>> getAllMovies(Pageable pageable) {
        return ResponseEntity.ok(movieRepository.findAll(pageable));
    }

    @GetMapping("/movies/{movieId}/shows")
    public ResponseEntity<List<ShowResponse>> getShowsForMovie(@PathVariable Long movieId) {
        List<Show> shows = showRepository.findByMovieId(movieId);
        List<ShowResponse> response = shows.stream().map(show -> ShowResponse.builder()
                .showId(show.getId())
                .movieId(show.getMovie().getId())
                .movieTitle(show.getMovie().getTitle())
                .moviePosterUrl(show.getMovie().getPosterUrl())
                .movieCertification(show.getMovie().getCertification())
                .hallId(show.getHall().getId())
                .hallName(show.getHall().getName())
                .theatreName(show.getHall().getTheatre().getName())
                .city(show.getHall().getTheatre().getCity().getName())
                .language(show.getLanguage())
                .startTime(show.getStartTime())
                .endTime(show.getEndTime())
                .basePrice(show.getBasePrice())
                .availableSeats(show.getAvailableSeats())
                .status(show.getStatus())
                .build()
        ).toList();
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/shows/{showId}/seats")
    public ResponseEntity<List<ShowSeatResponse>> getShowSeats(@PathVariable Long showId) {
        List<ShowSeat> showSeats = showSeatRepository.findByShowId(showId);
        List<ShowSeatResponse> response = showSeats.stream().map(seat -> ShowSeatResponse.builder()
                .id(seat.getId())
                .showId(seat.getShow().getId())
                .seatId(seat.getSeat().getId())
                .rowLabel(seat.getSeat().getRowLabel())
                .seatNumber(seat.getSeat().getSeatNumber())
                .price(seat.getPrice())
                .status(seat.getStatus().name())
                .build()
        ).toList();
        return ResponseEntity.ok(response);
    }
}