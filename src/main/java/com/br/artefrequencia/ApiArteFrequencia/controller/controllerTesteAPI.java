package com.br.artefrequencia.ApiArteFrequencia.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

@RequestMapping("${API_BASE_PATH}")

public class controllerTesteAPI {

  // PARA TESTAR O FUNCIONAMENTO DA API

  @GetMapping("/mensagem")
  public String saudacao() {
    
    return "API RODANDO BOTA PRA TORAR EQUIPE ARTE & FREQUENCIA";
  }

}
