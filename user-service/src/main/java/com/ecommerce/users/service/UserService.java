package com.ecommerce.users.service;

import com.ecommerce.users.dto.LoginRequestDto;
import com.ecommerce.users.dto.LoginResponseDto;
import com.ecommerce.users.dto.UserCreateRequestDto;
import com.ecommerce.users.dto.UserResponseDto;

public interface UserService {
    UserResponseDto register(UserCreateRequestDto dto);
    LoginResponseDto login(LoginRequestDto dto);
    UserResponseDto getUserById(Long id);
}
