package com.unigym.treino.service;

import com.unigym.atleta.domain.Atleta;
import com.unigym.atleta.repository.AtletaRepository;
import com.unigym.treino.domain.Exercicio;
import com.unigym.treino.domain.FichaTreino;
import com.unigym.treino.domain.SerieFicha;
import com.unigym.treino.repository.ExercicioRepository;
import com.unigym.treino.repository.FichaTreinoRepository;
import com.unigym.treino.repository.SerieFichaRepository;
import com.unigym.treino.web.dto.ExercicioNaFichaResponse;
import com.unigym.treino.web.dto.FichaRequest;
import com.unigym.treino.web.dto.FichaResponse;
import com.unigym.treino.web.dto.SerieFichaResponse;
import com.unigym.treino.web.dto.SerieRequest;
import com.unigym.treino.web.exception.CampoInvalidoException;
import com.unigym.treino.web.exception.RecursoNaoEncontradoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FichaTreinoService {

    private final FichaTreinoRepository fichaTreinoRepository;
    private final SerieFichaRepository serieFichaRepository;
    private final ExercicioRepository exercicioRepository;
    private final AtletaRepository atletaRepository;

    public FichaTreinoService(
            FichaTreinoRepository fichaTreinoRepository,
            SerieFichaRepository serieFichaRepository,
            ExercicioRepository exercicioRepository,
            AtletaRepository atletaRepository
    ) {
        this.fichaTreinoRepository = fichaTreinoRepository;
        this.serieFichaRepository = serieFichaRepository;
        this.exercicioRepository = exercicioRepository;
        this.atletaRepository = atletaRepository;
    }

    @Transactional
    public FichaResponse criar(String emailAtleta, FichaRequest request) {
        Atleta atleta = buscarAtletaPorEmail(emailAtleta);
        Map<Long, Exercicio> exercicios = carregarExercicios(request.series());

        FichaTreino ficha = new FichaTreino();
        ficha.setNome(request.nome());
        ficha.setAtiva(true);
        ficha.setAtleta(atleta);
        ficha.setSeries(montarSeries(request.series(), exercicios, ficha));

        return toResponse(fichaTreinoRepository.save(ficha));
    }

    @Transactional(readOnly = true)
    public List<FichaResponse> listar(String emailAtleta, boolean incluirInativas) {
        Atleta atleta = buscarAtletaPorEmail(emailAtleta);
        List<FichaTreino> fichas = incluirInativas
                ? fichaTreinoRepository.findByAtletaIdOrderByIdAsc(atleta.getId())
                : fichaTreinoRepository.findByAtletaIdAndAtivaTrueOrderByIdAsc(atleta.getId());
        return fichas.stream().map(this::toResponse).toList();
    }

    @Transactional
    public FichaResponse atualizar(String emailAtleta, Long fichaId, FichaRequest request) {
        Atleta atleta = buscarAtletaPorEmail(emailAtleta);
        FichaTreino ficha = buscarFichaDoAtleta(fichaId, atleta.getId());
        Map<Long, Exercicio> exercicios = carregarExercicios(request.series());

        ficha.setNome(request.nome());
        ficha.getSeries().clear();
        ficha.getSeries().addAll(montarSeries(request.series(), exercicios, ficha));

        return toResponse(fichaTreinoRepository.save(ficha));
    }

    @Transactional
    public FichaResponse atualizarAtivacao(String emailAtleta, Long fichaId, boolean ativa) {
        Atleta atleta = buscarAtletaPorEmail(emailAtleta);
        FichaTreino ficha = buscarFichaDoAtleta(fichaId, atleta.getId());
        ficha.setAtiva(ativa);
        return toResponse(fichaTreinoRepository.save(ficha));
    }

    @Transactional
    public FichaResponse atualizarCheckSerie(String emailAtleta, Long fichaId, Long serieId, boolean concluida) {
        Atleta atleta = buscarAtletaPorEmail(emailAtleta);
        FichaTreino ficha = buscarFichaDoAtleta(fichaId, atleta.getId());

        SerieFicha serie = serieFichaRepository.findByIdAndFichaTreinoId(serieId, ficha.getId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Série não encontrada"));

        serie.setConcluida(concluida);
        serieFichaRepository.save(serie);

        return toResponse(buscarFichaDoAtleta(fichaId, atleta.getId()));
    }

    private Atleta buscarAtletaPorEmail(String email) {
        return atletaRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Atleta não encontrado"));
    }

    private FichaTreino buscarFichaDoAtleta(Long fichaId, Long atletaId) {
        return fichaTreinoRepository.findByIdAndAtletaId(fichaId, atletaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Ficha de treino não encontrada"));
    }

    private Map<Long, Exercicio> carregarExercicios(List<SerieRequest> seriesRequest) {
        List<Long> ids = seriesRequest.stream().map(SerieRequest::exercicioId).distinct().toList();
        List<Exercicio> exercicios = exercicioRepository.findAllById(ids);
        Map<Long, Exercicio> map = exercicios.stream().collect(Collectors.toMap(Exercicio::getId, e -> e));

        if (map.size() != ids.size()) {
            throw new CampoInvalidoException(
                    "Dados inválidos",
                    Map.of("series", "Uma ou mais séries referenciam exercício inexistente")
            );
        }

        return map;
    }

    private List<SerieFicha> montarSeries(List<SerieRequest> seriesRequest, Map<Long, Exercicio> exercicios, FichaTreino ficha) {
        List<SerieFicha> series = new ArrayList<>();
        for (SerieRequest serieRequest : seriesRequest) {
            SerieFicha serie = new SerieFicha();
            serie.setFichaTreino(ficha);
            serie.setExercicio(exercicios.get(serieRequest.exercicioId()));
            serie.setRepeticoes(serieRequest.repeticoes());
            serie.setCargaSugerida(serieRequest.cargaSugerida());
            serie.setOrdem(serieRequest.ordem());
            serie.setConcluida(false);
            series.add(serie);
        }
        return series;
    }

    private FichaResponse toResponse(FichaTreino ficha) {
        List<SerieFicha> seriesOrdenadas = ficha.getSeries().stream()
                .sorted(Comparator.comparing(SerieFicha::getOrdem))
                .toList();

        Map<Long, List<SerieFicha>> porExercicio = seriesOrdenadas.stream()
                .collect(Collectors.groupingBy(s -> s.getExercicio().getId(), LinkedHashMap::new, Collectors.toList()));

        List<ExercicioNaFichaResponse> exercicios = porExercicio.values().stream()
                .map(seriesDoExercicio -> {
                    Exercicio exercicio = seriesDoExercicio.getFirst().getExercicio();
                    boolean exercicioConcluido = seriesDoExercicio.stream().allMatch(SerieFicha::isConcluida);
                    List<SerieFichaResponse> seriesResponse = seriesDoExercicio.stream()
                            .map(serie -> new SerieFichaResponse(
                                    serie.getId(),
                                    serie.getRepeticoes(),
                                    serie.getCargaSugerida(),
                                    serie.getOrdem(),
                                    serie.isConcluida()
                            ))
                            .toList();
                    return new ExercicioNaFichaResponse(exercicio.getId(), exercicio.getNome(), exercicioConcluido, seriesResponse);
                })
                .toList();

        boolean treinoConcluido = !seriesOrdenadas.isEmpty() && seriesOrdenadas.stream().allMatch(SerieFicha::isConcluida);

        return new FichaResponse(
                ficha.getId(),
                ficha.getNome(),
                ficha.isAtiva(),
                treinoConcluido,
                exercicios
        );
    }
}
