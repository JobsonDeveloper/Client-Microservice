package br.com.client.micro.dto.response;

import jakarta.validation.constraints.NotNull;

public record PaymentDto(
        @NotNull(message = "The payment id is required!") String id
) {
}
