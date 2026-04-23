package com.br.artefrequencia.ApiArteFrequencia.service;

import org.springframework.stereotype.Service;
import com.br.artefrequencia.ApiArteFrequencia.dto.RegistrarFrequenciaRequest;
import com.br.artefrequencia.ApiArteFrequencia.model.Db1.Aluno;
import com.br.artefrequencia.ApiArteFrequencia.model.Db1.Presenca;
import com.br.artefrequencia.ApiArteFrequencia.repository.Db1.RepositoryAluno;
import com.br.artefrequencia.ApiArteFrequencia.repository.Db1.RepositoryPresenca;
import com.br.artefrequencia.ApiArteFrequencia.repository.Db1.RepositoryResponsavelChat;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor; 
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor 
@Transactional
public class PresencaService {

    private final RepositoryAluno repositoryaluno;
    private final RepositoryPresenca repositorypresenca;
    private final RepositoryResponsavelChat repositorychat;
    private final TelegramService servicotelegram;

    public void registrar(RegistrarFrequenciaRequest req) {
       
        if (req.getAlunoId() == null) {
            throw new IllegalArgumentException("ID do aluno não fornecido pelo QR Code");
        }

        Aluno aluno = repositoryaluno.findById(req.getAlunoId())
            .orElseThrow(() -> new IllegalArgumentException("Aluno com ID " + req.getAlunoId() + " não encontrado no sistema"));

        Presenca presenca = new Presenca();
        presenca.setAlunoId(aluno.getId()); 
        
        String tipoFinal = (req.getTipo() == null || req.getTipo().trim().isEmpty()) ? "ENTRADA" : req.getTipo().toUpperCase();
        presenca.setTipo(tipoFinal);
        presenca.setOrigem("APP_QR");
        presenca.setLidoEm(LocalDateTime.now());

        repositorypresenca.save(presenca);

        try {
            String msg = montarMensagem(aluno, presenca);
            repositorychat.findByAlunoIdAndAtivoTrue(aluno.getId())
                .forEach(chat -> {
                    if (chat.getChatId() != null && !chat.getChatId().isBlank()) {
                        servicotelegram.enviarMensagem(chat.getChatId(), msg);
                    }
                });
        } catch (Exception e) {
            
            System.err.println("Aviso: Falha ao enviar notificação Telegram: " + e.getMessage());
        }
    }

    private String montarMensagem(Aluno aluno, Presenca presenca) {
        String nome = (aluno.getNome() != null) ? aluno.getNome() : "Aluno";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");
        String momento = presenca.getLidoEm().format(dtf);

        String acao = "SAIDA".equalsIgnoreCase(presenca.getTipo()) ? "encerrou as atividades" : "iniciou as atividades";
        
        return String.format("🎵 O aluno %s %s no dia %s na Associação Pró‑Cidadania.", nome, acao, momento);
    }
}