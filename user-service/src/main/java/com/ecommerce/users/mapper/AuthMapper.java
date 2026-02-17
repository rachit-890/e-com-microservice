package com.ecommerce.users.mapper;

import com.ecommerce.users.dto.LoginResponseDto;
import com.ecommerce.users.entity.Users;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper {

    public LoginResponseDto toLOginResponse(Users user,String token){
        return new LoginResponseDto(
                token,"Bearer",user.getId(), user.getEmail(), user.getName()
        );
    }
}
