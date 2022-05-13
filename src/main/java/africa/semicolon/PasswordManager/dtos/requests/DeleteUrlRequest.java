package africa.semicolon.PasswordManager.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteUrlRequest {
    private String urlAddress;
    private String password;
}
