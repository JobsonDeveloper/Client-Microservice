package br.com.user.micro.dto.response;

import jakarta.validation.Valid;

public record PaymentInfoDto(
        String message,
        @Valid PaymentDto payment
) {
}
