package com.cinemaxpress.service;

import com.cinemaxpress.dto.AuthResponse;
import com.cinemaxpress.dto.LoginRequest;
import com.cinemaxpress.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}