package africa.semicolon.PasswordManager.dtos.requests;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest {
    String userName;
    String password;
}
