package com.unigym.treino.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record SerieRequest(
        @NotNull(message = "Exercício é obrigatório")
        Long exercicioId,

        @NotNull(message = "Repetições é obrigatório")
        @Positive(message = "Repetições deve ser maior que zero")
        Integer repeticoes,

        @NotNull(message = "Carga sugerida é obrigatória")
        @DecimalMin(value = "0.0", inclusive = true, message = "Carga sugerida deve ser maior ou igual a zero")
        BigDecimal cargaSugerida,

        @NotNull(message = "Ordem é obrigatória")
        @Positive(message = "Ordem deve ser maior que zero")
        Integer ordem
) {
}
