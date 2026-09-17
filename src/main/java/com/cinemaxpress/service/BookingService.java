package com.cinemaxpress.service;

import com.cinemaxpress.dto.BookingRequest;
import com.cinemaxpress.dto.BookingResponse;
import com.cinemaxpress.entity.Booking;
import java.util.List;

public interface BookingService {
    BookingResponse bookTickets(BookingRequest request);
    List<Booking> getUserBookingHistory();
}