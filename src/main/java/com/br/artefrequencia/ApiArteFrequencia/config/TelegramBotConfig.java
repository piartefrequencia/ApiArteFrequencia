package com.br.artefrequencia.ApiArteFrequencia.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.br.artefrequencia.ApiArteFrequencia.dto.VincularChatRequest;
import com.br.artefrequencia.ApiArteFrequencia.service.PresencaService;



@Component
public class TelegramBotConfig extends TelegramLongPollingBot {

    @Autowired
    private PresencaService presencaService;

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
  
            System.out.println("LOG ARTE FREQUENCIA: Mensagem recebida de " + chatId + ": " + messageText);

            if (messageText.startsWith("/start")) {
                try {
                    String parameter = "";
                    if (messageText.contains(" ")) {
                        parameter = messageText.substring(messageText.indexOf(" ")).trim();
                    }

                    System.out.println("LOG ARTE FREQUENCIA: Parâmetro extraído: [" + parameter + "]");

                    if (!parameter.isEmpty() && parameter.contains("_")) {
                        String[] partes = parameter.split("_");
                        Long alunoId = Long.parseLong(partes[0]);
                        String tipo = partes[1].toUpperCase(); 

                        VincularChatRequest vinculo = new VincularChatRequest();
                        vinculo.setAlunoId(alunoId);
                        vinculo.setChatId(chatId.toString());
                        vinculo.setTipo(tipo);
                        vinculo.setUsername(username != null ? username : "SemUsername");

                        // Invoca o serviço que agora salva no banco e devolve o nome do aluno
                        String nomeAluno = presencaService.vincularTelegram(vinculo);

                        // Tratamento visual para o Tipo de Responsável ficar bonito na leitura
                        String tipoFormatado = tipo;
                        if ("MAE".equals(tipo)) tipoFormatado = "Mãe";
                        if ("PAI".equals(tipo)) tipoFormatado = "Pai";
                        if ("RESPONSAVEL".equals(tipo)) tipoFormatado = "Responsável";

                        // Mensagem aprimorada para melhor entendimento da família
                        String mensagemAcolhedora = 
                            "✅ *Vínculo realizado com sucesso!*\n\n" +
                            "Olá! A partir de agora, você receberá aqui no Telegram as notificações de *" + tipoFormatado + "* " +
                            "sobre a frequência do(a) aluno(a) *\uD83D\uDC66 " + nomeAluno + "*, referente às suas atividades " +
                            "na *Associação Pró-Cidadania*.\n\n" +
                            "\uD83D\uDD14 _Não se preocupe, avisaremos você assim que a presença for registrada!_";

                        enviarResposta(chatId, mensagemAcolhedora);
                        
                        System.out.println("LOG ARTE FREQUENCIA: Sucesso ao vincular Aluno " + nomeAluno + " ao Chat " + chatId);
                        
                    } else {
                        enviarResposta(chatId, "👋 Olá! Para receber as notificações de frequência, use o link oficial enviado pela coordenação da *Associação Pró‑Cidadania*.");
                    }
                } catch (Exception e) {
                    System.err.println("LOG ARTE FREQUENCIA: Erro ao processar vínculo: " + e.getMessage());
                    e.printStackTrace();
                    enviarResposta(chatId, "❌ Ocorreu um erro ao realizar o vínculo. Por favor, solicite um novo link à coordenação.");
                }
            }
        }
    }

    private void enviarResposta(Long chatId, String texto) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(texto);
        message.setParseMode("Markdown"); 
        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.err.println("LOG ARTE FREQUENCIA: Falha ao enviar resposta para Telegram: " + e.getMessage());
        }
    }
}