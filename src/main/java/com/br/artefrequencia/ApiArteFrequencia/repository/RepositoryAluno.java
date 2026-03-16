package com.br.artefrequencia.ApiArteFrequencia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.br.artefrequencia.ApiArteFrequencia.model.Aluno;

public interface RepositoryAluno extends JpaRepository <Aluno,Long> {

     Aluno findByMatricula(int matricula);

    boolean existsByCpf(String cpf);

    @Query("SELECT COALESCE(MAX(a.matricula), 0) + 1 FROM Aluno a")
    Integer gerarNovaMatricula();
    
}
