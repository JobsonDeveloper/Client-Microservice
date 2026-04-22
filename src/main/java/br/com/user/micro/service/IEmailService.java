package br.com.user.micro.service;

public interface IEmailService {
    public void sendMessage(String to, String subject, String body);
}
