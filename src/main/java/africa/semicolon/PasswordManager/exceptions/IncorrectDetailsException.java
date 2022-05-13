package africa.semicolon.PasswordManager.exceptions;

public class IncorrectDetailsException extends UserNotFoundException {
    public IncorrectDetailsException(String message) {
        super(message);
    }

}
