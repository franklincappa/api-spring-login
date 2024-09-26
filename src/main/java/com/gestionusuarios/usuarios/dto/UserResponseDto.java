package com.gestionusuarios.usuarios.dto;

import lombok.Data;
import java.util.Date;
import java.util.UUID;

@Data
public class UserResponseDto {
    private UUID id;
    private Date created;
    private Date modified;
    private Date lastLogin;
    private String token;
    private boolean isActive;

}
