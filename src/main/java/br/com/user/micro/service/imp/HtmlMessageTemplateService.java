package br.com.user.micro.service.imp;

import br.com.user.micro.service.IHtmlMessageTemplateService;
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
    public String paymentProcessedTemplate(String username) {
        return templateConstructor(new HashMap<>(
                Map.of("nome", username)
        ), "email/paymentProcessed");
    }

    @Override
    public String deliveryCompletedTemplate(String username) {
        return templateConstructor(new HashMap<>(
                Map.of("nome", username)
        ), "email/deliveryCompleted");
    }

    @Override
    public String saleCanceledTemplate(String username) {
        return templateConstructor(new HashMap<>(
                Map.of("nome", username)
        ), "email/saleCancelled");
    }
}
