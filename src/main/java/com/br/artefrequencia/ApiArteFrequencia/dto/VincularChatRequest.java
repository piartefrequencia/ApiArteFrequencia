package com.br.artefrequencia.ApiArteFrequencia.dto;

import lombok.Data;

@Data
public class VincularChatRequest {
    
    private Long alunoId;
    private String chatId;
    private String tipo; 
    private String username; 

}