package com.gestionusuarios.usuarios.service;

import com.gestionusuarios.usuarios.entity.User;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<?> registerUser(User user);
}

