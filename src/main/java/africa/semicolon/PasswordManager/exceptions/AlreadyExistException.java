package africa.semicolon.PasswordManager.exceptions;

public class AlreadyExistException extends PasswordManagerException{
    public AlreadyExistException(String message){
        super(message);
    }
}
