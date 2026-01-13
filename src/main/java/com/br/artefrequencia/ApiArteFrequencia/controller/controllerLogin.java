package com.br.artefrequencia.ApiArteFrequencia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.artefrequencia.ApiArteFrequencia.dto.LoginRequest;
import com.br.artefrequencia.ApiArteFrequencia.dto.LoginResponse;
import com.br.artefrequencia.ApiArteFrequencia.model.Usuario;
import com.br.artefrequencia.ApiArteFrequencia.repository.RepositoryUsuario;
import com.br.artefrequencia.ApiArteFrequencia.security.JwtUtil;
import com.br.artefrequencia.ApiArteFrequencia.util.PasswordBCript;

import jakarta.validation.Valid;

@RestController

@CrossOrigin(origins = "${CORS_ORIGIN}")

@RequestMapping("/api/artefrequencia")

public class controllerLogin {


    @Autowired
    private RepositoryUsuario repositoryusuario;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
        Usuario user = repositoryusuario.findByEmail(req.getEmail()).orElse(null);

        if (user == null || !PasswordBCript.matches(req.getSenha(), user.getSenha())) {
            return ResponseEntity.status(401)
                                 .body("Credenciais inválidas");
        }
        if (user.getPerfil() == null) {
            return ResponseEntity.status(401)
                                 .body("Perfil do usuário inválido");
        }

        String token = jwtUtil.generateToken(user);

        return ResponseEntity.ok(new LoginResponse(
                token,
                user.getPerfil().name(),
                user.getUsuario(),
                user.getEmail()));
    }

    
}