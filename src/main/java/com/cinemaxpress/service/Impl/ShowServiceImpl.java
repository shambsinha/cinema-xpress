package com.cinemaxpress.service.Impl;

import com.cinemaxpress.dto.ShowCreateRequest;
import com.cinemaxpress.entity.Hall;
import com.cinemaxpress.entity.Movie;
import com.cinemaxpress.entity.Seat;
import com.cinemaxpress.entity.Show;
import com.cinemaxpress.entity.ShowSeat;
import com.cinemaxpress.enums.ShowSeatStatus;
import com.cinemaxpress.exception.BusinessException;
import com.cinemaxpress.exception.ResourceNotFoundException;
import com.cinemaxpress.repository.HallRepository;
import com.cinemaxpress.repository.MovieRepository;
import com.cinemaxpress.repository.SeatRepository;
import com.cinemaxpress.repository.ShowRepository;
import com.cinemaxpress.repository.ShowSeatRepository;
import com.cinemaxpress.service.ShowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShowServiceImpl implements ShowService {

    private final ShowRepository showRepository;
    private final MovieRepository movieRepository;
    private final HallRepository hallRepository;
    private final SeatRepository seatRepository;
    private final ShowSeatRepository showSeatRepository;

    @Override
    @Transactional
    public Show createShow(ShowCreateRequest request) {

        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));

        Hall hall = hallRepository.findById(request.getHallId())
                .orElseThrow(() -> new ResourceNotFoundException("Hall not found"));

        if (request.getEndTime().isBefore(request.getStartTime()) || request.getEndTime().equals(request.getStartTime())) {
            throw new BusinessException("End time must be after start time");
        }

        if (showRepository.existsOverlappingShow(hall.getId(), request.getStartTime(), request.getEndTime())) {
            throw new BusinessException("A show is already scheduled in this hall during this time block!");
        }

        Show show = new Show();
        show.setMovie(movie);
        show.setHall(hall);
        show.setLanguage(request.getLanguage());
        show.setStartTime(request.getStartTime());
        show.setEndTime(request.getEndTime());
        show.setBasePrice(request.getBasePrice());

        // At the beginning, all seats in the hall are available
        show.setAvailableSeats(hall.getCapacity());
        show.setStatus("SCHEDULED");

        Show savedShow = showRepository.save(show);

        List<Seat> physicalSeats = seatRepository.findByHallId(hall.getId());

        if (physicalSeats.isEmpty()) {
            throw new BusinessException("No physical seats found for this hall. Please setup hall seats first.");
        }

        List<ShowSeat> showSeats = new ArrayList<>();

        for (Seat seat : physicalSeats) {
            ShowSeat showSeat = new ShowSeat();
            showSeat.setShow(savedShow);
            showSeat.setSeat(seat);
            showSeat.setPrice(request.getBasePrice());
            showSeat.setStatus(ShowSeatStatus.AVAILABLE);

            showSeats.add(showSeat);
        }

        showSeatRepository.saveAll(showSeats);

        return savedShow;
    }
}
