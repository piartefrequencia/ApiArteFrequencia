package com.br.artefrequencia.ApiArteFrequencia.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.artefrequencia.ApiArteFrequencia.model.Colaborador;

public interface RepositoryColaborador extends JpaRepository <Colaborador,Long>{

    boolean existsByEmail(String email);

    boolean existsByCpf(String cpf);

    Colaborador findByMatricula(int matricula);
    
}
