package com.br.artefrequencia.ApiArteFrequencia.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.br.artefrequencia.ApiArteFrequencia.model.Colaborador;

public interface RepositoryColaborador extends JpaRepository <Colaborador,Long>{

    Optional<Colaborador> findByMatricula(Integer matricula);

    boolean existsByEmail(String email);

    boolean existsByCpf(String cpf);

    @Query("SELECT COALESCE(MAX(c.matricula), 0) + 1 FROM Colaborador c")
    Integer gerarNovaMatricula();
    
}
