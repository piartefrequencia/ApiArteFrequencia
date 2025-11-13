package com.br.artefrequencia.ApiArteFrequencia.controller;

import org.springframework.web.bind.annotation.RestController;

import com.br.artefrequencia.ApiArteFrequencia.model.Administrador;
import com.br.artefrequencia.ApiArteFrequencia.model.Aluno;
import com.br.artefrequencia.ApiArteFrequencia.model.Colaborador;
import com.br.artefrequencia.ApiArteFrequencia.repository.RepositoryAluno;
import com.br.artefrequencia.ApiArteFrequencia.util.PasswordBCript;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController

@CrossOrigin(origins = "${CORS_ORIGIN}")

@RequestMapping("/api/artefrequencia")
public class controllerAluno {

    @Autowired
    RepositoryAluno repositoryAluno;
    public String existenteOpt;

    // INICIO DO CRUD

    // CADASTRA OS ALUNOS

    @PostMapping("/aluno")
    public ResponseEntity<?> cadastrar(@Valid @RequestBody Aluno aluno) {

        try {
            aluno.setMatricula(null);

            if (repositoryAluno.existsByCpf(aluno.getCpf())) {
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body("Já existe um aluno com esse CPF.");
            }

            if (aluno.getOficinas() != null && !aluno.getOficinas().isEmpty()) {
                ObjectMapper mapper = new ObjectMapper();

                // Converte JSON para Map<String, Boolean>
                Map<String, Boolean> oficinasMap = mapper.readValue(
                        aluno.getOficinas(),
                        new TypeReference<Map<String, Boolean>>() {
                        });

                // Filtra só as selecionadas
                Map<String, Boolean> oficinasSelecionadas = oficinasMap.entrySet().stream()
                        .filter(Map.Entry::getValue) // pega só os true
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

                // Converte de volta para JSON e seta
                aluno.setOficinas(mapper.writeValueAsString(oficinasSelecionadas));
            }

            Aluno resposta = repositoryAluno.save(aluno);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("Aluno criado com sucesso" + "" + resposta.getMatricula());

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao cadastrar aluno " + e.getMessage());
        }
    }

    // LISTA DOS ALUNOS

    @GetMapping("/aluno")
    public List<Aluno> listaAluno() {

        return repositoryAluno.findAll();
    }

    // BUSCA OS ALUNOS PELA MATRICULA

    @GetMapping("/aluno/{matricula}")
    public Aluno listaPelaMatricula(@PathVariable int matricula) {

        return repositoryAluno.findByMatricula(matricula);

    }

    // ATUALIZA OS ALUNOS

    @PutMapping("/aluno/{matricula}")
    public ResponseEntity<?> atualizar(@PathVariable Long matricula, @RequestBody Aluno aluno) {
        try {
            Optional<Aluno> existenteOpt = repositoryAluno.findById(matricula);

            if (existenteOpt.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("Aluno com matrícula " + matricula + " não encontrado.");
            }

            Aluno existente = existenteOpt.get();

            existente.setFoto(aluno.getFoto());
            existente.setOficinas(aluno.getOficinas());
            existente.setNome(aluno.getNome());
            existente.setCpf(aluno.getCpf());
            existente.setRg(aluno.getRg());
            existente.setOrgaoExp(aluno.getOrgaoExp());
            existente.setDataExpedRg(aluno.getDataExpedRg());
            existente.setRn(aluno.getRn());
            existente.setDataNascimento(aluno.getDataNascimento());
            existente.setIdade(aluno.getIdade());
            existente.setEscola(aluno.getEscola());
            existente.setEstado(aluno.getEstado());
            existente.setCidade(aluno.getCidade());
            existente.setBairro(aluno.getBairro());
            existente.setFiliacaoPai(aluno.getFiliacaoPai());
            existente.setFiliacaoMae(aluno.getFiliacaoMae());
            existente.setTelefonePai(aluno.getTelefonePai());
            existente.setTelefoneMae(aluno.getTelefoneMae());
            existente.setResponsavel(aluno.getResponsavel());
            existente.setTelefoneResponsavel(aluno.getTelefoneResponsavel());
            existente.setEmailResponsavel(aluno.getEmailResponsavel());
            existente.setPossuiDoenca(aluno.getPossuiDoenca());
            existente.setQualDoenca(aluno.getQualDoenca());
            existente.setMedicacao(aluno.getMedicacao());
            existente.setTipoSanguineo(aluno.getTipoSanguineo());
            existente.setSerieturma(aluno.getSerieturma());
            existente.setTurnoesc(aluno.getTurnoesc());
            existente.setAutorizacaoImagem(aluno.getAutorizacaoImagem());
            existente.setAtividadesExtras(aluno.getAtividadesExtras());
            existente.setDescricaoAtividadesExtras(aluno.getDescricaoAtividadesExtras());
            existente.setNecessidadesEspeciais(aluno.getNecessidadesEspeciais());
            existente.setDescricaoNecessidadesEspeciais(aluno.getDescricaoNecessidadesEspeciais());

            Aluno atualizado = repositoryAluno.save(existente);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("Aluno atualizado com sucesso! Matrícula: " + atualizado.getMatricula());

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar Aluno: " + e.getMessage());
        }
    }

    // DELETA OS ALUNOS CADASTRADOS

    @DeleteMapping("/aluno/{matricula}")
    public ResponseEntity<?> deletar(@PathVariable Long matricula) {
        try {
            Optional<Aluno> existentOpt = repositoryAluno.findById(matricula);

            if (existenteOpt.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("Aluno com a matricula" + matricula + "não encontrado.");
            }

            repositoryAluno.delete(existentOpt.get());

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("Aluno com matricula" + matricula + " deletado com sucesso.");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao deletar Aluno: " + e.getMessage());

        }
    }

}
