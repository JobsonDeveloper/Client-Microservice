package br.com.client.micro.service;

public interface IEmailService {
    public void sendMessage(String to, String subject, String body);
}
