package com.unigym.atleta.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import com.unigym.atleta.domain.Atleta;
import com.unigym.atleta.domain.NivelExperiencia;
import com.unigym.atleta.repository.AtletaRepository;
import com.unigym.atleta.web.dto.CadastroAtletaRequest;
import com.unigym.atleta.web.exception.EmailJaCadastradoException;
import com.unigym.atleta.web.exception.SenhaFracaException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AtletaServiceTest {

	@Mock
	private AtletaRepository atletaRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	private AtletaService atletaService;

	@BeforeEach
	void setUp() {
		atletaService = new AtletaService(atletaRepository, passwordEncoder);
	}

	@Test
	void deveCadastrarAtletaComSucesso() {
		CadastroAtletaRequest request = new CadastroAtletaRequest(
				"Ana Silva",
				"ana@exemplo.com",
				"senhaSegura",
				LocalDate.of(1995, 4, 12),
				new BigDecimal("62.5"),
				new BigDecimal("1.70"),
				NivelExperiencia.INTERMEDIARIO);

		when(atletaRepository.findByEmailIgnoreCase("ana@exemplo.com")).thenReturn(Optional.empty());
		when(passwordEncoder.encode("senhaSegura")).thenReturn("hash-da-senha");
		when(atletaRepository.save(any(Atleta.class))).thenAnswer(invocation -> {
			return invocation.getArgument(0);
		});

		Atleta atleta = atletaService.cadastrar(request);

		assertThat(atleta.getNome()).isEqualTo("Ana Silva");
		assertThat(atleta.getEmail()).isEqualTo("ana@exemplo.com");
		assertThat(atleta.getSenha()).isEqualTo("hash-da-senha");
		assertThat(atleta.getNivelExperiencia()).isEqualTo(NivelExperiencia.INTERMEDIARIO);

		ArgumentCaptor<Atleta> captor = ArgumentCaptor.forClass(Atleta.class);
		verify(atletaRepository).save(captor.capture());
		assertThat(captor.getValue().getSenha()).isEqualTo("hash-da-senha");
	}

	@Test
	void deveRejeitarEmailDuplicado() {
		CadastroAtletaRequest request = new CadastroAtletaRequest(
				"Ana Silva",
				"ana@exemplo.com",
				"senhaSegura",
				LocalDate.of(1995, 4, 12),
				new BigDecimal("62.5"),
				new BigDecimal("1.70"),
				NivelExperiencia.INICIANTE);

		when(atletaRepository.findByEmailIgnoreCase("ana@exemplo.com")).thenReturn(Optional.of(new Atleta()));

		assertThatThrownBy(() -> atletaService.cadastrar(request))
				.isInstanceOf(EmailJaCadastradoException.class)
				.hasMessageContaining("ana@exemplo.com");
	}

	@Test
	void deveRejeitarSenhaFraca() {
		CadastroAtletaRequest request = new CadastroAtletaRequest(
				"Ana Silva",
				"ana@exemplo.com",
				"1234567",
				LocalDate.of(1995, 4, 12),
				new BigDecimal("62.5"),
				new BigDecimal("1.70"),
				NivelExperiencia.AVANCADO);

		when(atletaRepository.findByEmailIgnoreCase("ana@exemplo.com")).thenReturn(Optional.empty());

		assertThatThrownBy(() -> atletaService.cadastrar(request))
				.isInstanceOf(SenhaFracaException.class);
	}
}
