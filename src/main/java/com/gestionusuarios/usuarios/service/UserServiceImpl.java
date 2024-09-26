package com.gestionusuarios.usuarios.service;

import com.gestionusuarios.usuarios.entity.User;
import com.gestionusuarios.usuarios.repository.UserRepository;
import com.gestionusuarios.usuarios.exception.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<?> registerUser(User user) {
        // Verificar si el correo ya está registrado
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorMessage("El correo ya registrado"));
        }

        // Validar formato de correo
        if (!user.getEmail().matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorMessage("Formato de correo incorrecto"));
        }

        // Validar formato de contraseña
        if (!user.getPassword().matches("^(?=.*[0-9])(?=.*[a-zA-Z]).{6,}$")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorMessage("Formato de contraseña incorrecto"));
        }

        // Crear usuario
        user.setId(UUID.randomUUID());
        user.setCreated(new Date());
        user.setModified(new Date());
        user.setLastLogin(new Date());
        user.setToken(UUID.randomUUID().toString()); // Para JWT, podrías generar el token JWT aquí
        user.setActive(true);

        // Guardar el usuario en la base de datos
        User savedUser = userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }
}

