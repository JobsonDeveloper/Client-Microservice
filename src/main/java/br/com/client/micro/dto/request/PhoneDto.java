package br.com.client.micro.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PhoneDto(
        @NotNull(message = "The phone's country code is required!") @Size(min = 2, message = "The phone's country code must be valid!") String countryCode,
        @NotNull(message = "The phone's city code is required!") @Size(min = 2, message = "The phone's city code must be valid!") String cityCode,
        @NotNull(message = "The phone number is required!") @Size(min = 8, message = "The phone number must be valid!") String number
) {
}
