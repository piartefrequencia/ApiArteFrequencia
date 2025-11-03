package com.br.artefrequencia.ApiArteFrequencia.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.artefrequencia.ApiArteFrequencia.model.Partitura;

public interface RepositoryPartitura extends JpaRepository <Partitura, Long>{
    
}
