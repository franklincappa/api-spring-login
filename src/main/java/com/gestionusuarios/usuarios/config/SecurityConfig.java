package com.gestionusuarios.usuarios.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Deshabilita CSRF (si no es necesario)
                .authorizeHttpRequests((authz) -> authz
                        .anyRequest().permitAll() // Permitir acceso a todas las rutas sin autenticación
                )
                .httpBasic().disable() // Deshabilita la autenticación básica
                .formLogin().disable(); // Deshabilita la autenticación con formularios

        return http.build();
    }
}

