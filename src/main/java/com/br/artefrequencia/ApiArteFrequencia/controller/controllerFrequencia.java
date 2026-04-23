package com.br.artefrequencia.ApiArteFrequencia.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.br.artefrequencia.ApiArteFrequencia.dto.RegistrarFrequenciaRequest;
import com.br.artefrequencia.ApiArteFrequencia.dto.RegistrarFrequenciaResponse;
import com.br.artefrequencia.ApiArteFrequencia.service.PresencaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/artefrequencia/frequencia")
@RequiredArgsConstructor
public class controllerFrequencia  {

    private final PresencaService service;

    @PostMapping("/registrar")
    
    public ResponseEntity<RegistrarFrequenciaResponse> registrar(@RequestBody RegistrarFrequenciaRequest req) {
        RegistrarFrequenciaResponse res = new RegistrarFrequenciaResponse();
    try {
        service.registrar(req);
        res.setOk(true);
        res.setMensagem("Frequência registrada com sucesso!");
        return ResponseEntity.ok(res);
    } catch (IllegalArgumentException e) {
        res.setOk(false);
        res.setMensagem(e.getMessage()); // Retorna "Aluno não encontrado"
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
    } catch (Exception e) {
        res.setOk(false);
        res.setMensagem("Erro ao processar: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }
  }
}
