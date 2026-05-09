package com.br.artefrequencia.ApiArteFrequencia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.br.artefrequencia.ApiArteFrequencia.dto.RegistrarFrequenciaRequest;
import com.br.artefrequencia.ApiArteFrequencia.dto.RegistrarFrequenciaResponse;
import com.br.artefrequencia.ApiArteFrequencia.dto.VincularChatRequest;
import com.br.artefrequencia.ApiArteFrequencia.model.Db1.ResponsavelChat;
import com.br.artefrequencia.ApiArteFrequencia.repository.Db1.RepositoryResponsavelChat;
import com.br.artefrequencia.ApiArteFrequencia.repository.Db1.RepositoryUsuario;
import com.br.artefrequencia.ApiArteFrequencia.service.PresencaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/artefrequencia/frequencia")
@RequiredArgsConstructor
public class controllerFrequencia  {

    @Autowired
    private RepositoryResponsavelChat repositorychat;

    private final PresencaService service;

    @PostMapping("/registrar")
    
    public ResponseEntity<RegistrarFrequenciaResponse> registrar(@RequestBody RegistrarFrequenciaRequest req) {
        RegistrarFrequenciaResponse resposta = new RegistrarFrequenciaResponse();
    try {
        service.registrar(req);
        resposta.setOk(true);
        resposta.setMensagem("Frequência registrada com sucesso!");
        return ResponseEntity.ok(resposta);
    } catch (IllegalArgumentException e) {
        resposta.setOk(false);
        resposta.setMensagem(e.getMessage()); 
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resposta);
    } catch (Exception e) {
        resposta.setOk(false);
        resposta.setMensagem("Erro ao processar: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resposta);
    }
  }
  
@PostMapping("/vincular-telegram")
public ResponseEntity<RegistrarFrequenciaResponse> vincular(@RequestBody VincularChatRequest req) {
    RegistrarFrequenciaResponse resposta = new RegistrarFrequenciaResponse();
    try {
        service.vincularTelegram(req); 
        resposta.setOk(true);
        resposta.setMensagem("Telegram vinculado com sucesso!");
        return ResponseEntity.ok(resposta);
    } catch (Exception e) {
        resposta.setOk(false);
        resposta.setMensagem("Erro ao vincular: " + e.getMessage());
        return ResponseEntity.badRequest().body(resposta);
    }
  }

}
