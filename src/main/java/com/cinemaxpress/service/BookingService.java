package com.cinemaxpress.service;

import com.cinemaxpress.dto.BookingRequest;
import com.cinemaxpress.dto.BookingResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookingService {
    BookingResponse bookTickets(BookingRequest request);
    Page<BookingResponse> getUserBookingHistory(Pageable pageable);
}
