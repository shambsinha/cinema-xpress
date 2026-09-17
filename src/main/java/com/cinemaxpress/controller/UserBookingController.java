package com.cinemaxpress.controller;

import com.cinemaxpress.config.SecurityConfig;
import com.cinemaxpress.dto.BookingRequest;
import com.cinemaxpress.dto.BookingResponse;
import com.cinemaxpress.entity.Booking;
import com.cinemaxpress.service.BookingService;
import com.cinemaxpress.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/bookings")
@RequiredArgsConstructor
public class UserBookingController {

    private final BookingService bookingService;
    private final BookingRepository bookingRepository;

    @PostMapping
    public ResponseEntity<BookingResponse> bookTickets(@RequestBody BookingRequest request) {
        BookingResponse bookingResponse = bookingService.bookTickets(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingResponse);
    }

    @GetMapping("/history")
    public ResponseEntity<List<Booking>> getUserBookingHistory() {
        List<Booking> bookings = bookingService.getUserBookingHistory();
        return ResponseEntity.ok(bookings);
    }
}
