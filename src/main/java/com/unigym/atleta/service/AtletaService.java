package com.unigym.atleta.service;

import java.util.Collections;
import java.util.Locale;

import com.unigym.atleta.domain.Atleta;
import com.unigym.atleta.repository.AtletaRepository;
import com.unigym.atleta.web.dto.CadastroAtletaRequest;
import com.unigym.atleta.web.exception.EmailJaCadastradoException;
import com.unigym.atleta.web.exception.SenhaFracaException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class AtletaService {

	private final AtletaRepository atletaRepository;
	private final PasswordEncoder passwordEncoder;

	public AtletaService(AtletaRepository atletaRepository, PasswordEncoder passwordEncoder) {
		this.atletaRepository = atletaRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional
	public Atleta cadastrar(CadastroAtletaRequest request) {
		String emailNormalizado = normalizarEmail(request.email());

		if (atletaRepository.findByEmailIgnoreCase(emailNormalizado).isPresent()) {
			throw new EmailJaCadastradoException(emailNormalizado);
		}

		validarSenha(request.senha());

		Atleta atleta = new Atleta();
		atleta.setNome(request.nome().trim());
		atleta.setEmail(emailNormalizado);
		atleta.setSenha(passwordEncoder.encode(request.senha()));
		atleta.setDataNascimento(request.dataNascimento());
		atleta.setPeso(request.peso());
		atleta.setAltura(request.altura());
		atleta.setNivelExperiencia(request.nivelExperiencia());

		return atletaRepository.save(atleta);
	}

	public Authentication autenticar(Atleta atleta) {
		return new UsernamePasswordAuthenticationToken(
				atleta.getEmail(),
				null,
				Collections.emptyList());
	}

	private static String normalizarEmail(String email) {
		return email.trim().toLowerCase(Locale.ROOT);
	}

	private static void validarSenha(String senha) {
		if (!StringUtils.hasText(senha) || senha.length() < 8) {
			throw new SenhaFracaException();
		}
	}
}
