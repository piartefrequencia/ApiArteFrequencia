package com.br.artefrequencia.ApiArteFrequencia.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.br.artefrequencia.ApiArteFrequencia.enums.Perfil;
import com.fasterxml.jackson.annotation.JsonProperty;



@Entity
@Table(name = "usuarios")
@Data
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario", columnDefinition = "VARCHAR(60)", nullable = false)
    private String usuario;

    @Email(message = "Digite um email valido")
    @NotBlank(message = "Email e obrigatório")
    @Column(name = "email", columnDefinition = "VARCHAR(100)", unique = true)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) 
    @NotBlank(message = "Digite sua senha")
    @Column(name = "senha")
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(name = "perfil", columnDefinition = "VARCHAR(20)", nullable = false)
    private Perfil perfil;

    
    @CreationTimestamp  
    private LocalDateTime dataCriacao;

    @UpdateTimestamp
    private LocalDateTime dataAtualizacao;

    
}
