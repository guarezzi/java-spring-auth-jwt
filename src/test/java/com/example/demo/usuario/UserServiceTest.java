package com.example.demo.usuario;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.usuario.model.Usuario;
import com.example.demo.usuario.repository.UsuarioRepository;
import com.example.demo.usuario.services.UsuarioService;

@SpringBootTest()
public class UserServiceTest {

    private static final long ID = 1L;
    private static final String USERNAME = "usuario_teste";
    private static final String USUARIO_TESTE_EMAIL = "usuario.teste@teste.com.br";

    @Autowired
    private UsuarioService usuarioService;
    
    @MockBean
    private UsuarioRepository usuarioRepository;

    @Test
    public void loadUserByUsername_withValidParam_shouldLoadUserDetail() {
        Usuario mockUsuario = new Usuario();
        mockUsuario.setId(ID);
        mockUsuario.setUsername(USERNAME);
        mockUsuario.setEmail(USUARIO_TESTE_EMAIL);

        Mockito.when(this.usuarioRepository.findByUsername(USERNAME)).thenReturn(mockUsuario);

        UserDetails result = this.usuarioService.loadUserByUsername(USERNAME);

        assertNotNull(result);
        assertEquals(USERNAME, result.getUsername());
    }

}
