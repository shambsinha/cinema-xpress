package com.cinemaxpress.controller;

import com.cinemaxpress.dto.HallCreateRequest;
import com.cinemaxpress.dto.ShowCreateRequest;
import com.cinemaxpress.dto.ShowResponse;
import com.cinemaxpress.dto.ShowSeatResponse;
import com.cinemaxpress.entity.Hall;
import com.cinemaxpress.entity.Show;
import com.cinemaxpress.entity.ShowSeat;
import com.cinemaxpress.service.HallService;
import com.cinemaxpress.service.ShowService;
import com.cinemaxpress.repository.ShowSeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final HallService hallService;
    private final ShowService showService;
    private final ShowSeatRepository showSeatRepository;

    @PostMapping("/halls")
    public ResponseEntity<Hall> createHall(@RequestBody HallCreateRequest request) {
        Hall createdHall = hallService.createHallWithSeats(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdHall);
    }

    @PostMapping("/shows")
    public ResponseEntity<ShowResponse> scheduleShow(@RequestBody ShowCreateRequest request) {
        Show show = showService.createShow(request);
        ShowResponse response = ShowResponse.builder()
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
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/shows/{showId}/occupancy")
    public ResponseEntity<List<ShowSeatResponse>> getShowOccupancy(@PathVariable Long showId) {
        List<ShowSeat> seats = showSeatRepository.findByShowId(showId);
        List<ShowSeatResponse> response = seats.stream().map(seat -> ShowSeatResponse.builder()
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
