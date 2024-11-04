package com.claro.proceso.migracion.pospago.api.models.responses;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorResponse {
    private final boolean hasError;
    private final int statusCode;
    private final String message;
}
