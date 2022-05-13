package africa.semicolon.PasswordManager.dtos.requests;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class UrlRequest {

    @NonNull
    private String urlAddress;
    @NonNull
    private String username;
    @NonNull
    private String emailAddress;
    @NonNull
    private String password;
    private String firstName;
    private String lastName;
}
