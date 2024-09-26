package com.gestionusuarios.usuarios.service;

import com.gestionusuarios.usuarios.dto.UserRequestDto;
import com.gestionusuarios.usuarios.entity.User;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface UserService {
    ResponseEntity<?> registerUser(UserRequestDto userDto);
    List<User> getAllUsers();
    ResponseEntity<?> getUserById(UUID id);
    ResponseEntity<?> updateUser(UUID id, UserRequestDto userDto);
    ResponseEntity<?> deleteUser(UUID id);
    ResponseEntity<?> login(String email, String password);
}

