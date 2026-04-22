package com.br.artefrequencia.ApiArteFrequencia.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class TelegramService {

    @Value("${telegram.bot.token:}")
    private String botToken;

    private final RestTemplate rest = new RestTemplate();

    public void enviarMensagem(String chatId, String texto) {
        try {
            if (botToken == null || botToken.isBlank()) return;
            if (chatId == null || chatId.isBlank()) return;

            String url = "https://api.telegram.org/bot" + botToken + "/sendMessage";
            Map<String, Object> body = Map.of("chat_id", chatId, "text", texto);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            rest.postForEntity(url, new HttpEntity<>(body, headers), String.class);
        } catch (Exception ignored) { }
    }
}
