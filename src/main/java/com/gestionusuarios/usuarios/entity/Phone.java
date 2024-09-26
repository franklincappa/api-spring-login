package com.gestionusuarios.usuarios.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
public class Phone {

    @Id
    @GeneratedValue
    private UUID id;

    private String number;
    private String citycode;
    private String contrycode;

}
