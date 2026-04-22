package br.com.user.micro.event.dto;

import br.com.user.micro.domain.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PaymentEventDto(
        String saleId,
        Status status,
        String userId
) {
}
