package br.com.user.micro.exceptions;

public class ErrorExecutingOperationException extends RuntimeException {
    public ErrorExecutingOperationException() {
        super("The operation could not be completed due to an internal error!");
    }
    public ErrorExecutingOperationException(String message) {
        super(message);
    }
}
