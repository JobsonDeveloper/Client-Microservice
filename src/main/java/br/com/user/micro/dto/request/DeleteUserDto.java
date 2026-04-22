package br.com.user.micro.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DeleteUserDto(
        @NotNull(message = "The User ID is required!") @Size(min = 1, message = "The user id must be valid!") String id
) {
}
