package com.unigym.atleta.web.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.unigym.atleta.domain.NivelExperiencia;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CadastroAtletaRequest(
		@NotBlank(message = "Nome é obrigatório") String nome,
		@NotBlank(message = "E-mail é obrigatório") @Email(message = "E-mail inválido") String email,
		@NotBlank(message = "Senha é obrigatória") @Size(min = 8, message = "Senha deve ter no mínimo 8 caracteres") String senha,
		@NotNull(message = "Data de nascimento é obrigatória") LocalDate dataNascimento,
		@NotNull(message = "Peso é obrigatório") @Positive(message = "Peso deve ser maior que zero") BigDecimal peso,
		@NotNull(message = "Altura é obrigatória") @Positive(message = "Altura deve ser maior que zero") BigDecimal altura,
		@NotNull(message = "Nível de experiência é obrigatório") NivelExperiencia nivelExperiencia) {
}
