package africa.semicolon.PasswordManager.exceptions;

public class UserNotFoundException extends PasswordManagerException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
