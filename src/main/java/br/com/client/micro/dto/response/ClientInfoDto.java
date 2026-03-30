package br.com.client.micro.dto.response;

import br.com.client.micro.domain.Client;

public record ClientInfoDto(String message, Client client) {
}
