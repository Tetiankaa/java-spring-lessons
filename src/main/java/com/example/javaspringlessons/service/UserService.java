package com.example.javaspringlessons.service;

import com.example.javaspringlessons.dto.UserDTO;
import com.example.javaspringlessons.entity.User;
import com.example.javaspringlessons.mapper.UserMapper;
import com.example.javaspringlessons.repository.UserDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper mapper;
    private final UserDAO userDAO;


    public String save(UserDTO userDTO){
        User user = mapper.convertToUser(userDTO);
         userDAO.save(user);

         return "Successfully registered!";
    }
}
