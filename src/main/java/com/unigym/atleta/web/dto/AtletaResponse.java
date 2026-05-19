package com.unigym.atleta.web.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.unigym.atleta.domain.Atleta;
import com.unigym.atleta.domain.NivelExperiencia;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representação pública do atleta cadastrado.")
public record AtletaResponse(
		@Schema(example = "1")
		Long id,
		@Schema(example = "Ana Silva")
		String nome,
		@Schema(example = "ana@exemplo.com")
		String email,
		@Schema(example = "1995-04-12")
		LocalDate dataNascimento,
		@Schema(example = "62.5")
		BigDecimal peso,
		@Schema(example = "1.70")
		BigDecimal altura,
		@Schema(example = "INTERMEDIARIO")
		NivelExperiencia nivelExperiencia) {

	public static AtletaResponse from(Atleta atleta) {
		return new AtletaResponse(
				atleta.getId(),
				atleta.getNome(),
				atleta.getEmail(),
				atleta.getDataNascimento(),
				atleta.getPeso(),
				atleta.getAltura(),
				atleta.getNivelExperiencia());
	}
}
