package com.br.artefrequencia.ApiArteFrequencia.model.Db1;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "presenca")
@Data
public class Presenca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "aluno_id", nullable = false)
    private Long alunoId;

    @Column(name = "tipo", columnDefinition = "VARCHAR(70)", nullable = false)
    private String tipo; 

    @Column(name = "origem", columnDefinition = "VARCHAR(220)", nullable = true)
    private String origem; 
    
    @Column(name = "lido_em", nullable = false)
    private LocalDateTime lidoEm;

    @CreationTimestamp  
    @Column(name = "criado_em", updatable = false, nullable = false)
    private LocalDateTime criadoEm;

    @UpdateTimestamp
    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;
    
}


