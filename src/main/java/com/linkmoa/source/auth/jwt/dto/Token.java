package com.linkmoa.source.auth.jwt.dto;

import jakarta.validation.constraints.NotNull;

public record Token(
        @NotNull String grantType,
        @NotNull TokenType tokenType,
        @NotNull String value
) {
}
