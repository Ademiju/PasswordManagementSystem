package africa.semicolon.PasswordManager.dtos.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DeleteAccountRequest {
    private String username;
    private String emailAddress;
    private String password;
    private String confirmPassword;
}
