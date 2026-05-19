package com.unigym.atleta.web.dto;

import java.util.Map;

public record ApiErrorResponse(
		String message,
		Map<String, String> fieldErrors) {
}
