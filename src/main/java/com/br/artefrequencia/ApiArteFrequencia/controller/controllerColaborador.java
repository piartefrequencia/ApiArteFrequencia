package com.br.artefrequencia.ApiArteFrequencia.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableTypeProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.artefrequencia.ApiArteFrequencia.model.Colaborador;
import com.br.artefrequencia.ApiArteFrequencia.repository.RepositoryColaborador;
import com.br.artefrequencia.ApiArteFrequencia.util.PasswordBCript;

import jakarta.validation.Valid;

@RestController

@CrossOrigin(origins = "${CORS_ORIGIN}")

@RequestMapping("/api/artefrequencia")

public class controllerColaborador {

    @Autowired
    RepositoryColaborador repositoryColaborador;
    public String existenteOpt;

    // INICIO DO CRUD

    // CADASTRA OS COLABORADORES

    @PostMapping("/colaborador")
    public ResponseEntity<?> cadastrar(@Valid @RequestBody Colaborador colaborador) {

        String hashSenha = PasswordBCript.encoder(colaborador.getSenha());
        colaborador.setSenha(hashSenha);

        try {
            colaborador.setMatricula(null);

            if (repositoryColaborador.existsByEmail(colaborador.getEmail())) {
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body("Já existe um colaborador com esse email.");
            }
            if (repositoryColaborador.existsByCpf(colaborador.getCpf())) {
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body("Já existe um colaborador com esse CPF");
            }
            Colaborador resposta = repositoryColaborador.save(colaborador);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("Colaborador criado com sucesso" + "" + resposta.getMatricula());

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("erro ao cadastrar coloborador" + "" + e.getMessage());
        }
    }

    // LISTA COLABORADORES

    @GetMapping("/colaborador")
    public List<Colaborador> listaColaborador() {

        return repositoryColaborador.findAll();
    }

    // BUSCA COLABORADORES PELA MATRICULA

    @GetMapping("/colaborador/{matricula}")
    public Colaborador listaPelaMatricula(@PathVariable int matricula) {

        return repositoryColaborador.findByMatricula(matricula);
    }

    // ATUALIZA OS COLABORADORES

    @PutMapping("/colaborador/{matricula}")
    public ResponseEntity<?> atualizar(@PathVariable Long matricula,
            @RequestBody Colaborador colaborador) {
        try {
            Optional<Colaborador> existenteOpt = repositoryColaborador.findById(matricula);

            if (existenteOpt.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("Colaborador com matricula" + matricula + "não encontardo.");
            }

            Colaborador existente = existenteOpt.get();

            existente.setNome(colaborador.getNome());
            existente.setCpf(colaborador.getCpf());
            existente.setRg(colaborador.getRg());
            existente.setDataExpedRg(colaborador.getDataExpedRg());
            existente.setDataNascimento(colaborador.getDataNascimento());
            existente.setIdade(colaborador.getIdade());
            existente.setAreaInstrucao(colaborador.getAreaInstrucao());
            existente.setFormacao(colaborador.getFormacao());
            existente.setApelido(colaborador.getApelido());
            existente.setRedeSocial(colaborador.getRedeSocial());
            existente.setTelefone(colaborador.getTelefone());
            existente.setEmail(colaborador.getEmail());

            Colaborador atualizado = repositoryColaborador.save(existente);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("Colaborador cadastrado com sucesso." + "" + atualizado.getMatricula());

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao cadastrar colaborador");
        }
    }

    // EXCLUI OS COLABORADOES CADASTRADOS

    @DeleteMapping("/colaborador/{matricula}")
    public ResponseEntity<?> deletar(@PathVariable Long matricula) {
        try {
            Optional<Colaborador> existentOpt = repositoryColaborador.findById(matricula);

            if (existentOpt.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("Colaborador com a matricula" + matricula + "não encontrado.");

            }

            repositoryColaborador.delete(existentOpt.get());

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("Colaborador com matricula" + matricula + " deletado com sucesso.");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao deletar colaborador: " + e.getMessage());

        }
    }

}
