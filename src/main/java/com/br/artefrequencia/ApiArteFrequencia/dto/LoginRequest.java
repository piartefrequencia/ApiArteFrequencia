package com.br.artefrequencia.ApiArteFrequencia.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @Email(message = "Digite um Email Valido")
    @NotBlank(message = "Email e Obrigatório")
    private String email;

    @NotBlank(message = "Digite sua senha")
    private String senha;
    
}
