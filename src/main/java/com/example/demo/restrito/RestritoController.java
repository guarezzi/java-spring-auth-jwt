package com.example.demo.restrito;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/restrito")
public class RestritoController {
    

    @PostMapping
    public ResponseEntity<String> get() {
        return ResponseEntity.ok("Conteúdo restrito acessado!");
    }

}
