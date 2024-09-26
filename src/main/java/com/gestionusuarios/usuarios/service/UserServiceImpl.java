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
import java.util.List;

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

        // Seteo campos de usuario
        user.setId(UUID.randomUUID());
        user.setCreated(new Date());
        user.setModified(new Date());
        user.setLastLogin(new Date());
        user.setToken(UUID.randomUUID().toString());
        user.setActive(true);

        User savedUser = userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public ResponseEntity<?> getUserById(UUID id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorMessage("Usuario no encontrado"));
        }
    }

    @Override
    public ResponseEntity<?> updateUser(UUID id, User userDetails) {
        Optional<User> existingUser = userRepository.findById(id);

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setName(userDetails.getName());
            user.setEmail(userDetails.getEmail());
            user.setPassword(userDetails.getPassword());
            user.setPhones(userDetails.getPhones());
            user.setModified(new java.util.Date());

            userRepository.save(user);
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorMessage("Usuario no encontrado para actualizar"));
        }
    }

    @Override
    public ResponseEntity<?> deleteUser(UUID id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.delete(user.get());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorMessage("Usuario no encontrado para eliminar"));
        }
    }

    @Override
    public ResponseEntity<?> login(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            if (user.get().getPassword().equals(password)) {
                // Actualiza el último login
                user.get().setLastLogin(new java.util.Date());
                userRepository.save(user.get());
                return ResponseEntity.ok(user.get());
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ErrorMessage("Contraseña incorrecta"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorMessage("Usuario no encontrado"));
        }
    }

}
