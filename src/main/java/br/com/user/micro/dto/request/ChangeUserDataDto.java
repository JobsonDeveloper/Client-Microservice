package br.com.user.micro.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record ChangeUserDataDto(
        @NotNull(message = "The User ID is required!") @Size(min = 1, message = "The User ID must be valid!") String id,
        @NotNull(message = "The First Name is required!") @Size(min = 1, message = "The Fist Name must be valid!") String firstName,
        @NotNull(message = "The Last Name is required!") @Size(min = 1, message = "The Last Name bust be valid!") String lastName,
        @NotNull(message = "The Birthday is required!") @Past(message = "The birthday must be valid!") LocalDate birthday,
        @NotNull(message = "The Phone number is required!") @Valid PhoneDto phone,
        @NotNull(message = "The Address is required!") @Valid AddressDto address
) {
}
