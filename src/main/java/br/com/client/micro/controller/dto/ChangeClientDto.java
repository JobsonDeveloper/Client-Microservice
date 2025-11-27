package br.com.client.micro.controller.dto;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record ChangeClientDto(
        @NotNull(message = "The User ID is required!") @Size(min = 1, message = "The User ID must be valid!") String id,
        @NotNull(message = "The first name is required!") @Size(min = 1, message = "The first name must be valid!") String firstName,
        @NotNull(message = "The last name is required!") @Size(min = 1, message = "The last name must be valid!") String lastName,
        @NotNull(message = "The birthday is required!") @Past(message = "The birthday must be valid!") LocalDate birthday,
        @NotNull(message = "The email is required!") @Email(message = "The email must be valid!") String email,
        @NotNull(message = "The phone number is required!") @Pattern(regexp = "^[0-9]{10,13}$", message = "The Phone number must be valid!") String phone,
        @NotNull(message = "The address is required!") @Size(min = 5, message = "The address must be valid!") String address
) {
}
