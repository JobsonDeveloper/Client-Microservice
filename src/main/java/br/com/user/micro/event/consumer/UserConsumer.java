package br.com.user.micro.event.consumer;

import br.com.user.micro.domain.User;
import br.com.user.micro.domain.enums.Status;
import br.com.user.micro.event.dto.DeliveryEventDto;
import br.com.user.micro.event.dto.PaymentEventDto;
import br.com.user.micro.event.dto.SaleEventDto;
import br.com.user.micro.exceptions.UserNotFoundException;
import br.com.user.micro.exceptions.ErrorGettingPaymentInfoException;
import br.com.user.micro.repository.IUserRepository;
import br.com.user.micro.service.IPayment;
import br.com.user.micro.service.IEmailService;
import br.com.user.micro.service.IHtmlMessageTemplateService;
import feign.FeignException;
import feign.FeignException.FeignClientException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class UserConsumer {
    private final IEmailService iEmailService;
    private final IUserRepository iUserRepository;
    private final IHtmlMessageTemplateService iHtmlMessageTemplateService;
    private final IPayment iPayment;

    public UserConsumer(
            IEmailService iEmailService,
            IUserRepository iUserRepository,
            IHtmlMessageTemplateService iHtmlMessageTemplateService, IPayment iPayment
    ) {
        this.iEmailService = iEmailService;
        this.iUserRepository = iUserRepository;
        this.iHtmlMessageTemplateService = iHtmlMessageTemplateService;
        this.iPayment = iPayment;
    }

    @KafkaListener(
            topics = "payment",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "paymentKafkaListenerFactory"
    )
    public void saleListener(PaymentEventDto event) {
        String userId = event.userId();
        Status status = event.status();

        if (!status.equals(Status.PAID)) return;

        User user = iUserRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        String userName = user.getFirstName() + " " + user.getLastName();
        String message = iHtmlMessageTemplateService.paymentProcessedTemplate(userName);

        iEmailService.sendMessage(
                user.getEmail(),
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
        String userId = event.userId();
        Status status = event.status();

        if (!status.equals(Status.DELIVERED)) return;

        User user = iUserRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        String userName = user.getFirstName() + " " + user.getLastName();
        String message = iHtmlMessageTemplateService.deliveryCompletedTemplate(userName);

        iEmailService.sendMessage(
                user.getEmail(),
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
        String saleId = event.id();
        String userId = event.userId();
        Status status = event.status();

        if (!status.equals(Status.CANCELED)) return;

        try {
            iPayment.getPaymentInfo(saleId);
        }  catch (FeignException.NotFound e){
            return;
        } catch (FeignClientException e) {
            throw new ErrorGettingPaymentInfoException("It was not possible to get payment info!");
        }

        User user = iUserRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        String userName = user.getFirstName() + " " + user.getLastName();
        String message = iHtmlMessageTemplateService.saleCanceledTemplate(userName);

        iEmailService.sendMessage(
                user.getEmail(),
                "E-Sale: Pedido cancelado",
                message
        );
    }
}
