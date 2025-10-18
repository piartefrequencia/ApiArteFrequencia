package com.br.artefrequencia.ApiArteFrequencia.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.artefrequencia.ApiArteFrequencia.model.Administrador;

public interface RepositoryAdministardor extends JpaRepository <Administrador,Long> {

    Administrador findByMatricula(int matricula);

    boolean existsByEmail(String email);
    
}

