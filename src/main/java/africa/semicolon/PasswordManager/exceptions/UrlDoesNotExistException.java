package africa.semicolon.PasswordManager.exceptions;

public class UrlDoesNotExistException extends UserNotFoundException {
    public UrlDoesNotExistException(String message) {
        super(message);
    }
}
