package br.com.client.micro.controller.dto;

import br.com.client.micro.domain.Client;

public record ReturnClientInformationDto(String message, Client client) {
}
