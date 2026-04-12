package com.br.artefrequencia.ApiArteFrequencia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import com.br.artefrequencia.ApiArteFrequencia.model.Db1.Aluno;
import com.br.artefrequencia.ApiArteFrequencia.repository.Db1.RepositoryAluno;
import com.br.artefrequencia.ApiArteFrequencia.service.CrachaService;

@RestController
@RequestMapping("/api/artefrequencia")
public class controllerCracha {

    @Autowired
    RepositoryAluno repositoryAluno;

    @Autowired
    CrachaService crachaservico;

    @GetMapping("/aluno/{matricula}/cracha.png")
    public ResponseEntity<?> crachaIndividual(@PathVariable("matricula") Long matricula) {
        try {
            Aluno aluno = repositoryAluno.findByMatricula(matricula.intValue());
            
            if (aluno == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Aluno com matrícula " + matricula + " não encontrado.");
            }
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