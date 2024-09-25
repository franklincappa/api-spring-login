package com.gestionusuarios.usuarios.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
//import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Entity
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Phone> phones;

    private Date created;
    private Date modified;
    private Date lastLogin;

    private String token;
    private boolean isActive;

    // Getters y Setters
}
