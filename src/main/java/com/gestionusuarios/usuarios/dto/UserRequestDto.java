package com.gestionusuarios.usuarios.dto;

import lombok.Data;
import java.util.List;

@Data
public class UserRequestDto {
    private String name;
    private String email;
    private String password;
    private List<PhoneDto> phones;

}
