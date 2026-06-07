package com.br.artefrequencia.ApiArteFrequencia.controller;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.artefrequencia.ApiArteFrequencia.enums.Perfil;
import com.br.artefrequencia.ApiArteFrequencia.model.Db1.Usuario;
import com.br.artefrequencia.ApiArteFrequencia.repository.Db1.RepositoryUsuario;
import com.br.artefrequencia.ApiArteFrequencia.security.JwtUtil;


@RestController

@RequestMapping("/api/artefrequencia/auth")
public class controllerRefresh {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RepositoryUsuario repositoryUsuario;

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> body) {

    String refreshToken = body.get("refreshToken");

    if (!jwtUtil.validateToken(refreshToken) || !jwtUtil.isRefreshToken(refreshToken)) {
        return ResponseEntity.status(401).build();
    }

    String email = jwtUtil.extractEmail(refreshToken);

    Usuario user = repositoryUsuario.findByEmail(email).orElse(null);

    if (user == null) {
        return ResponseEntity.status(401).build();
    }

    boolean permitido =
            user.getPerfil() == Perfil.ADMIN ||
            user.getPerfil() == Perfil.COLAB;

    if (!permitido) {
        return ResponseEntity.status(403).build();
    }

    String newAccessToken = jwtUtil.generateAccessToken(user);

    return ResponseEntity.ok(Map.of("token", newAccessToken));
  }

}
