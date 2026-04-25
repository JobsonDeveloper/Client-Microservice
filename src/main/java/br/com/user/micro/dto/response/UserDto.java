package br.com.user.micro.dto.response;

import br.com.user.micro.domain.User;
import br.com.user.micro.domain.Role;
import br.com.user.micro.domain.complements.Address;
import br.com.user.micro.domain.complements.Phone;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UserDto(
        String id,
        String firstName,
        String lastName,
        String cpf,
        LocalDate birthday,
        String email,
        Phone phone,
        Address address,
        String role,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public UserDto(User user) {
        this(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getCpf(),
                user.getBirthday(),
                user.getEmail(),
                user.getPhone(),
                user.getAddress(),
                user.getRole(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
