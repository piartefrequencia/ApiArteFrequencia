package com.br.artefrequencia.ApiArteFrequencia.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.br.artefrequencia.ApiArteFrequencia.model.Administrador;
import com.br.artefrequencia.ApiArteFrequencia.repository.RepositoryAdministardor;
import com.br.artefrequencia.ApiArteFrequencia.util.PasswordBCript;

import jakarta.validation.Valid;

@RestController

@CrossOrigin(origins = "http://localhost:3000")

@RequestMapping("/api/artefrequencia")

public class controllerAdministador {

    @Autowired
    RepositoryAdministardor repositoryAdministrador;

    // INICIO DO CRUD

    @PostMapping("/administrador")
    public ResponseEntity<?> cadastrar(@Valid @RequestBody Administrador administrador) {

        String hashSenha = PasswordBCript.encoder(administrador.getSenha());
        administrador.setSenha(hashSenha);

        try {

            administrador.setMatricula(null);

            if (repositoryAdministrador.existsByEmail(administrador.getEmail())) {
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body("Já existe um administrador com esse email.");
            }
            Administrador resposta = repositoryAdministrador.save(administrador);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("Administrador criado com sucesso" + "" + resposta.getMatricula());

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao cadastrar administrador " + e.getMessage());
        }
    }

    /*
     * @PostMapping("artefrequencia/adiministador")
     * public ResponseEntity<Administrador> cadastrar(@Valid @RequestBody
     * Administrador administrador) {
     * 
     * administrador.matricula = null;
     * 
     * 
     * Administrador resposta = repositoryAdministrador.save(administrador);
     * 
     * return new ResponseEntity<>(resposta, HttpStatus.CREATED);
     * // return (ResponseEntity<Administrador>)
     * servicoadministrador.cadastrar(administrador);
     * 
     * }
     */

    @GetMapping("/administador")
    public List<Administrador> listaAdministrador() {

        return repositoryAdministrador.findAll();
    }

    @GetMapping("/administador/{matricula}")
    public Administrador listaPelaMatricula(@PathVariable int matricula) {

        return repositoryAdministrador.findByMatricula(matricula);

    }

    /*
     * @PutMapping("artefrequencia/adiministador/{matricula}")
     * public ResponseEntity<Administrador> atualizar(@PathVariable Long matricula,
     * 
     * @RequestBody Administrador administrador) {
     * return repositoryAdministrador.findById(matricula)
     * .map(existente -> {
     * existente.setNome(administrador.getNome());
     * existente.setSobrenome(administrador.getSobrenome());
     * existente.setEmail(administrador.getEmail());
     * existente.setSenha(administrador.getSenha());
     * Administrador atualizado = repositoryAdministrador.save(existente);
     * 
     * return ResponseEntity.ok(atualizado);
     * })
     * .orElse(ResponseEntity.notFound().build());
     * 
     * }
     */

    @PutMapping("/administador/{matricula}")
    public ResponseEntity<?> atualizar(@PathVariable Long matricula,
            @RequestBody Administrador administrador) {
        try {
            Optional<Administrador> existenteOpt = repositoryAdministrador.findById(matricula);

            if (existenteOpt.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("Administrador com matrícula " + matricula + " não encontrado.");
            }

            Administrador existente = existenteOpt.get();

            existente.setNome(administrador.getNome());
            existente.setUsuario(administrador.getUsuario());
            existente.setEmail(administrador.getEmail());
            existente.setSenha(administrador.getSenha());

            Administrador atualizado = repositoryAdministrador.save(existente);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("Administrador atualizado com sucesso. " + "" + atualizado.getMatricula());

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar administrador: " + e.getMessage());
        }
    }

    /*
     * @DeleteMapping("artefrequencia/adiministador/{matricula}")
     * public ResponseEntity<Void> deletar(@PathVariable Long matricula) {
     * return repositoryAdministrador.findById(matricula)
     * .map(existente -> {
     * repositoryAdministrador.delete(existente);
     * return ResponseEntity.noContent().<Void>build();
     * })
     * .orElse(ResponseEntity.notFound().build());
     * }
     * 
     * }
     */

    @DeleteMapping("/administador/{matricula}")
    public ResponseEntity<?> deletar(@PathVariable Long matricula) {
        try {
            Optional<Administrador> existenteOpt = repositoryAdministrador.findById(matricula);

            if (existenteOpt.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("Administrador com matrícula " + matricula + " não encontrado.");
            }

            repositoryAdministrador.delete(existenteOpt.get());

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("Administrador com matrícula " + matricula + " deletado com sucesso.");

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao deletar administrador: " + e.getMessage());
        }
    }
}
