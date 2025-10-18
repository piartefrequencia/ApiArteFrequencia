package com.br.artefrequencia.ApiArteFrequencia.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.artefrequencia.ApiArteFrequencia.model.Aluno;

public interface RepositoryAluno extends JpaRepository <Aluno,Long> {

     Aluno findByMatricula(int matricula);

    boolean existsByCpf(String cpf);

    
}
