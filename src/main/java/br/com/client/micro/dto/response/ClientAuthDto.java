package br.com.client.micro.dto.response;

public record ClientAuthDto(String message, ClientDto client, String accessToken) {
}
