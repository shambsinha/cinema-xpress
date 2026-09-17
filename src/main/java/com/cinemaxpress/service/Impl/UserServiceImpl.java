package com.cinemaxpress.service.Impl;

import com.cinemaxpress.dto.AdminUserCreateRequest;
import com.cinemaxpress.dto.AdminUserUpdateRequest;
import com.cinemaxpress.dto.UserResponse;
import com.cinemaxpress.entity.User;
import com.cinemaxpress.enums.Role;
import com.cinemaxpress.enums.UserStatus;
import com.cinemaxpress.exception.BusinessException;
import com.cinemaxpress.exception.ResourceNotFoundException;
import com.cinemaxpress.repository.UserRepository;
import com.cinemaxpress.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(this::toResponse);
    }

    @Override
    public UserResponse getUserById(Long id) {
        return toResponse(findUserOrThrow(id));
    }

    @Override
    public UserResponse createUser(AdminUserCreateRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BusinessException("Email already registered!");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole() != null ? Role.valueOf(request.getRole()) : Role.USER);
        user.setStatus(request.getStatus() != null ? UserStatus.valueOf(request.getStatus()) : UserStatus.ACTIVE);

        return toResponse(userRepository.save(user));
    }

    @Override
    public UserResponse updateUser(Long id, AdminUserUpdateRequest request) {
        User user = findUserOrThrow(id);
        user.setName(request.getName());
        user.setPhone(request.getPhone());
        if (request.getRole() != null) {
            user.setRole(Role.valueOf(request.getRole()));
        }
        if (request.getStatus() != null) {
            user.setStatus(UserStatus.valueOf(request.getStatus()));
        }
        return toResponse(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    private User findUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    private UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole().name())
                .status(user.getStatus().name())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
