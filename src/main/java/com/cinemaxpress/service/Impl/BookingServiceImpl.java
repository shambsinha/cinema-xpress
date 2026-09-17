package com.cinemaxpress.service.Impl;

import com.cinemaxpress.config.SecurityConfig;
import com.cinemaxpress.dto.BookingRequest;
import com.cinemaxpress.dto.BookingResponse;
import com.cinemaxpress.entity.*;
import com.cinemaxpress.enums.BookingStatus;
import com.cinemaxpress.enums.ShowSeatStatus;
import com.cinemaxpress.exception.BusinessException;
import com.cinemaxpress.exception.ResourceNotFoundException;
import com.cinemaxpress.repository.*;
import com.cinemaxpress.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final UserRepository userRepository;
    private final ShowRepository showRepository;
    private final ShowSeatRepository showSeatRepository;
    private final BookingRepository bookingRepository;
    private final BookingSeatRepository bookingSeatRepository;

    @Override
    @Transactional
    public BookingResponse bookTickets(BookingRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Show show = showRepository.findByIdForUpdate(request.getShowId())
                .orElseThrow(() -> new ResourceNotFoundException("Show not found"));

        List<ShowSeat> selectedSeats = showSeatRepository.findByIdsForUpdate(request.getSelectedShowSeatIds());

        if (selectedSeats.size() != request.getSelectedShowSeatIds().size()) {
            throw new BusinessException("One or more seats are invalid.");
        }

        for (ShowSeat seat : selectedSeats) {
            if (!seat.getShow().getId().equals(show.getId())) {
                throw new BusinessException("Seat does not belong to this show.");
            }
            if (seat.getStatus() != ShowSeatStatus.AVAILABLE) {
                throw new BusinessException("Seat " + seat.getSeat().getRowLabel() +
                        seat.getSeat().getSeatNumber() + " is already booked.");
            }
        }

        BigDecimal subtotal = BigDecimal.ZERO;
        for (ShowSeat seat : selectedSeats) {
            subtotal = subtotal.add(seat.getPrice());
        }

        BigDecimal convenienceFee = new BigDecimal("2.00").multiply(new BigDecimal(selectedSeats.size()));
        BigDecimal totalAmount = subtotal.add(convenienceFee);

        selectedSeats.forEach(seat -> seat.setStatus(ShowSeatStatus.BOOKED));
        showSeatRepository.saveAll(selectedSeats);

        show.setAvailableSeats(show.getAvailableSeats() - selectedSeats.size());
        showRepository.save(show);

        Booking booking = new Booking();
        booking.setBookingReference(generateBookingReference());
        booking.setUser(user);
        booking.setShow(show);
        booking.setSubtotal(subtotal);
        booking.setConvenienceFee(convenienceFee);
        booking.setTotalAmount(totalAmount);
        booking.setStatus(BookingStatus.CONFIRMED);

        Booking savedBooking = bookingRepository.save(booking);

        List<BookingSeat> bookingSeats = new ArrayList<>();
        List<String> seatLabels = new ArrayList<>();

        for (ShowSeat showSeat : selectedSeats) {
            BookingSeat bookingSeat = new BookingSeat();
            bookingSeat.setBooking(savedBooking);
            bookingSeat.setShowSeat(showSeat);
            bookingSeat.setSeatPrice(showSeat.getPrice());
            bookingSeats.add(bookingSeat);

            seatLabels.add(showSeat.getSeat().getRowLabel() + showSeat.getSeat().getSeatNumber());
        }
        bookingSeatRepository.saveAll(bookingSeats);

        BookingResponse response = new BookingResponse();
        response.setBookingReference(savedBooking.getBookingReference());
        response.setMovieTitle(show.getMovie().getTitle());
        response.setTheatreName(show.getHall().getTheatre().getName());
        response.setHallName(show.getHall().getName());
        response.setShowTime(show.getStartTime().toString());
        response.setSeatLabels(seatLabels);
        response.setTotalAmount(savedBooking.getTotalAmount());
        response.setStatus(savedBooking.getStatus());

        return response;
    }

    @Override
    public List<Booking> getUserBookingHistory() {
        Long userId = SecurityConfig.getCurrentUserId();
        return bookingRepository.findByUserId(userId);
    }

    private String generateBookingReference() {
        return "BK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
