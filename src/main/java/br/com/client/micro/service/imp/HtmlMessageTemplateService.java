package br.com.client.micro.service.imp;

import br.com.client.micro.service.IHtmlMessageTemplateService;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.util.HashMap;
import java.util.Map;

@Service
public class HtmlMessageTemplateService implements IHtmlMessageTemplateService {

    private final TemplateEngine templateEngine;

    public HtmlMessageTemplateService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String templateConstructor(HashMap<String, String> variables, String templatePath) {
        Context context = new Context();
        variables.forEach(context::setVariable);
        return templateEngine.process(templatePath, context);
    }

    @Override
    public String paymentProcessedTemplate(String clientName) {
        return templateConstructor(new HashMap<>(
                Map.of("nome", clientName)
        ), "email/paymentProcessed");
    }

    @Override
    public String deliveryCompletedTemplate(String clientName) {
        return templateConstructor(new HashMap<>(
                Map.of("nome", clientName)
        ), "email/deliveryCompleted");
    }

    @Override
    public String saleCanceledTemplate(String clientName) {
        return templateConstructor(new HashMap<>(
                Map.of("nome", clientName)
        ), "email/saleCancelled");
    }
}
