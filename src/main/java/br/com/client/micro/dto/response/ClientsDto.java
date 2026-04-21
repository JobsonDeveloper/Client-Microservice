package br.com.client.micro.dto.response;

import br.com.client.micro.domain.Client;
import br.com.client.micro.domain.Role;
import br.com.client.micro.domain.complements.Address;
import br.com.client.micro.domain.complements.Phone;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ClientsDto(
        String id,
        String firstName,
        String lastName,
        String cpf,
        LocalDate birthday,
        String email,
        Phone phone,
        Address address,
        Role role,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public ClientsDto(Client client) {
        this(
                client.getId(),
                client.getFirstName(),
                client.getLastName(),
                client.getCpf(),
                client.getBirthday(),
                client.getEmail(),
                client.getPhone(),
                client.getAddress(),
                client.getRole(),
                client.getCreatedAt(),
                client.getUpdatedAt()
        );
    }
}
