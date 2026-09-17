package com.cinemaxpress.service;

import com.cinemaxpress.dto.ShowCreateRequest;
import com.cinemaxpress.dto.ShowResponse;
import com.cinemaxpress.dto.ShowSeatResponse;

import java.util.List;

public interface ShowService {
    ShowResponse createShow(ShowCreateRequest request);
    List<ShowResponse> getShowsForMovie(Long movieId);
    List<ShowSeatResponse> getShowSeats(Long showId);
}
