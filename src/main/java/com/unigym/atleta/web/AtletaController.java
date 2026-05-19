package com.unigym.atleta.web;

import com.unigym.atleta.domain.Atleta;
import com.unigym.atleta.service.AtletaService;
import com.unigym.atleta.web.dto.AtletaResponse;
import com.unigym.atleta.web.dto.CadastroAtletaRequest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/atletas")
public class AtletaController {

	private final AtletaService atletaService;
	private final SecurityContextRepository securityContextRepository;

	public AtletaController(AtletaService atletaService, SecurityContextRepository securityContextRepository) {
		this.atletaService = atletaService;
		this.securityContextRepository = securityContextRepository;
	}

	@PostMapping
	public ResponseEntity<AtletaResponse> cadastrar(
			@Valid @RequestBody CadastroAtletaRequest request,
			HttpServletRequest servletRequest,
			HttpServletResponse servletResponse) {
		Atleta atleta = atletaService.cadastrar(request);
		Authentication authentication = atletaService.autenticar(atleta);

		SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
		securityContext.setAuthentication(authentication);
		SecurityContextHolder.setContext(securityContext);
		securityContextRepository.saveContext(securityContext, servletRequest, servletResponse);

		return ResponseEntity.status(HttpStatus.CREATED).body(AtletaResponse.from(atleta));
	}
}
