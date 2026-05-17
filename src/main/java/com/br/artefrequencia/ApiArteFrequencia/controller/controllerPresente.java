package com.br.artefrequencia.ApiArteFrequencia.controller;

import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import com.br.artefrequencia.ApiArteFrequencia.dto.FrequenciaAnual;
import com.br.artefrequencia.ApiArteFrequencia.dto.FrequenciaMes;
import com.br.artefrequencia.ApiArteFrequencia.model.Db1.Aluno;
import com.br.artefrequencia.ApiArteFrequencia.model.Db1.Presenca;
import com.br.artefrequencia.ApiArteFrequencia.repository.Db1.RepositoryAluno;
import com.br.artefrequencia.ApiArteFrequencia.repository.Db1.RepositoryPresenca;
import com.br.artefrequencia.ApiArteFrequencia.dto.AlunoResposta;


@RestController
@RequestMapping("/api/artefrequencia")
public class controllerPresente {

    @Autowired
    private RepositoryPresenca presencaRepository;

    @Autowired
    private RepositoryAluno repositoryAluno;

    @GetMapping("/presenca/aluno/{alunoId}")
    public FrequenciaAnual buscarFrequenciaAnual(
            @PathVariable Long alunoId,
            @RequestParam Integer ano
    ) {

        // BUSCAR ALUNO
        Aluno aluno = repositoryAluno.findById(alunoId)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        LocalDate inicioAno = Year.of(ano).atDay(1);
        LocalDate fimAno = Year.of(ano).atMonth(12).atEndOfMonth();

        List<Presenca> presencas = presencaRepository
                .findByAlunoIdAndTipoAndLidoEmBetween(
                        alunoId,
                        "ENTRADA",
                        inicioAno.atStartOfDay(),
                        fimAno.atTime(23, 59, 59) );

        Map<Integer, List<Integer>> agrupadoPorMes = presencas.stream()
                .collect(Collectors.groupingBy(
                        p -> p.getLidoEm().getMonthValue(),
                        Collectors.mapping(
                                p -> p.getLidoEm().getDayOfMonth(),
                                Collectors.toList() )
                ));

        List<FrequenciaMes> meses = new ArrayList<>();

        for (Map.Entry<Integer, List<Integer>> entry : agrupadoPorMes.entrySet()) {

            FrequenciaMes mesDTO = new FrequenciaMes();

            mesDTO.setMes(entry.getKey());

            List<Integer> diasUnicos = entry.getValue()
                    .stream()
                    .distinct()
                    .sorted()
                    .toList();

            mesDTO.setDias(diasUnicos);

            meses.add(mesDTO);
        }

        meses.sort((a, b) -> a.getMes().compareTo(b.getMes()));

        AlunoResposta alunoDTO = new AlunoResposta();
        alunoDTO.setId(aluno.getId());
        alunoDTO.setMatricula(aluno.getMatricula());
        alunoDTO.setNome(aluno.getNome());
        alunoDTO.setOficinas(aluno.getOficinas());

        FrequenciaAnual resposta = new FrequenciaAnual();
        resposta.setAluno(alunoDTO);
        resposta.setAno(ano);
        resposta.setMeses(meses);

        return resposta;
    }

    @DeleteMapping("/presenca/aluno/{alunoId}")
public String deletarPresencaPorMes(
        @PathVariable Long alunoId,
        @RequestParam Integer mes,
        @RequestParam Integer ano
) {

    LocalDate inicioMes = Year.of(ano)
            .atMonth(mes)
            .atDay(1);

    LocalDate fimMes = inicioMes.withDayOfMonth(
            inicioMes.lengthOfMonth()
    );

    presencaRepository.deleteByAlunoIdAndLidoEmBetween(
            alunoId,
            inicioMes.atStartOfDay(),
            fimMes.atTime(23, 59, 59)
    );

    return "Presenças deletadas com sucesso";
}

}
