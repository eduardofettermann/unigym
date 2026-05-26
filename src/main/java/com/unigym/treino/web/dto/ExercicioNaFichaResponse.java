package com.unigym.treino.web.dto;

import java.util.List;

public record ExercicioNaFichaResponse(
        Long exercicioId,
        String nome,
        boolean concluido,
        List<SerieFichaResponse> series
) {
}
