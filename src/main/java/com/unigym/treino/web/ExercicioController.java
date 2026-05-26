package com.unigym.treino.web;

import com.unigym.treino.repository.ExercicioRepository;
import com.unigym.treino.web.dto.ExercicioResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/exercicios")
public class ExercicioController {

    private final ExercicioRepository exercicioRepository;

    public ExercicioController(ExercicioRepository exercicioRepository) {
        this.exercicioRepository = exercicioRepository;
    }

    @GetMapping
    public ResponseEntity<List<ExercicioResponse>> listar() {
        return ResponseEntity.ok(exercicioRepository.findAllByOrderByNomeAsc().stream().map(ExercicioResponse::from).toList());
    }
}
