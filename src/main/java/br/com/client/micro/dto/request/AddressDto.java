package br.com.client.micro.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AddressDto(
        @NotNull(message = "The cep is required!") @Size(min = 8, message = "The cep must be valid!") String cep,
        @NotNull(message = "The home number is required!") @Size(min = 1, message = "The home number must be valid!") String number,
        String complement
) {
}
