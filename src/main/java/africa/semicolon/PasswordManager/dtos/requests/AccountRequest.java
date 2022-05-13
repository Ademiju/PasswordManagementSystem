package africa.semicolon.PasswordManager.dtos.requests;

import lombok.*;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountRequest {
    @NonNull
    private String userName;
    @NonNull
    private String password;
    @NonNull
    private String emailAddress;
    private String firstName;
    private String lastName;
    private String phoneNumber;


}
