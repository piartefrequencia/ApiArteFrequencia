package com.br.artefrequencia.ApiArteFrequencia.repository.Db1;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.br.artefrequencia.ApiArteFrequencia.model.Db1.Aluno;

public interface RepositoryAluno extends JpaRepository <Aluno,Long> {

    Optional<Aluno> findByMatricula(Integer matricula);

    boolean existsByCpf(String cpf);

    @Query("SELECT COALESCE(MAX(a.matricula), 0) + 1 FROM Aluno a")
    Integer gerarNovaMatricula();
    
}
