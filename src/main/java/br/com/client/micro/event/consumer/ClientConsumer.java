package br.com.client.micro.event.consumer;

import br.com.client.micro.domain.Client;
import br.com.client.micro.domain.Status;
import br.com.client.micro.dto.response.PaymentInfoDto;
import br.com.client.micro.event.dto.DeliveryEventDto;
import br.com.client.micro.event.dto.PaymentEventDto;
import br.com.client.micro.event.dto.SaleEventDto;
import br.com.client.micro.exceptions.ClientNotFoundException;
import br.com.client.micro.exceptions.ErrorGettingPaymentInfoException;
import br.com.client.micro.repository.IClientRepository;
import br.com.client.micro.service.IClientPayment;
import br.com.client.micro.service.IEmailService;
import br.com.client.micro.service.IHtmlMessageTemplateService;
import feign.FeignException.FeignClientException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ClientConsumer {
    private final IEmailService iEmailService;
    private final IClientRepository iClientRepository;
    private final IHtmlMessageTemplateService iHtmlMessageTemplateService;
    private final IClientPayment iClientPayment;

    public ClientConsumer(
            IEmailService iEmailService,
            IClientRepository iClientRepository,
            IHtmlMessageTemplateService iHtmlMessageTemplateService, IClientPayment iClientPayment
    ) {
        this.iEmailService = iEmailService;
        this.iClientRepository = iClientRepository;
        this.iHtmlMessageTemplateService = iHtmlMessageTemplateService;
        this.iClientPayment = iClientPayment;
    }

    @KafkaListener(
            topics = "payment",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "paymentKafkaListenerFactory"
    )
    public void saleListener(PaymentEventDto event) {
        String clientId = event.clientId();
        String saleId = event.saleId();
        Status status = event.status();

        if (!status.equals(Status.PAID)) return;

        Client client = iClientRepository.findById(clientId).orElseThrow(ClientNotFoundException::new);
        String clientName = client.getFirstName() + " " + client.getLastName();
        String message = iHtmlMessageTemplateService.paymentProcessedTemplate(clientName);

        iEmailService.sendMessage(
                client.getEmail(),
                "E-Sale: Pagamento processado",
                message
        );
    }

    @KafkaListener(
            topics = "delivery",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "deliveryKafkaListenerFactory"
    )
    public void deliveryListener(DeliveryEventDto event) {
        String saleId = event.saleId();
        String clientId = event.clientId();
        Status status = event.status();

        if (!status.equals(Status.DELIVERED)) return;

        Client client = iClientRepository.findById(clientId).orElseThrow(ClientNotFoundException::new);
        String clientName = client.getFirstName() + " " + client.getLastName();
        String message = iHtmlMessageTemplateService.deliveryCompletedTemplate(clientName);

        iEmailService.sendMessage(
                client.getEmail(),
                "E-Sale: Entrega de pedido realizada",
                message
        );
    }

    @KafkaListener(
            topics = "sale",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "saleKafkaListenerFactory"
    )
    public void saleListener(SaleEventDto event) {
        String id = event.id();
        String clientId = event.clientId();
        Status status = event.status();
        PaymentInfoDto payment = null;

        if (!status.equals(Status.CANCELED)) return;

        try {
            payment = iClientPayment.getPaymentInfo(id);
        } catch (FeignClientException e) {
            throw new ErrorGettingPaymentInfoException("It was not possible to get payment info!");
        }

        Client client = iClientRepository.findById(clientId).orElseThrow(ClientNotFoundException::new);
        String clientName = client.getFirstName() + " " + client.getLastName();
        String message = iHtmlMessageTemplateService.saleCanceledTemplate(clientName);

        iEmailService.sendMessage(
                client.getEmail(),
                "E-Sale: Pedido cancelado",
                message
        );
    }
}
