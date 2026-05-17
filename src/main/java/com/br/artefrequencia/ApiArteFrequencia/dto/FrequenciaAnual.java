package com.br.artefrequencia.ApiArteFrequencia.dto;

import java.util.List;
import lombok.Data;

@Data
public class FrequenciaAnual {
 
    private AlunoResposta aluno;
    private Integer ano;
    private List<FrequenciaMes> meses;
    
}