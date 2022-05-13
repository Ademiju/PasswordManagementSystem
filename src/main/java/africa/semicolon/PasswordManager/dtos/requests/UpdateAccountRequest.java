package africa.semicolon.PasswordManager.dtos.requests;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class UpdateAccountRequest {
    private String userName;
    private String emailAddress;
    private String firstName;
    private String lastName;
    private String phoneNumber;
}
