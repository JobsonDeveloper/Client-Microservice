package br.com.client.micro.exceptions;

public class ErrorSendingEmailException extends RuntimeException {
    public ErrorSendingEmailException(String message) {
        super(message);
    }
}
