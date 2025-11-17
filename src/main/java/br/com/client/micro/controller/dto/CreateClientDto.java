package br.com.client.micro.controller.dto;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record CreateClientDto(
        @NotBlank(message = "The First Name is required") @Size(min = 1, message = "The Fist Name must be valid!") String firstName,
        @NotBlank(message = "The Last Name is required!") @Size(min = 1, message = "The Last Name bust be valid!") String lastName,
        @NotBlank(message = "The CPF is required!") @CPF(message = "The CPF must be valid!") String cpf,
        @NotNull(message = "The Birthday is required!") @Past(message = "The birthday must be valid!") LocalDate birthday,
        @NotBlank(message = "The Email is required!") @Email(message = "Invalid email!") String email,
        @NotBlank(message = "The Phone number is required") @Pattern(regexp = "^[0-9]{10,13}$", message = "The Phone number must be valid!") String phone,
        @NotBlank(message = "The Address is required") @Size(min = 5, message = "The Address must be valid!") String address
) {
}
