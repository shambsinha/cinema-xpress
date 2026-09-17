package com.cinemaxpress.controller;

import com.cinemaxpress.dto.HallCreateRequest;
import com.cinemaxpress.dto.HallResponse;
import com.cinemaxpress.dto.ShowCreateRequest;
import com.cinemaxpress.dto.ShowResponse;
import com.cinemaxpress.dto.ShowSeatResponse;
import com.cinemaxpress.service.HallService;
import com.cinemaxpress.service.ShowService;
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

    @PostMapping("/halls")
    public ResponseEntity<HallResponse> createHall(@RequestBody HallCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(hallService.createHallWithSeats(request));
    }

    @PostMapping("/shows")
    public ResponseEntity<ShowResponse> scheduleShow(@RequestBody ShowCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(showService.createShow(request));
    }

    @GetMapping("/shows/{showId}/occupancy")
    public ResponseEntity<List<ShowSeatResponse>> getShowOccupancy(@PathVariable Long showId) {
        return ResponseEntity.ok(showService.getShowSeats(showId));
    }
}
