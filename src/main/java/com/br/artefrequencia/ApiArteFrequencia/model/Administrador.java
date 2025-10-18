package com.br.artefrequencia.ApiArteFrequencia.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Administracao")
@lombok.Data
public class Administrador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "matricula")
    public Long matricula;

    @NotBlank(message = "Digite seu nome")
    @Column(name = "nome", columnDefinition = "VARCHAR(150)", nullable = false)
    private String nome;

    @Column(name = "usuario", columnDefinition = "VARCHAR(60)", nullable = false)
    private String usuario;

    @Email(message = "Digite um email valido")
    @NotBlank(message = "Email e obrigatório")
    @Column(name = "email", columnDefinition = "VARCHAR(100)", unique = true)
    private String email;

    @Size(min = 8, message = "Digite no minimo de 8 caracteres")
    @NotBlank(message = "Digite sua senha")
    @Column(name = "senha")
    private String senha;

}
