package br.com.client.micro.exceptions;

public class ErrorGettingPaymentInfoException extends RuntimeException {
    public ErrorGettingPaymentInfoException(String message) {
        super(message);
    }
}
