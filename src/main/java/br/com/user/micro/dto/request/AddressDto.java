package br.com.user.micro.dto.request;

import br.com.user.micro.domain.enums.UF;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AddressDto(
        @NotNull(message = "The cep is required!") @Size(min = 8, message = "The cep must be valid!") String cep,
        @NotNull(message = "The home number is required!") @Size(min = 1, message = "The home number must be valid!") String number,
        @NotNull(message = "The state uf is required!") UF state,
        @NotNull(message = "The city name is required!") @Size(min = 3, message = "The city name must be valid!") String city,
        @NotNull(message = "The street name is required!") @Size(min = 3, message = "The street name must be valid!") String street,
        String complement
) {
}
