package com.unigym.treino.web.dto;

import java.math.BigDecimal;

public record SerieFichaResponse(
        Long id,
        Integer repeticoes,
        BigDecimal cargaSugerida,
        Integer ordem,
        boolean concluida
) {
}
