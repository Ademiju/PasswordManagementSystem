package africa.semicolon.PasswordManager.dtos.responses;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateResponse {
    private String urlAddress;
    private String userName;
    private String emailAddress;
    private String firstName;
    private String password;
    private String lastName;
    private String phoneNumber;
    private String message;

}
