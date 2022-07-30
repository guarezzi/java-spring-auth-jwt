package com.example.demo.usuario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioViewDTO {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
}
