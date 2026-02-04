package br.com.client.micro.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "Client")
public record ReturnAllClientsDto(
        String id,
        String firstName,
        String lastName,
        Long cpf,
        LocalDate birthday,
        String email,
        String phone,
        String address,
        LocalDateTime createdAt
) {
}
