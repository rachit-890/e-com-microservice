package com.ecommerce.users.service.impl;

import com.ecommerce.users.dto.LoginRequestDto;
import com.ecommerce.users.dto.LoginResponseDto;
import com.ecommerce.users.dto.UserCreateRequestDto;
import com.ecommerce.users.dto.UserResponseDto;
import com.ecommerce.users.entity.Users;
import com.ecommerce.users.exception.ResourceAlreadyExitsException;
import com.ecommerce.users.exception.ResourceNotFoundException;
import com.ecommerce.users.mapper.AuthMapper;
import com.ecommerce.users.mapper.UserMapper;
import com.ecommerce.users.repository.UserRepository;
import com.ecommerce.users.service.UserService;
import com.ecommerce.users.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthMapper authMapper;

    @Override
    public UserResponseDto register(UserCreateRequestDto dto) {
       if(userRepository.existsByEmail(dto.getEmail())){
           throw new ResourceAlreadyExitsException("Email already registered");
       }
       Users user=userMapper.toEntity(dto);
       Users saved=userRepository.save(user);
       return userMapper.toDto(saved);
    }

    @Override
    public LoginResponseDto login(LoginRequestDto dto) {
        Users user=userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid credentials"));
       boolean matches= passwordEncoder.matches(dto.getPassword(),user.getPassword());
       if(!matches) {
           throw new ResourceNotFoundException("Invalid credentials");
       }
        String token=jwtUtils.generateToken(user.getId(), user.getEmail(), user.getRoles()) ;
        return authMapper.toLOginResponse(user,token) ;
    }

    @Override
    public UserResponseDto getUserById(Long id) {
       Users user=userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: "+id));
        return userMapper.toDto(user);
    }
}
