package com.unigym.atleta.service;

import com.unigym.atleta.domain.Atleta;
import com.unigym.atleta.repository.AtletaRepository;
import com.unigym.atleta.web.dto.CadastroAtletaRequest;
import com.unigym.atleta.web.exception.EmailJaCadastradoException;
import com.unigym.atleta.web.exception.SenhaFracaException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        if (request.senha() == null || request.senha().length() < 8) {
            throw new SenhaFracaException("Senha deve ter no mínimo 8 caracteres");
        }

        if (atletaRepository.existsByEmail(request.email())) {
            throw new EmailJaCadastradoException(request.email());
        }

        Atleta atleta = new Atleta();
        atleta.setNome(request.nome());
        atleta.setEmail(request.email());
        atleta.setSenha(passwordEncoder.encode(request.senha()));
        atleta.setDataNascimento(request.dataNascimento());
        atleta.setPeso(request.peso());
        atleta.setAltura(request.altura());
        atleta.setNivelExperiencia(request.nivelExperiencia());
        return atletaRepository.save(atleta);
    }

    public Authentication autenticar(Atleta atleta) {
        return UsernamePasswordAuthenticationToken.authenticated(
                atleta.getEmail(),
                null,
                AuthorityUtils.NO_AUTHORITIES);
    }
}
