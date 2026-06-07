package com.br.artefrequencia.ApiArteFrequencia.dto;

import lombok.Data;

@Data
public class LoginResponse {

    private String token;
    private String refreshToken;
    private String perfil;
    private String usuario;
    private String email;

    public LoginResponse(String token, String refreshToken,
                         String perfil, String usuario, String email) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.perfil = perfil;
        this.usuario = usuario;
        this.email = email;
    }
}
    
