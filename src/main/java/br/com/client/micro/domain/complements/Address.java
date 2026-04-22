package br.com.client.micro.domain.complements;

import br.com.client.micro.domain.enums.UF;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Address {
    private String cep;
    private UF state;
    private String city;
    private String street;
    private String number;
    private String complement;
}
