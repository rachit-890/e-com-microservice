package com.ecommerce.users.controller;

import com.ecommerce.users.dto.LoginRequestDto;
import com.ecommerce.users.dto.LoginResponseDto;
import com.ecommerce.users.dto.UserCreateRequestDto;
import com.ecommerce.users.dto.UserResponseDto;
import com.ecommerce.users.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@Valid @RequestBody UserCreateRequestDto dto){
        UserResponseDto createdUser=userService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto dto){
        LoginResponseDto token=userService.login(dto);
        return ResponseEntity.ok(token);
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id, Authentication authentication){
        UserResponseDto user=userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
}
