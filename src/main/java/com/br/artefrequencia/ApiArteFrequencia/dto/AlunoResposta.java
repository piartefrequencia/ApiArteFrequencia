package com.br.artefrequencia.ApiArteFrequencia.dto;

import lombok.Data;

@Data
public class AlunoResposta {
    
    private Long id;
    private Integer matricula;
    private String nome;
    private String oficinas;

}