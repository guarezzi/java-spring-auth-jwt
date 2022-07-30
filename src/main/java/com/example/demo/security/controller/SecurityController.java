package com.example.demo.security.controller;

import java.security.Principal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("logout")
public class SecurityController {
    
    @GetMapping()
    public void logout(Principal principal) {
        System.out.println(String.format("Login encerrado com sucesso para o usuário: %s ", principal.getName()));
    }
    
}
