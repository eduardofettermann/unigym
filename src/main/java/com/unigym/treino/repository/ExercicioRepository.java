package com.unigym.treino.repository;

import com.unigym.treino.domain.Exercicio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExercicioRepository extends JpaRepository<Exercicio, Long> {
    List<Exercicio> findAllByOrderByNomeAsc();
}
