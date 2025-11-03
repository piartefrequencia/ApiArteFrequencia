package com.br.artefrequencia.ApiArteFrequencia.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Table(name = "partituras")
@Data
public class Partitura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
   
    private String tipo;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] conteudo;
    
}
