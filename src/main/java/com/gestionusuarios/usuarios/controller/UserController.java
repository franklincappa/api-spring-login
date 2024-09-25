package com.gestionusuarios.usuarios.controller;

import com.gestionusuarios.usuarios.entity.User;
import com.gestionusuarios.usuarios.exception.ErrorMessage;
import com.gestionusuarios.usuarios.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorMessage("El correo ya registrado"));
        }

        // Validar formato de correo y contraseña
        if (!user.getEmail().matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorMessage("Formato de correo incorrecto"));
        }
        if (!user.getPassword().matches("^(?=.*[0-9])(?=.*[a-zA-Z]).{6,}$")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorMessage("Formato de contraseña incorrecto"));
        }

        // Generar token (opcionalmente JWT)
        String token = UUID.randomUUID().toString(); // Para JWT, reemplazar por el código de generación

        user.setId(UUID.randomUUID());
        user.setCreated(new Date());
        user.setModified(new Date());
        user.setLastLogin(new Date());
        user.setToken(token);
        user.setActive(true);

        User savedUser = userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }
}

