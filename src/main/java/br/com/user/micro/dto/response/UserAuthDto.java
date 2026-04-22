package br.com.user.micro.dto.response;

public record UserAuthDto(String message, UserDto user, String accessToken) {
}
