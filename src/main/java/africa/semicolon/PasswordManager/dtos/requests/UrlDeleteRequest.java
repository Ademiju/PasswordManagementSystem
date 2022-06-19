package africa.semicolon.PasswordManager.dtos.requests;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UrlDeleteRequest {
    private String urlAddress;
    private String userPassword;
}
