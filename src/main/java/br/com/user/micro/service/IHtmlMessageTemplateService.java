package br.com.user.micro.service;

public interface IHtmlMessageTemplateService {
    public String paymentProcessedTemplate(String username);
    public String deliveryCompletedTemplate(String username);
    public String saleCanceledTemplate(String username);
}
