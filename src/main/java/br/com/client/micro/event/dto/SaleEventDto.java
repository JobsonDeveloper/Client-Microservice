package br.com.client.micro.event.dto;

import br.com.client.micro.domain.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SaleEventDto(
        String id,
        String clientId,
        Status status
) {
}
