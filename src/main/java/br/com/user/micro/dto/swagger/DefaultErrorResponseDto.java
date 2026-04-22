package br.com.user.micro.dto.swagger;

public record DefaultErrorResponseDto(
        String status,
        String message
) {
}
