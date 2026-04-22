package br.com.user.micro.exceptions;

public class DifferentPasswordsException extends RuntimeException {
    public DifferentPasswordsException() {
        super("The password and the confirmation password must be equals!");
    }
    public DifferentPasswordsException(String message) {
        super(message);
    }
}
