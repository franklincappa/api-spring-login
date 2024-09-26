package com.gestionusuarios.usuarios.service;

import com.gestionusuarios.usuarios.entity.User;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface UserService {
    ResponseEntity<?> registerUser(User user);
    List<User> getAllUsers();
    ResponseEntity<?> getUserById(UUID id);
    ResponseEntity<?> updateUser(UUID id, User user);
    ResponseEntity<?> deleteUser(UUID id);
    ResponseEntity<?> login(String email, String password);
}

