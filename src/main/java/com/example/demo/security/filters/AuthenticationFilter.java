package com.example.demo.security.filters;

import static com.example.demo.security.constants.SecurityConstants.EXPIRATION_TIME;
import static com.example.demo.security.constants.SecurityConstants.SECRET;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.usuario.dto.UserDetailDTO;
import com.example.demo.usuario.dto.UsuarioDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    
    private AuthenticationManager authenticationManager;
    private ObjectMapper objectMapper;

    public AuthenticationFilter(AuthenticationManager authenticationManager, ObjectMapper objectMapper) {
        this.authenticationManager = authenticationManager;
        this.objectMapper = objectMapper;
    }

    /**
     * Autentica o usuário
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) {
        try {
            UsuarioDTO usuario = this.objectMapper.readValue(req.getInputStream(), UsuarioDTO.class);
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(usuario.getUsername(), usuario.getPassword(), Arrays.asList());
            return this.authenticationManager.authenticate(token);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Cria o token caso usuário for autenticado
     */
    protected void successfulAuthentication(HttpServletRequest req, 
                                            HttpServletResponse res, 
                                            FilterChain filter, 
                                            Authentication auth) throws IOException {

        UserDetailDTO usuario = (UserDetailDTO) auth.getPrincipal();
        Date expirationDate = new Date(System.currentTimeMillis() + EXPIRATION_TIME);
        Algorithm algoritmo = Algorithm.HMAC512(SECRET.getBytes());
        
        String token = JWT.create()
                .withSubject(usuario.getUsername())
                .withExpiresAt(expirationDate)
                .sign(algoritmo);
        
        String body = usuario.getUsername().concat(" ").concat(token);

        res.getWriter().write(body);
        res.getWriter().flush();
    }

}
