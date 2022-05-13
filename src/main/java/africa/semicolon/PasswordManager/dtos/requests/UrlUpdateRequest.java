package africa.semicolon.PasswordManager.dtos.requests;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UrlUpdateRequest {
    private String urlAddress;
    private String username;
    private String emailAddress;
    private String password;
    private String firstName;
    private String lastName;
}
