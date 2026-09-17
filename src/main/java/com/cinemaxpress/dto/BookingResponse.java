package com.cinemaxpress.dto;

import com.cinemaxpress.enums.BookingStatus;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class BookingResponse {
    private Long id;
    private String bookingReference;
    private String movieTitle;
    private String theatreName;
    private String hallName;
    private String showTime;
    private List<String> seatLabels;
    private BigDecimal totalAmount;
    private BookingStatus status;
    private LocalDateTime bookedAt;
}