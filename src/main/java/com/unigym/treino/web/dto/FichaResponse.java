package com.unigym.treino.web.dto;

import java.util.List;

public record FichaResponse(
        Long id,
        String nome,
        boolean ativa,
        boolean treinoConcluido,
        List<ExercicioNaFichaResponse> exercicios
) {
}
