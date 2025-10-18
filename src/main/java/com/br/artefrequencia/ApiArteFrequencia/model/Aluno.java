package com.br.artefrequencia.ApiArteFrequencia.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDate;

import org.hibernate.validator.constraints.br.CPF;


@Entity
@Table(name = "Alunos")
@Data
public class Aluno {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "matricula")
    public Long matricula;

   
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String foto;

    @Lob
    @Column(name = "oficinas", columnDefinition = "TEXT", nullable = true)
    private String oficinas;

    @NotBlank(message = " Digite seu nome")
    @Column(name = "nome", columnDefinition = "VARCHAR(150)", nullable = false)
    private String nome;
    
    
    //@CPF(message = "Digite um CPF valido")
    @Column(name = "cpf", columnDefinition = "VARCHAR(15)", nullable = true)
    private String cpf;
    
    @Column(name = "rg", columnDefinition = "VARCHAR(15)", nullable = true)
    private String rg;

    @Column(name = "orgaoExp", columnDefinition = "VARCHAR(15)", nullable = true)
    private String orgaoExp;

    @Column(name = "dataExpedRg", columnDefinition = "VARCHAR(15)", nullable = true)
    private String dataExpedRg; 
   
    @Column(name = "rn", columnDefinition = "VARCHAR(15)", nullable = true)
    private String rn;
    
    @Column(name = "dataNascimento", columnDefinition = "VARCHAR(15)", nullable = false)
    private String dataNascimento;
    
    private Integer idade;

    @Column(name = "escola", columnDefinition = "VARCHAR(150)", nullable = true)
    private String escola;
    
    @Column(name = "estado", columnDefinition = "VARCHAR(100)", nullable = true)
    private String estado;
    
    @Column(name = "cidade", columnDefinition = "VARCHAR(100)", nullable = true)
    private String cidade;
    
    @Column(name = "bairro", columnDefinition = "VARCHAR(100)", nullable = true)
    private String bairro;

    @Column(name = "filiacaoPai", columnDefinition = "VARCHAR(150)", nullable = true)
    private String filiacaoPai;
    
    @Column(name = "filiacaoMae", columnDefinition = "VARCHAR(150)", nullable = true)
    private String filiacaoMae;
    
    @Column(name = "telefonePai", columnDefinition = "VARCHAR(15)", nullable = true)
    private String telefonePai;
    
    @Column(name = "telefoneMae", columnDefinition = "VARCHAR(15)", nullable = true)
    private String telefoneMae;

    @Column(name = "responsavel", columnDefinition = "VARCHAR(150)", nullable = true)
    private String responsavel;
    
    @Column(name = "telefoneResponsavel", columnDefinition = "VARCHAR(15)", nullable = true)
    private String telefoneResponsavel;
    
    @Email(message = "Digite um Email valido")
    @Column(name = "emailResponsavel", columnDefinition = "VARCHAR(150)", nullable = false)
    private String emailResponsavel;

    @Column(name = "possuiDoenca", columnDefinition = "VARCHAR(150)", nullable = true)
    private String possuiDoenca; 

    @Column(name = "qualDoenca", columnDefinition = "VARCHAR(150)", nullable = true)
    private String qualDoenca;

    @Column(name = "medicacao", columnDefinition = "VARCHAR(150)", nullable = true)
    private String medicacao;

    @Column(name = "tipoSanguineo", columnDefinition = "VARCHAR(150)", nullable = true)
    private String tipoSanguineo;

    @Column(name = "serieturma", columnDefinition = "VARCHAR(150)", nullable = true)
    private String serieturma;

    @Column(name = "turnoesc", columnDefinition = "VARCHAR(150)", nullable = true)
    private String turnoesc;

    private Boolean autorizacaoImagem;

    private Boolean atividadesExtras;

   @Column(name = "descricaoAtividadesExtras", columnDefinition = "VARCHAR(150)", nullable = true)
    private String descricaoAtividadesExtras;

    private Boolean necessidadesEspeciais;

    @Column(name = "descricaoNecessidadesEspeciais", columnDefinition = "VARCHAR(150)", nullable = true)
    private String descricaoNecessidadesEspeciais;


}
      


