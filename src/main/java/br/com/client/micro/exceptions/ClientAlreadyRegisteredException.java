package br.com.client.micro.exceptions;

public class ClientAlreadyRegisteredException extends RuntimeException {
    public ClientAlreadyRegisteredException() {
        super("Client already registered!");
    }

    public ClientAlreadyRegisteredException(String message) {
        super(message);
    }
}
