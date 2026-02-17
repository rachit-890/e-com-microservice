package com.ecommerce.users.dto;

import lombok.*;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDto {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String roles;
    private String phone;
    private Instant createdAt;
    private Instant updatedAt;
}
