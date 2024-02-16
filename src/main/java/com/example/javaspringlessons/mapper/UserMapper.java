package com.example.javaspringlessons.mapper;

import com.example.javaspringlessons.dto.UserDTO;
import com.example.javaspringlessons.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO convertToDto(User user){
        return UserDTO.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
    }

    public User convertToUser(UserDTO dto){

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());

        return user;
    }


}
