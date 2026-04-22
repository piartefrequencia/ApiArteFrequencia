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

    Aluno aluno = repositoryaluno.findById(req.getAlunoId())
        .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado"));

    Presenca presenca = new Presenca();
    presenca.setAlunoId(aluno.getId()); 
    presenca.setTipo(req.getTipo() != null ? req.getTipo().toUpperCase() : "ENTRADA");
    presenca.setOrigem("APP_QR");
    presenca.setLidoEm(LocalDateTime.now());

    repositorypresenca.save(presenca);

    String msg = montarMensagem(aluno, presenca);

    repositorychat.findByAlunoIdAndAtivoTrue(aluno.getId())
        .forEach(c -> servicotelegram.enviarMensagem(c.getChatId(), msg));
}


    private String montarMensagem(Aluno aluno, Presenca presenca) {
        String nome = aluno.getNome() != null ? aluno.getNome() : "aluno";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");
        String momento = presenca.getLidoEm().format(dtf);

        String acao = "SAIDA".equalsIgnoreCase(presenca.getTipo()) ? "encerrou as atividades" : "iniciou as atividades";
        
        return String.format("🎵 O aluno %s %s no dia %s na Associação Pro‑Cidadania.", nome, acao, momento);
    }
}