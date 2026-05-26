package com.unigym.treino.repository;

import com.unigym.treino.domain.FichaTreino;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FichaTreinoRepository extends JpaRepository<FichaTreino, Long> {

    @EntityGraph(attributePaths = {"series", "series.exercicio"})
    List<FichaTreino> findByAtletaIdAndAtivaTrueOrderByIdAsc(Long atletaId);

    @EntityGraph(attributePaths = {"series", "series.exercicio"})
    List<FichaTreino> findByAtletaIdOrderByIdAsc(Long atletaId);

    @EntityGraph(attributePaths = {"series", "series.exercicio"})
    Optional<FichaTreino> findByIdAndAtletaId(Long id, Long atletaId);
}
