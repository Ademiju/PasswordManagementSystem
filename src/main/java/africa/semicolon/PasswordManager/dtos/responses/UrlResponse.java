package africa.semicolon.PasswordManager.dtos.responses;

import africa.semicolon.PasswordManager.datas.models.User;
import lombok.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UrlResponse {
    private String urlAddress;
    private String username;
    private String emailAddress;
    private String password;
    private UserResponse urlOwner;
}
