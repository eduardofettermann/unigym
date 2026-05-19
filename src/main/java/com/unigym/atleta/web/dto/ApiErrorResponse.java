package com.unigym.atleta.web.dto;

import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta padrão para erros da API.")
public record ApiErrorResponse(
		@Schema(example = "Dados inválidos")
		String message,
		@Schema(example = """
				{"email":"E-mail inválido","senha":"Senha deve ter no mínimo 8 caracteres"}
				""")
		Map<String, String> fieldErrors) {
}
