package com.gestionusuarios.usuarios.config;

import com.gestionusuarios.usuarios.entity.Phone;
import com.gestionusuarios.usuarios.entity.User;
import com.gestionusuarios.usuarios.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;
import java.util.UUID;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    public DataInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
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
            user.setPassword("fcappa2024");
            user.setPhones(Collections.singletonList(phone));
            user.setCreated(new Date());
            user.setModified(new Date());
            user.setLastLogin(new Date());
            user.setToken(UUID.randomUUID().toString());
            user.setActive(true);

            userRepository.save(user);

            System.out.println("Usuario por defecto creado: Franklin Cappa");
        } else {
            System.out.println("El usuario Franklin Cappa ya existe en la base de datos.");
        }
    }
}

