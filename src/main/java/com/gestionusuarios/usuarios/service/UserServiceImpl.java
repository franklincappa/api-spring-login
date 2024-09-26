package com.gestionusuarios.usuarios.service;

import com.gestionusuarios.usuarios.dto.UserRequestDto;
import com.gestionusuarios.usuarios.dto.UserResponseDto;
import com.gestionusuarios.usuarios.entity.Phone;
import com.gestionusuarios.usuarios.entity.User;
import com.gestionusuarios.usuarios.repository.UserRepository;
import com.gestionusuarios.usuarios.exception.ErrorMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Value("${user.password.regex}")
    private String passwordRegex;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<?> registerUser(UserRequestDto userDto) {
        // Verificar si el correo ya está registrado
        Optional<User> existingUser = userRepository.findByEmail(userDto.getEmail());

        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorMessage("El correo ya registrado"));
        }

        // Validar formato de correo
        if (!userDto.getEmail().matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorMessage("Formato de correo incorrecto"));
        }

        // Validar formato de contraseña
        if (!userDto.getPassword().matches(passwordRegex)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorMessage("Formato de contraseña incorrecto"));
        }

        // Convertir UserRequestDto a User
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setCreated(new Date());
        user.setModified(new Date());
        user.setLastLogin(new Date());
        user.setToken(UUID.randomUUID().toString());
        user.setActive(true);

        List<Phone> phones = new ArrayList<>();
        for (var phoneDto : userDto.getPhones()) {
            Phone phone = new Phone();
            phone.setNumber(phoneDto.getNumber());
            phone.setCitycode(phoneDto.getCitycode());
            phone.setContrycode(phoneDto.getContrycode());
            phones.add(phone);
        }
        user.setPhones(phones);

        User savedUser = userRepository.save(user);

        //Data Response de usuario
        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setId(savedUser.getId());
        responseDto.setCreated(savedUser.getCreated());
        responseDto.setModified(savedUser.getModified());
        responseDto.setLastLogin(savedUser.getLastLogin());
        responseDto.setToken(savedUser.getToken());
        responseDto.setActive(savedUser.isActive());

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
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
    public ResponseEntity<?> updateUser(UUID id, UserRequestDto userDto) {
        Optional<User> existingUser = userRepository.findById(id);

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setName(userDto.getName());
            user.setEmail(userDto.getEmail());
            user.setPassword(userDto.getPassword());
            user.setModified(new java.util.Date());

            List<Phone> phones = new ArrayList<>();
            for (var phoneDto : userDto.getPhones()) {
                Phone phone = new Phone();
                phone.setNumber(phoneDto.getNumber());
                phone.setCitycode(phoneDto.getCitycode());
                phone.setContrycode(phoneDto.getContrycode());
                phones.add(phone);
            }
            user.setPhones(phones);

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
