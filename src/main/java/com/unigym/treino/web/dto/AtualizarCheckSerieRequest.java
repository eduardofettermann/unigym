package com.unigym.treino.web.dto;

import jakarta.validation.constraints.NotNull;

public record AtualizarCheckSerieRequest(
        @NotNull(message = "Campo concluida é obrigatório")
        Boolean concluida
) {
}
