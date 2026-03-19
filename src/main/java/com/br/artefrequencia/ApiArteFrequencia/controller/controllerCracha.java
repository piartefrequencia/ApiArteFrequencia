package com.br.artefrequencia.ApiArteFrequencia.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import com.br.artefrequencia.ApiArteFrequencia.model.Aluno;
import com.br.artefrequencia.ApiArteFrequencia.repository.RepositoryAluno;
import com.br.artefrequencia.ApiArteFrequencia.service.CrachaService;


@RestController

@RequestMapping("/api/artefrequencia")

public class controllerCracha {

    @Autowired
    RepositoryAluno repositoryAluno;

    @Autowired
    CrachaService crachaservico;

    @GetMapping("/aluno/{id}/cracha.png")
    public ResponseEntity<?> crachaIndividual(@PathVariable Long id) {
        try {
            Optional<Aluno> opt = repositoryAluno.findById(id);
            if (opt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aluno não encontrado");
            }

            Aluno aluno = opt.get();
            byte[] png = crachaservico.gerarCrachaPNG(aluno);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=cracha-" + aluno.getMatricula() + ".png");
            headers.setContentLength(png.length);

            return new ResponseEntity<>(png, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao gerar crachá: " + e.getMessage());
        }
    }
    
}
