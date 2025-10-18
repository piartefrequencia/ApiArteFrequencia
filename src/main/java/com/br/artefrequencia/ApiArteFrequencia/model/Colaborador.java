package com.br.artefrequencia.ApiArteFrequencia.model;

import java.time.LocalDate;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Table(name = "Colaborador")
@Data
public class Colaborador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "matricula")
    public Long matricula;

    @NotBlank(message = " Digite seu nome")
    @Column(name = "nome", columnDefinition = "VARCHAR(150)", nullable = false)
    private String nome;

    @NotBlank(message = "Digite o CPF")
    @CPF(message = "Digite um CPF valido")
    @Column(name = "cpf", columnDefinition = "VARCHAR(15)", unique = true)
    private String cpf;

    @NotBlank(message = "Digite seu RG")
    @Column(name = "rg", columnDefinition = "VARCHAR(15)", nullable = true)
    private String rg;

    @Column(name = "dataExpedRg", columnDefinition = "VARCHAR(15)", nullable = true)
    private String dataExpedRg;

    @Column(name = "dataNascimento", columnDefinition = "VARCHAR(15)", nullable = false)
    private String dataNascimento;

    @Column(name = "idade", columnDefinition = "VARCHAR(15)", nullable = false)
    private Integer idade;

    @Column(name = "areaInstrucao", columnDefinition = "VARCHAR(100)", nullable = true)
    private String areaInstrucao;

    @NotBlank(message = "Digite sua Formação Profissional")
    @Column(name = "formacao", columnDefinition = "VARCHAR(100)", nullable = false)
    private String formacao;

    @Column(name = "apelido", columnDefinition = "VARCHAR(50)", nullable = true)
    private String apelido;

    @Column(name = "redeSocial", columnDefinition = "VARCHAR(50)", nullable = true)
    private String redeSocial;

    @NotBlank(message = "Digite telefone")
    @Column(name = "telefone", columnDefinition = "VARCHAR(15)", nullable = false)
    private String telefone;

    @Email(message = "Digite um Email valido")
    @NotBlank(message = "Email e obrigatório")
    @Column(name = "email", columnDefinition = "VARCHAR(150)", unique = true)
    private String email;

    @Column(name = "senha")
    private String senha;

}
