package com.br.artefrequencia.ApiArteFrequencia.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.br.artefrequencia.ApiArteFrequencia.model.Partitura;
import com.br.artefrequencia.ApiArteFrequencia.repository.RepositoryPartitura;

@RestController

@CrossOrigin(origins = "${CORS_ORIGIN}")

@RequestMapping("${API_BASE_PATH}")

public class controllerPartitura {

    @Autowired
    private RepositoryPartitura repositorypartitura;

    // INICIO DO CRUD

    // LISTA TODAS AS PARTITURAS

    @GetMapping("/partitura")

    public ResponseEntity<?> listar() {
        try {
            List<Partitura> partituras = repositorypartitura.findAll();
            if (partituras.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NO_CONTENT)
                        .body("Nenhum arquivo encontrado");
            }
            return ResponseEntity.ok(partituras);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao Listar arquivos:" + e.getMessage());

        }
    }

    // UPLOAD DAS PARTITURAS

    @PostMapping("/partitura")

    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Arquivo vazio");
            }

            Partitura partitura = new Partitura();
            partitura.setNome(file.getOriginalFilename());
            partitura.setTipo(file.getContentType());
            partitura.setConteudo(file.getBytes());

            Partitura salvo = repositorypartitura.save(partitura);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(salvo);

        } catch (IOException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao ler arquivo: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao salvar arquivo:" + e.getMessage());
        }
    }

    // EXCLUI ARQUIVOS

    @DeleteMapping("/partitura/{id}")

    public ResponseEntity<?> excluir(@PathVariable Long id) {
        try {
            if (!repositorypartitura.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Arquivo não encontrado");
            }
            repositorypartitura.deleteById(id);
            return ResponseEntity.ok("Arquivo excluído com sucesso");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao excluir arquivo: " + e.getMessage());
        }
    }

    // PARA O DOWNLOAD DOS ARQUIVOS

    @GetMapping("/partitura/{id}/download")

    public ResponseEntity<?> download(@PathVariable Long id) {
        try {
            Partitura partitura = repositorypartitura.findById(id)
                    .orElse(null);
            if (partitura == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("Arquivo não encontrado");
            }

            ByteArrayResource resource = new ByteArrayResource(partitura.getConteudo());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + partitura.getNome() + "\"")
                    .contentType(MediaType.parseMediaType(partitura.getTipo()))
                    .contentLength(partitura.getConteudo().length)
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao baixar arquivo: " + e.getMessage());
        }
    }

    // VISUALIZAÇÃO DITERO (PDF ou imagem)

    @GetMapping("/partitura/{id}/visualizar")

    public ResponseEntity<?> visualizar(@PathVariable Long id) {
        try {
            Partitura partitura = repositorypartitura.findById(id)
                    .orElse(null);
            if (partitura == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("Arquivo não encontrado");
            }

            ByteArrayResource resource = new ByteArrayResource(partitura.getConteudo());
            MediaType mediaType = partitura.getTipo() != null ? MediaType.parseMediaType(partitura.getTipo())
                    : MediaType.APPLICATION_OCTET_STREAM;

            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .contentLength(partitura.getConteudo().length)
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao visualizar arquivo: " + e.getMessage());
        }
    }

}
