package com.cinemaxpress.service;

import com.cinemaxpress.dto.ShowCreateRequest;
import com.cinemaxpress.entity.Show;

public interface ShowService {
    Show createShow(ShowCreateRequest request);
}