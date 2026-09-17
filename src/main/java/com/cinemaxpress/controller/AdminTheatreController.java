package com.cinemaxpress.controller;

import com.cinemaxpress.entity.Theatre;
import com.cinemaxpress.exception.ResourceNotFoundException;
import com.cinemaxpress.repository.TheatreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/theatres")
@RequiredArgsConstructor
public class AdminTheatreController {

    private final TheatreRepository theatreRepository;

    @GetMapping
    public ResponseEntity<List<Theatre>> getAllTheatres() {
        return ResponseEntity.ok(theatreRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Theatre> getTheatreById(@PathVariable Long id) {
        return theatreRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Theatre not found with id: " + id));
    }

    @PostMapping
    public ResponseEntity<Theatre> createTheatre(@RequestBody Theatre theatre) {
        return ResponseEntity.status(HttpStatus.CREATED).body(theatreRepository.save(theatre));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Theatre> updateTheatre(@PathVariable Long id, @RequestBody Theatre theatreDetails) {
        Theatre theatre = theatreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theatre not found with id: " + id));
        theatre.setName(theatreDetails.getName());
        theatre.setCity(theatreDetails.getCity());
        theatre.setAddress(theatreDetails.getAddress());
        theatre.setManager(theatreDetails.getManager());
        return ResponseEntity.ok(theatreRepository.save(theatre));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheatre(@PathVariable Long id) {
        if (!theatreRepository.existsById(id)) {
            throw new ResourceNotFoundException("Theatre not found with id: " + id);
        }
        theatreRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
