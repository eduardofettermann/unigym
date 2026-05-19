package com.unigym.atleta.domain;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Nível de experiência do atleta.")
public enum NivelExperiencia {
	INICIANTE,
	INTERMEDIARIO,
	AVANCADO
}
