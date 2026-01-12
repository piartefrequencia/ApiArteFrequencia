package com.br.artefrequencia.ApiArteFrequencia.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.artefrequencia.ApiArteFrequencia.model.Usuario;

import java.util.Optional;

public interface RepositoryUsuario extends JpaRepository<Usuario, Long> {

      boolean existsByEmail(String email);
    Optional<Usuario> findByEmail(String email);
    
}
