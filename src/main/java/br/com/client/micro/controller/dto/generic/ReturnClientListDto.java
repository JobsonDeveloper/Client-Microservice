package br.com.client.micro.controller.dto.generic;

import br.com.client.micro.controller.dto.ReturnAllClientsDto;

import java.util.ArrayList;
import java.util.Map;

public record ReturnClientListDto(
        ArrayList<ReturnAllClientsDto> content,
        int page,
        int size,
        int totalElements,
        int totalPages
) {
}
