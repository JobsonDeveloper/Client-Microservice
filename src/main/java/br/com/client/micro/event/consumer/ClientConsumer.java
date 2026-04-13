package br.com.client.micro.event.consumer;

import br.com.client.micro.domain.Client;
import br.com.client.micro.domain.Status;
import br.com.client.micro.event.dto.PaymentEventDto;
import br.com.client.micro.exceptions.ClientNotFoundException;
import br.com.client.micro.repository.IClientRepository;
import br.com.client.micro.service.IEmailService;
import br.com.client.micro.service.IHtmlMessageTemplateService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ClientConsumer {
    private final IEmailService iEmailService;
    private final IClientRepository iClientRepository;
    private final IHtmlMessageTemplateService iHtmlMessageTemplateService;

    public ClientConsumer(
            IEmailService iEmailService,
            IClientRepository iClientRepository,
            IHtmlMessageTemplateService iHtmlMessageTemplateService
    ) {
        this.iEmailService = iEmailService;
        this.iClientRepository = iClientRepository;
        this.iHtmlMessageTemplateService = iHtmlMessageTemplateService;
    }

    @KafkaListener(
            topics = "payment",
            groupId = "client-group",
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

        iEmailService.sendMessage(client.getEmail(), "E-Sale: Pagamento processado", message);
    }
}
