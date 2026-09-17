package com.cinemaxpress.service;

import com.cinemaxpress.dto.HallCreateRequest;
import com.cinemaxpress.entity.Hall;

public interface HallService {
    Hall createHallWithSeats(HallCreateRequest request);
}