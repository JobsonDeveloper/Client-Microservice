package br.com.client.micro.exceptions;

public class ErrorCreatingClientException extends RuntimeException {
  public ErrorCreatingClientException() {
    super("It was not possible to create the client");
  }
    public ErrorCreatingClientException(String message) {
        super(message);
    }
}
