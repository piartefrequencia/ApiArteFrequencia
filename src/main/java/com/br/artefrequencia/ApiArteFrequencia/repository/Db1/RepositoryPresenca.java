package com.br.artefrequencia.ApiArteFrequencia.repository.Db1;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.artefrequencia.ApiArteFrequencia.model.Db1.Presenca;

import jakarta.transaction.Transactional;

public interface RepositoryPresenca extends JpaRepository<Presenca, Long>  {
    
    List<Presenca> findByAlunoIdAndTipoAndLidoEmBetween(
        Long alunoId, String tipo, LocalDateTime inicio, LocalDateTime fim
    );

    @Transactional
    void deleteByAlunoIdAndLidoEmBetween(
        Long alunoId, LocalDateTime inicio, LocalDateTime fim
    );

}
