package com.unigym.atleta.web.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.unigym.atleta.domain.NivelExperiencia;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

public record CadastroAtletaRequest(
		@NotBlank(message = "Nome é obrigatório")
		@Schema(example = "Ana Silva")
		String nome,
		@NotBlank(message = "E-mail é obrigatório") @Email(message = "E-mail inválido")
		@Schema(example = "ana@exemplo.com")
		String email,
		@NotBlank(message = "Senha é obrigatória") @Size(min = 8, message = "Senha deve ter no mínimo 8 caracteres")
		@Schema(example = "senhaSegura")
		String senha,
		@NotNull(message = "Data de nascimento é obrigatória")
		@Schema(example = "1995-04-12")
		LocalDate dataNascimento,
		@NotNull(message = "Peso é obrigatório") @Positive(message = "Peso deve ser maior que zero")
		@Schema(example = "62.5")
		BigDecimal peso,
		@NotNull(message = "Altura é obrigatória") @Positive(message = "Altura deve ser maior que zero")
		@Schema(example = "1.70")
		BigDecimal altura,
		@NotNull(message = "Nível de experiência é obrigatório")
		@Schema(example = "INTERMEDIARIO")
		NivelExperiencia nivelExperiencia) {
}
