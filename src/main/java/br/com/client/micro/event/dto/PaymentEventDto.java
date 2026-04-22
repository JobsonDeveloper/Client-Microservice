package br.com.client.micro.event.dto;

import br.com.client.micro.domain.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PaymentEventDto(
        String saleId,
        Status status,
        String clientId
) {
}
