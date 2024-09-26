package com.gestionusuarios.usuarios.config;

import com.gestionusuarios.usuarios.entity.Phone;
import com.gestionusuarios.usuarios.entity.User;
import com.gestionusuarios.usuarios.repository.UserRepository;
import com.gestionusuarios.usuarios.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;
import java.util.UUID;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public DataInitializer(UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void run(String... args) throws Exception {
        // Verificar si el usuario ya existe para no crear duplicados
        if (userRepository.findByEmail("franklincappa@gmail.com").isEmpty()) {

            // Crear el objeto Phone
            Phone phone = new Phone();
            phone.setNumber("12345678");
            phone.setCitycode("1");
            phone.setContrycode("57");

            // Crear el objeto User
            User user = new User();
            user.setId(UUID.randomUUID());
            user.setName("Franklin Cappa");
            user.setEmail("franklincappa@gmail.com");
            //user.setPassword("fcappa2024");
            user.setPhones(Collections.singletonList(phone));
            user.setCreated(new Date());
            user.setModified(new Date());
            user.setLastLogin(new Date());
            //user.setToken(UUID.randomUUID().toString());
            user.setActive(true);

            //encriptamos la contraseña
            String encodedPassword = passwordEncoder.encode("fcappa2024");
            user.setPassword(encodedPassword);

            String jwtToken = jwtTokenProvider.generateToken(user.getEmail());
            user.setToken(jwtToken);

            userRepository.save(user);

            System.out.println("Usuario por defecto creado: Franklin Cappa");
        } else {
            System.out.println("El usuario Franklin Cappa ya existe en la base de datos.");
        }
    }
}

