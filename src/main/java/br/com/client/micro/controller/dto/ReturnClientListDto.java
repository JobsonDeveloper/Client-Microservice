package br.com.client.micro.controller.dto;

import java.util.ArrayList;

public record ReturnClientListDto(
        ArrayList<ReturnAllClientsDto> content,
        int page,
        int size,
        int totalElements,
        int totalPages
) {
}
