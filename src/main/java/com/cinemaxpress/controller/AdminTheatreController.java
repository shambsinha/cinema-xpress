package com.cinemaxpress.controller;

import com.cinemaxpress.dto.TheatreRequest;
import com.cinemaxpress.dto.TheatreResponse;
import com.cinemaxpress.service.TheatreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/theatres")
@RequiredArgsConstructor
public class AdminTheatreController {

    private final TheatreService theatreService;

    @GetMapping
    public ResponseEntity<Page<TheatreResponse>> getAllTheatres(Pageable pageable) {
        return ResponseEntity.ok(theatreService.getAllTheatres(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TheatreResponse> getTheatreById(@PathVariable Long id) {
        return ResponseEntity.ok(theatreService.getTheatreById(id));
    }

    @PostMapping
    public ResponseEntity<TheatreResponse> createTheatre(@RequestBody TheatreRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(theatreService.createTheatre(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TheatreResponse> updateTheatre(@PathVariable Long id, @RequestBody TheatreRequest request) {
        return ResponseEntity.ok(theatreService.updateTheatre(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheatre(@PathVariable Long id) {
        theatreService.deleteTheatre(id);
        return ResponseEntity.noContent().build();
    }
}
