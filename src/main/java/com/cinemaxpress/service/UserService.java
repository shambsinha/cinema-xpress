package com.cinemaxpress.service;

import com.cinemaxpress.dto.AdminUserCreateRequest;
import com.cinemaxpress.dto.AdminUserUpdateRequest;
import com.cinemaxpress.dto.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    Page<UserResponse> getAllUsers(Pageable pageable);
    UserResponse getUserById(Long id);
    UserResponse createUser(AdminUserCreateRequest request);
    UserResponse updateUser(Long id, AdminUserUpdateRequest request);
    void deleteUser(Long id);
}
