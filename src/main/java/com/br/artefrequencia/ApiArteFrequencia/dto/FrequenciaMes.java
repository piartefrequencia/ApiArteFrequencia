package com.br.artefrequencia.ApiArteFrequencia.dto;

import java.util.List;
import lombok.Data;

@Data
public class FrequenciaMes {

    private Integer mes;
    private List<Integer> dias;

}
