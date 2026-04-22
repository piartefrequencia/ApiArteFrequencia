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
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Entity
@Table(name = "responsavel_chat", 
       uniqueConstraints = @UniqueConstraint
       (name = "uq_responsavel_chat", 
       columnNames = {"aluno_id", "tipo"}))
@Data
public class ResponsavelChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "aluno_id", nullable = false)
    private Long alunoId;

    @Column(name = "tipo", columnDefinition = "VARCHAR(50)", nullable = false)
    private String tipo;

    @Column(name = "chat_id", columnDefinition = "VARCHAR(220)", nullable = false)
    private String chatId;

    @Column(name = "username", columnDefinition = "VARCHAR(220)")
    private String username;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;

    @CreationTimestamp
    @Column(name = "data_criacao", updatable = false)
    private LocalDateTime dataCriacao;

    @UpdateTimestamp
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

}
