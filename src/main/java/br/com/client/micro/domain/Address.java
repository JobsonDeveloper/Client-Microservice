package br.com.client.micro.domain;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Address {
    private String cep;
    private String number;
    private String complement;
}
