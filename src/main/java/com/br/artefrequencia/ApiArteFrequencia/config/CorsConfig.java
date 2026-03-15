package com.br.artefrequencia.ApiArteFrequencia.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Configuration
public class CorsConfig {

    @Value("${CORS_ORIGIN}")
    private String corsOrigin;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        List<String> allowedOrigins = Arrays.stream(corsOrigin.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .collect(Collectors.toList());

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(allowedOrigins);

        // Métodos liberados
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

        // Headers que o frontend pode enviar
        config.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept", "Origin"));

        // Headers que o navegador pode expor para o JS ler (se necessário)
        config.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}


