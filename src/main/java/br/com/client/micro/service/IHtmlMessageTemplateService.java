package br.com.client.micro.service;

import org.thymeleaf.spring6.SpringTemplateEngine;

public interface IHtmlMessageTemplateService {
    public String paymentProcessedTemplate(String clientName);
    public String deliveryCompletedTemplate(String clientName);
    public String saleCanceledTemplate(String clientName);
}
