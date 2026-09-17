package com.cinemaxpress.service;

import com.cinemaxpress.dto.HallCreateRequest;
import com.cinemaxpress.dto.HallResponse;

public interface HallService {
    HallResponse createHallWithSeats(HallCreateRequest request);
}
