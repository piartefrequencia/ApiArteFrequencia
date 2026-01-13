package com.br.artefrequencia.ApiArteFrequencia.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.br.artefrequencia.ApiArteFrequencia.model.Usuario;
import com.br.artefrequencia.ApiArteFrequencia.repository.RepositoryUsuario;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private RepositoryUsuario repository;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        Usuario usuario = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email não encontrado"));

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + usuario.getPerfil().name());

        return new User(usuario.getEmail(), usuario.getSenha(), Collections.singleton(authority));
    }

}
