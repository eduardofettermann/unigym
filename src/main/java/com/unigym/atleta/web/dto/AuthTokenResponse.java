package com.unigym.atleta.web.dto;

public record AuthTokenResponse(String token, String tipo) {

    public static AuthTokenResponse bearer(String token) {
        return new AuthTokenResponse(token, "Bearer");
    }
}
