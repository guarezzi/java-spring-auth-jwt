package com.example.demo.security.filters;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.util.Strings;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.security.constants.SecurityConstants;
import com.example.demo.usuario.dto.UsuarioDTO;
import com.example.demo.usuario.services.UsuarioService;

public class AuthorizationFilter extends BasicAuthenticationFilter {

    // TODO: https://www.toptal.com/spring/spring-security-tutorial
    UsuarioService usuarioService;

    public AuthorizationFilter(AuthenticationManager authenticationManager, UsuarioService usuarioService) {
        super(authenticationManager);
        this.usuarioService = usuarioService;
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filter) throws IOException, ServletException {
        String header = req.getHeader(SecurityConstants.HEADER_STRING);

        if (!Strings.isBlank(header) && header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            UsernamePasswordAuthenticationToken token = this.getAuthentication(req);
            SecurityContextHolder.getContext().setAuthentication(token);
        }

        filter.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest req) {
        String token = req.getHeader(SecurityConstants.HEADER_STRING);

        if (Strings.isBlank(token)) {
            return null;
        }

        String user = JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()))
            .build()
            .verify(token.replace(SecurityConstants.TOKEN_PREFIX, ""))
            .getSubject();

        if (Strings.isBlank(user)) {
            return null;
        }

        UsuarioDTO usuario = this.usuarioService.getByUsername(user);
        return new UsernamePasswordAuthenticationToken(user, usuario, Arrays.asList());
    }
}
