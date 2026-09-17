package com.cinemaxpress.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class ShowSeatResponse {
    private Long id;
    private Long showId;
    private Long seatId;
    private String rowLabel;
    private Integer seatNumber;
    private BigDecimal price;
    private String status;
}
