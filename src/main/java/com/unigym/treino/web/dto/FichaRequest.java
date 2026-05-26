package com.unigym.treino.web.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record FichaRequest(
        @NotBlank(message = "Nome da ficha é obrigatório")
        String nome,

        @NotEmpty(message = "Ficha deve conter ao menos uma série")
        List<@Valid SerieRequest> series
) {
}
