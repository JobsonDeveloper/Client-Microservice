package br.com.user.micro.service;

import br.com.user.micro.dto.response.PaymentInfoDto;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "payment-microservice",
        url = "${payment.micro.url}"
)
public interface IPayment {
    @GetMapping("/api/payment/{saleId}/info")
    PaymentInfoDto getPaymentInfo(
            @Parameter(description = "Id of the sale", required = true)
            @PathVariable String saleId
    );
}
