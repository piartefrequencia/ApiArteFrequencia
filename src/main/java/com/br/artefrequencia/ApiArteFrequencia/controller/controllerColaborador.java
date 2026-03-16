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
import org.springframework.transaction.annotation.Transactional;

import com.br.artefrequencia.ApiArteFrequencia.enums.Perfil;
import com.br.artefrequencia.ApiArteFrequencia.model.Colaborador;
import com.br.artefrequencia.ApiArteFrequencia.model.Usuario;
import com.br.artefrequencia.ApiArteFrequencia.repository.RepositoryColaborador;
import com.br.artefrequencia.ApiArteFrequencia.repository.RepositoryUsuario;
import com.br.artefrequencia.ApiArteFrequencia.util.PasswordBCript;

import jakarta.validation.Valid;

@RestController

@RequestMapping("/api/artefrequencia")

public class controllerColaborador {

    @Autowired
    RepositoryColaborador repositoryColaborador;

    @Autowired
    private RepositoryUsuario repositoryUsuario;

    // INICIO DO CRUD

    // CADASTRA OS COLABORADORES

    @PostMapping("/colaborador")
    @Transactional
    public ResponseEntity<?> cadastrar(@Valid @RequestBody Colaborador colaborador) {
        try {
            colaborador.setId(null);

            if (colaborador.getPerfil() == null
                    || !(colaborador.getPerfil() == Perfil.PROF || colaborador.getPerfil() == Perfil.ESTAG)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Perfil inválido. Use PROF ou ESTAG.");
            }

            if (repositoryColaborador.existsByEmail(colaborador.getEmail())
                    || repositoryUsuario.existsByEmail(colaborador.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("E-mail já cadastrado.");
            }

            if (repositoryColaborador.existsByCpf(colaborador.getCpf())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("CPF já cadastrado.");
            }

            Integer novaMatricula = repositoryColaborador.gerarNovaMatricula();
            colaborador.setMatricula(novaMatricula);

            Colaborador salvo = repositoryColaborador.save(colaborador);

            Usuario usuario = new Usuario();
            usuario.setUsuario(colaborador.getNome());
            usuario.setEmail(colaborador.getEmail());
            usuario.setSenha(PasswordBCript.encoder(colaborador.getCpf()));
            usuario.setPerfil(colaborador.getPerfil());

            Usuario usuarioSalvo = repositoryUsuario.save(usuario);

            return ResponseEntity.status(HttpStatus.CREATED).body("Colaborador e Usuário criados com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao cadastrar: " + e.getMessage());
        }
    }

    @GetMapping("/colaborador")
    public List<Colaborador> listaColaborador() {
        return repositoryColaborador.findAll();
    }

    @GetMapping("/colaborador/{matricula}")
    public ResponseEntity<Colaborador> listaPelaMatricula(@PathVariable Integer matricula) {
        return repositoryColaborador.findByMatricula(matricula)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/colaborador/{matricula}")
    @Transactional
    public ResponseEntity<?> atualizar(@PathVariable Integer matricula, @RequestBody Colaborador colaborador) {
        try {
            Optional<Colaborador> existenteOpt = repositoryColaborador.findByMatricula(matricula);
            if (existenteOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Matrícula " + matricula + " não encontrada.");
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
            existente.setPerfil(colaborador.getPerfil());

            repositoryColaborador.save(existente);
            return ResponseEntity.ok("Colaborador atualizado com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao atualizar: " + e.getMessage());
        }
    }

    @DeleteMapping("/colaborador/{matricula}")
    @Transactional
    public ResponseEntity<?> deletar(@PathVariable Integer matricula) {
        try {
            Optional<Colaborador> existenteOpt = repositoryColaborador.findByMatricula(matricula);
            if (existenteOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Matrícula " + matricula + " não encontrada.");
            }
            repositoryColaborador.delete(existenteOpt.get());
            return ResponseEntity.ok("Deletado com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao deletar: " + e.getMessage());
        }
    }
}
