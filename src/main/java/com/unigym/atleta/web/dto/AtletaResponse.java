package com.unigym.atleta.web.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.unigym.atleta.domain.Atleta;
import com.unigym.atleta.domain.NivelExperiencia;

public record AtletaResponse(
		Long id,
		String nome,
		String email,
		LocalDate dataNascimento,
		BigDecimal peso,
		BigDecimal altura,
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
