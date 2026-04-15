package br.com.client.micro.service.imp;

import br.com.client.micro.service.IHtmlMessageTemplateService;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class HtmlMessageTemplateService implements IHtmlMessageTemplateService {

    private final TemplateEngine templateEngine;

    public HtmlMessageTemplateService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public String paymentProcessedTemplate(String clientName) {
        Context context = new Context();
        context.setVariable("nome", clientName);

        return templateEngine.process("email/paymentProcessed", context);
    }

    @Override
    public String deliveryCompletedTemplate(String clientName) {
        Context context = new Context();
        context.setVariable("nome", clientName);

        return templateEngine.process("email/deliveryCompleted", context);
    }
}
