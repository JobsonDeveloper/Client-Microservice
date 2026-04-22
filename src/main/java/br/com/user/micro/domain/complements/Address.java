package br.com.user.micro.domain.complements;

import br.com.user.micro.domain.enums.UF;
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
