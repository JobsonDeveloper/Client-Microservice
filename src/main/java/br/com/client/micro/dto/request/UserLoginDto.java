package br.com.client.micro.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UserLoginDto(
        @NotNull(message = "The Email is required!") @Email(message = "Invalid email!") String email,
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).+$", message = "The user password must include upper, lower, number, and special character!")
        String password
) {
}
