package com.br.artefrequencia.ApiArteFrequencia.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.br.artefrequencia.ApiArteFrequencia.dto.VincularChatRequest;
import com.br.artefrequencia.ApiArteFrequencia.service.PresencaService;

import lombok.RequiredArgsConstructor;
  
@Component
@RequiredArgsConstructor
public class TelegramBotConfig extends TelegramLongPollingBot {

    private final PresencaService presencaService;

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Override
    public String getBotToken() {
        return botToken;
    }
    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            String username = update.getMessage().getFrom().getUserName();

            if (messageText.startsWith("/start")) {
                try {
                    String parameter = messageText.replace("/start", "").trim();

                    if (!parameter.isEmpty() && parameter.contains("_")) {
                        String[] partes = parameter.split("_");
                        Long alunoId = Long.parseLong(partes[0]);
                        String tipo = partes[1]; 

                        VincularChatRequest vinculo = new VincularChatRequest();
                        vinculo.setAlunoId(alunoId);
                        vinculo.setChatId(chatId.toString());
                        vinculo.setTipo(tipo);
                        vinculo.setUsername(username != null ? username : "SemUsername");

                        presencaService.vincularTelegram(vinculo);

                        enviarResposta(chatId,
                                "✅ Sucesso! Agora você receberá as notificações de " + tipo + " para este aluno.");
                    } else {
                        enviarResposta(chatId,
                                "👋 Bem-vindo! Use os botões no sistema da escola para vincular seu acesso.");
                    }
                } catch (Exception e) {
                    enviarResposta(chatId, "❌ Erro ao processar vínculo. Tente novamente pelo link do sistema.");
                }
            }
        }
    }

    private void enviarResposta(Long chatId, String texto) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(texto);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}