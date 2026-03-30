package br.com.client.micro.domain;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Phone {
    private String countryCode;
    private String cityCode;
    private String number;
}
