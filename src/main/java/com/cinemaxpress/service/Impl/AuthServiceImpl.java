package com.cinemaxpress.service.impl;

import com.cinemaxpress.dto.AuthResponse;
import com.cinemaxpress.dto.LoginRequest;
import com.cinemaxpress.dto.RegisterRequest;
import com.cinemaxpress.entity.User;
import com.cinemaxpress.enums.Role;
import com.cinemaxpress.enums.UserStatus;
import com.cinemaxpress.exception.BusinessException;
import com.cinemaxpress.repository.UserRepository;
import com.cinemaxpress.security.JwtService;
import com.cinemaxpress.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BusinessException("Email already registered!");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        user.setStatus(UserStatus.ACTIVE);

        User savedUser = userRepository.save(user);

        String token = jwtService.generateToken(savedUser.getId(), savedUser.getEmail(), savedUser.getRole().name());
        return new AuthResponse(token, savedUser.getName(), savedUser.getEmail(), savedUser.getRole().name());
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException("Invalid email or password!"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException("Invalid email or password!");
        }

        String token = jwtService.generateToken(user.getId(), user.getEmail(), user.getRole().name());
        return new AuthResponse(token, user.getName(), user.getEmail(), user.getRole().name());
    }
}