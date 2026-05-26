package com.unigym.treino.repository;

import com.unigym.treino.domain.SerieFicha;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SerieFichaRepository extends JpaRepository<SerieFicha, Long> {
    Optional<SerieFicha> findByIdAndFichaTreinoId(Long id, Long fichaTreinoId);
}
