package com.br.artefrequencia.ApiArteFrequencia.config;

import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class TelegramBotInitializer {

    private final TelegramBotConfig telegramBotConfig;

    @PostConstruct
    public void init() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(telegramBotConfig);
            System.out.println("✅ LOG ARTE FREQUENCIA: Bot do Telegram registrado e escutando com sucesso!");
        } catch (TelegramApiException e) {
            System.err.println("❌ LOG ARTE FREQUENCIA: Erro ao registrar o bot do Telegram: " + e.getMessage());
        }
    }
}