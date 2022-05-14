package africa.semicolon.PasswordManager.dtos.requests;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteAccountRequest {
    private String username;
    private String emailAddress;
    private String password;
    private String confirmPassword;
}
