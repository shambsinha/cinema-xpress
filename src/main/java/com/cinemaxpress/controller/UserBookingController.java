package com.cinemaxpress.controller;

import com.cinemaxpress.dto.BookingRequest;
import com.cinemaxpress.dto.BookingResponse;
import com.cinemaxpress.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/bookings")
@RequiredArgsConstructor
public class UserBookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponse> bookTickets(@RequestBody BookingRequest request) {
        BookingResponse bookingResponse = bookingService.bookTickets(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingResponse);
    }

    @GetMapping("/history")
    public ResponseEntity<Page<BookingResponse>> getUserBookingHistory(Pageable pageable) {
        return ResponseEntity.ok(bookingService.getUserBookingHistory(pageable));
    }
}
