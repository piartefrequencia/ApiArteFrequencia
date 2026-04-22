package com.br.artefrequencia.ApiArteFrequencia.dto;

import lombok.Data;

@Data
public class RegistrarFrequenciaRequest {
    
    private Long alunoId;
    
    private String tipo; 

}