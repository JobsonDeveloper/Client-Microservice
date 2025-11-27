package br.com.client.micro.controller.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
