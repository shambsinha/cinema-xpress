package com.cinemaxpress.service.Impl;

import com.cinemaxpress.dto.HallCreateRequest;
import com.cinemaxpress.dto.HallResponse;
import com.cinemaxpress.entity.Hall;
import com.cinemaxpress.entity.Seat;
import com.cinemaxpress.entity.Theatre;
import com.cinemaxpress.exception.ResourceNotFoundException;
import com.cinemaxpress.repository.HallRepository;
import com.cinemaxpress.repository.SeatRepository;
import com.cinemaxpress.repository.TheatreRepository;
import com.cinemaxpress.service.HallService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HallServiceImpl implements HallService {

    private final TheatreRepository theatreRepository;
    private final HallRepository hallRepository;
    private final SeatRepository seatRepository;

    @Override
    @Transactional
    public HallResponse createHallWithSeats(HallCreateRequest request) {

        Theatre theatre = theatreRepository.findById(request.getTheatreId())
                .orElseThrow(() -> new ResourceNotFoundException("Theatre not found"));

        Hall hall = new Hall();
        hall.setTheatre(theatre);
        hall.setName(request.getName());
        hall.setCapacity(request.getCapacity());
        hall.setStatus("ACTIVE");

        Hall savedHall = hallRepository.save(hall);

        List<Seat> seats = generateSeatsForHall(savedHall, request.getCapacity());
        seatRepository.saveAll(seats);

        return HallResponse.builder()
                .id(savedHall.getId())
                .theatreId(theatre.getId())
                .theatreName(theatre.getName())
                .name(savedHall.getName())
                .capacity(savedHall.getCapacity())
                .status(savedHall.getStatus())
                .build();
    }

    private List<Seat> generateSeatsForHall(Hall hall, int totalCapacity) {
        List<Seat> seats = new ArrayList<>();

        int seatsPerRow = 20;
        int generatedCount = 0;
        int rowIndex = 0;

        while (generatedCount < totalCapacity) {
            String rowLabel = getRowLabel(rowIndex);

            for (int seatNum = 1; seatNum <= seatsPerRow; seatNum++) {
                if (generatedCount >= totalCapacity) {
                    break;
                }

                Seat seat = new Seat();
                seat.setHall(hall);
                seat.setRowLabel(rowLabel);
                seat.setSeatNumber(seatNum);
                seat.setSeatType("STANDARD");
                seat.setStatus("ACTIVE");

                seats.add(seat);
                generatedCount++;
            }
            rowIndex++;
        }

        return seats;
    }

    private String getRowLabel(int index) {
        StringBuilder label = new StringBuilder();
        int temp = index;
        while (temp >= 0) {
            label.insert(0, (char) ('A' + (temp % 26)));
            temp = (temp / 26) - 1;
        }
        return label.toString();
    }
}