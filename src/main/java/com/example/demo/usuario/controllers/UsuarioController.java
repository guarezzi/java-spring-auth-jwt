package com.example.demo.usuario.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.usuario.dto.UsuarioDTO;
import com.example.demo.usuario.dto.UsuarioViewDTO;
import com.example.demo.usuario.services.UsuarioService;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
    
    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/novo")
    public ResponseEntity<Boolean> salvar(@RequestBody UsuarioDTO usuario) {
        this.usuarioService.salvar(usuario);
        return ResponseEntity.ok(true);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioViewDTO>> listar() {
        List<UsuarioViewDTO> usuarios = this.usuarioService.listar();
        return ResponseEntity.ok(usuarios);
    }

}
