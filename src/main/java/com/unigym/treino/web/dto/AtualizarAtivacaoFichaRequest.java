package com.unigym.treino.web.dto;

import jakarta.validation.constraints.NotNull;

public record AtualizarAtivacaoFichaRequest(
        @NotNull(message = "Campo ativa é obrigatório")
        Boolean ativa
) {
}
