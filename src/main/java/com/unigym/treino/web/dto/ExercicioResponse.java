package com.unigym.treino.web.dto;

import com.unigym.treino.domain.Exercicio;

public record ExercicioResponse(Long id, String nome) {
    public static ExercicioResponse from(Exercicio exercicio) {
        return new ExercicioResponse(exercicio.getId(), exercicio.getNome());
    }
}
