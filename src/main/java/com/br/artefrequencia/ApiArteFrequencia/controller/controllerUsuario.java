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

import com.br.artefrequencia.ApiArteFrequencia.model.Usuario;
import com.br.artefrequencia.ApiArteFrequencia.repository.RepositoryUsuario;
import com.br.artefrequencia.ApiArteFrequencia.util.PasswordBCript;

import jakarta.validation.Valid;

@RestController

@CrossOrigin(origins = "${CORS_ORIGIN}")

@RequestMapping("/api/artefrequencia")

public class controllerUsuario {

    @Autowired
    RepositoryUsuario repositoryusuario;

    // INICIO DO CRUD

    // CADASTRA OS USUARIOS

    @PostMapping("/usuario")
    public ResponseEntity<?> cadastrar(@Valid @RequestBody Usuario usuario) {

        String hashSenha = PasswordBCript.encoder(usuario.getSenha());
        usuario.setSenha(hashSenha);

        try {
            usuario.setId(null);

            if (repositoryusuario.existsByEmail(usuario.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Já existe um usuário com este email.");
            }

            Usuario salvo = repositoryusuario.save(usuario);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("Usuário criado com sucesso! ID: " + salvo.getId());

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao cadastrar usuário: " + e.getMessage());
        }
    }

    // LISTA TODOS OS USUARIOS

    @GetMapping("/usuario")
    public List<Usuario> listar() {
        return repositoryusuario.findAll();
    }

    // BUSCA OS USUARIOS POR ID

    @GetMapping("/usuario/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {

        Optional<Usuario> usuarioOpt = repositoryusuario.findById(id);

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Usuário com ID " + id + " não encontrado.");
        }

        return ResponseEntity.ok(usuarioOpt.get());
    }

    // ATUALIZA OS USUARIOS CADASTRADOS

    @PutMapping("/usuario/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Usuario usuario) {

        try {
            Optional<Usuario> existenteOpt = repositoryusuario.findById(id);

            if (existenteOpt.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("Usuário com ID " + id + " não encontrado.");
            }

            Usuario existente = existenteOpt.get();

            existente.setEmail(usuario.getEmail());
            existente.setUsuario(usuario.getUsuario());

            Usuario atualizado = repositoryusuario.save(existente);

            return ResponseEntity.ok("Usuário atualizado com sucesso! ID: " + atualizado.getId());

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar usuário: " + e.getMessage());
        }
    }

    // DELETA OS USUARIOS CADASTRADOS

    @DeleteMapping("/usuario/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            Optional<Usuario> existenteOpt = repositoryusuario.findById(id);

            if (existenteOpt.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("Usuário com ID " + id + " não encontrado.");
            }

            repositoryusuario.delete(existenteOpt.get());

            return ResponseEntity.ok("Usuário com ID " + id + " deletado com sucesso.");

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao deletar usuário: " + e.getMessage());
        }
    }

}
