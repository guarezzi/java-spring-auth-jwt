package com.example.demo.usuario.services;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.usuario.dto.UserDetailDTO;
import com.example.demo.usuario.dto.UsuarioDTO;
import com.example.demo.usuario.dto.UsuarioViewDTO;
import com.example.demo.usuario.model.Usuario;
import com.example.demo.usuario.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class UsuarioService implements UserDetailsService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Transactional()
    public void salvar(UsuarioDTO usuarioDTO) {
        String senhaCriptografada = this.criptografarSenha(usuarioDTO.getPassword());
        usuarioDTO.setPassword(senhaCriptografada);
        Usuario usuario = this.objectMapper.convertValue(usuarioDTO, Usuario.class);
        this.usuarioRepository.save(usuario);
    }

    private String criptografarSenha(final String senha) {
        return this.bCryptPasswordEncoder.encode(senha);
    }

    public List<UsuarioViewDTO> listar() {
        List<Usuario> usuarios = this.usuarioRepository.findAll();
        return usuarios.stream().map(
            usuario -> objectMapper.convertValue(usuario, UsuarioViewDTO.class)
        ).collect(Collectors.toList());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = this.usuarioRepository.findByUsername(username);
        UserDetailDTO userDetail = this.objectMapper.convertValue(usuario, UserDetailDTO.class);
        userDetail.setEnabled(true);
        userDetail.setAccountNonLocked(true);
        userDetail.setAccountNonExpired(true);
        userDetail.setCredentialsNonExpired(true);
        return userDetail;
    }

    public UsuarioDTO getByUsername(String username) {
        Usuario usuario = this.usuarioRepository.findByUsername(username);
        return this.objectMapper.convertValue(usuario, UsuarioDTO.class);
    }

}
