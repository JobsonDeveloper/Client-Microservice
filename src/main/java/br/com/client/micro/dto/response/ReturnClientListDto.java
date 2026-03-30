package br.com.client.micro.dto.response;

import br.com.client.micro.domain.Client;

import java.util.ArrayList;

public record ReturnClientListDto(
        ArrayList<Client> content,
        int page,
        int size,
        int totalElements,
        int totalPages
) {
}
