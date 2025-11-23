package br.com.client.micro.exceptions;

public class ErrorDeletingClientException extends RuntimeException {
  public ErrorDeletingClientException() {
    super("We were unable to delete the client!");
  }
  public ErrorDeletingClientException(String message) {
    super(message);
  }
}
