package br.com.client.micro.exceptions;

public class ErrorModifyingClientDataException extends RuntimeException {
    public ErrorModifyingClientDataException() {
        super("It was not possible to modify the client data!");
    }
    public ErrorModifyingClientDataException(String message) {
        super(message);
    }
}
