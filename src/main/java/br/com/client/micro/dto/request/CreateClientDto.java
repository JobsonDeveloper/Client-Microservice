package br.com.client.micro.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record CreateClientDto(
        @NotNull(message = "The First Name is required!") @Size(min = 1, message = "The Fist Name must be valid!") String firstName,
        @NotNull(message = "The Last Name is required!") @Size(min = 1, message = "The Last Name bust be valid!") String lastName,
        @NotNull(message = "The CPF is required!") @CPF(message = "The CPF must be valid!") String cpf,
        @NotNull(message = "The Birthday is required!") @Past(message = "The birthday must be valid!") LocalDate birthday,
        @NotNull(message = "The Email is required!") @Email(message = "Invalid email!") String email,
        @NotNull(message = "The Phone number is required!") @Valid PhoneDto phone,
        @NotNull(message = "The Address is required!") @Valid AddressDto address,
        @NotBlank(message = "The user password is required!")
        @Size(min = 8, message = "The user password must be at least 8 characters!")
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).+$", message = "The user password must include upper, lower, number, and special character!")
        String password,
        @NotBlank(message = "The user confirmation password is required!")
        @Size(min = 8, message = "The user confirmation password must be at least 8 characters!")
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).+$", message = "The user confirmation password must include upper, lower, number, and special character!")
        String confirmationPassword
) {
}
