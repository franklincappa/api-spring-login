package com.gestionusuarios.usuarios.service;

import com.gestionusuarios.usuarios.dto.UserResponseDto;
import com.gestionusuarios.usuarios.entity.User;
import com.gestionusuarios.usuarios.repository.UserRepository;
import com.gestionusuarios.usuarios.security.JwtTokenProvider;
import com.gestionusuarios.usuarios.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceImplLoginTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private UserServiceImpl userService;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private User defaultUser;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        // Configuración de usuario por default
        defaultUser = new User();
        defaultUser.setId(UUID.randomUUID());
        defaultUser.setName("Franklin Cappa");
        defaultUser.setEmail("franklincappa@gmail.com");
        defaultUser.setPassword(passwordEncoder.encode("fcappa2024"));
        defaultUser.setActive(true);

        when(userRepository.findByEmail("franklincappa@gmail.com")).thenReturn(Optional.of(defaultUser));
    }

    @Test
    public void testLogin_successful() {
        ResponseEntity<?> response = userService.login("franklincappa@gmail.com", "fcappa2024");
        assertTrue(passwordEncoder.matches("fcappa2024", defaultUser.getPassword()),
                "La contraseña no coincide con la contraseña encriptada");

        assertTrue(response.getStatusCode().is2xxSuccessful());

        User responseBody = (User) response.getBody();
        assertNotNull(responseBody, "El cuerpo de la respuesta no debe ser nulo");

        //validación de test
        assertEquals(defaultUser.getEmail(), responseBody.getEmail(), "El email no coincide");
        assertEquals(defaultUser.getName(), responseBody.getName(), "El nombre no coincide");
        assertTrue(passwordEncoder.matches("fcappa2024", defaultUser.getPassword()), "La contraseña encriptada no coincide");
        assertTrue(responseBody.isActive(), "El usuario no está activo");
    }

}

