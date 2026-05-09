package com.br.artefrequencia.ApiArteFrequencia.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class TelegramService {

    @Value("${telegram.bot.token}") 
    private String botToken;

    private final RestTemplate rest = new RestTemplate();

    public void enviarMensagem(String chatId, String texto) {
        try {
            if (botToken == null || botToken.isBlank() || chatId == null || chatId.isBlank()) {
                return;
            }
            String url = "https://api.telegram.org/bot" + botToken + "/sendMessage";
            
            Map<String, Object> body = Map.of(
                "chat_id", chatId, 
                "text", texto,
                "parse_mode", "Markdown" 
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            ResponseEntity<String> response = rest.postForEntity(url, new HttpEntity<>(body, headers), String.class);
            
            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("✅ Telegram enviado com sucesso para: " + chatId);
            }
        } catch (Exception e) {
            
            System.err.println("❌ Erro ao enviar mensagem para o Telegram: " + e.getMessage());
        }
    }
}
