package com.unigym.atleta.web;

import com.unigym.atleta.service.AtletaService;
import com.unigym.atleta.service.JwtService;
import com.unigym.atleta.web.dto.AuthTokenResponse;
import com.unigym.atleta.web.dto.LoginRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AtletaService atletaService;
    private final JwtService jwtService;

    public AuthController(AtletaService atletaService, JwtService jwtService) {
        this.atletaService = atletaService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthTokenResponse> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = atletaService.autenticar(request.email(), request.senha());
        String token = jwtService.gerarToken(authentication);

        return ResponseEntity.ok(AuthTokenResponse.bearer(token));
    }
}
