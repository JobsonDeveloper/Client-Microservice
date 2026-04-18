package br.com.client.micro.dto.swagger;

public record DefaultErrorResponseDto(
        String status,
        String message
) {
}
