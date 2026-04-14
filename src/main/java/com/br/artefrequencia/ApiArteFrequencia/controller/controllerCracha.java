package com.br.artefrequencia.ApiArteFrequencia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.br.artefrequencia.ApiArteFrequencia.model.Db1.Aluno;
import com.br.artefrequencia.ApiArteFrequencia.repository.Db1.RepositoryAluno;
import com.br.artefrequencia.ApiArteFrequencia.service.CrachaService;

import java.util.Optional;

@RestController
@RequestMapping("/api/artefrequencia")
public class controllerCracha{

    @Autowired
    private RepositoryAluno repositoryAluno;

    @Autowired
    private CrachaService crachaservico;

    @GetMapping("/aluno/{id}/cracha.png")
    public ResponseEntity<?> crachaIndividual(@PathVariable("id") Long id) {
        try {
            Optional<Aluno> alunoOpt = repositoryAluno.findById(id);
            if (alunoOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Erro: Aluno com ID " + id + " não encontrado no sistema.");
            }
            Aluno aluno = alunoOpt.get();

            byte[] png = crachaservico.gerarCrachaPNG(aluno);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);

            String filename = "cracha-" + aluno.getNome().toLowerCase().replace(" ", "-") + ".png";
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + filename);
            headers.setContentLength(png.length);

            return new ResponseEntity<>(png, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno ao processar o crachá: " + e.getMessage());
        }
    }
}
