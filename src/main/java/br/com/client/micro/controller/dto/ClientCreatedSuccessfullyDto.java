package br.com.client.micro.controller.dto;

import br.com.client.micro.domain.Client;

public record ClientCreatedSuccessfullyDto(String message, Client client) {
}
