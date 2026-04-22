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
        try {
            service.registrar(req);
            return ResponseEntity.ok(new RegistrarFrequenciaResponse());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

