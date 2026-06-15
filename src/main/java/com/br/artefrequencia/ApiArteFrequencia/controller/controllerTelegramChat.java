package com.br.artefrequencia.ApiArteFrequencia.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.br.artefrequencia.ApiArteFrequencia.model.Db1.ResponsavelChat;
import com.br.artefrequencia.ApiArteFrequencia.repository.Db1.RepositoryResponsavelChat;    
    
@RestController
@RequestMapping("/api/artefrequencia")
public class controllerTelegramChat {

    @Autowired
    private RepositoryResponsavelChat repository;

    @GetMapping("/status/{alunoId}")
    public ResponseEntity<?> status(@PathVariable Long alunoId) {
        try {
            List<ResponsavelChat> chats = repository.findAll();

            Map<String, Boolean> status = new HashMap<>();
            status.put("PAI", false);
            status.put("MAE", false);
            status.put("RESPONSAVEL", false);

            for (ResponsavelChat chat : chats) {
                if (chat.getAlunoId().equals(alunoId)) {
                    status.put(chat.getTipo(), chat.getAtivo());
                }
            }

            return ResponseEntity.ok(status);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao verificar status: " + e.getMessage());
        }
    }

    @DeleteMapping("/remover/{alunoId}/{tipo}")
    public ResponseEntity<?> remover(@PathVariable Long alunoId, @PathVariable String tipo) {
        try {
            List<ResponsavelChat> chats = repository.findAll();

            boolean encontrado = false;

            for (ResponsavelChat chat : chats) {
                if (chat.getAlunoId().equals(alunoId)
                        && chat.getTipo().equalsIgnoreCase(tipo)) {
                    chat.setAtivo(false);
                    repository.save(chat);
                    encontrado = true;
                }
            }

            if (!encontrado) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("Usuário não encontrado para esse tipo");
            }

            return ResponseEntity.ok("Usuário (" + tipo + ") desativado com sucesso");

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao remover: " + e.getMessage());
        }
    }
}



