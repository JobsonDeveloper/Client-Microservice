package br.com.client.micro.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

public record DeleteClientDto(
        @NotNull(message = "The User ID is required!") @Size(min = 1, message = "The user id must be valid!") String id,
        @NotNull(message = "The CPF is required!") @CPF(message = "The CPF must be valid!") String cpf
) {
}
