package africa.semicolon.PasswordManager.dtos.responses;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateResponse {
    private String userName;
    private String emailAddress;
    private String firstName;
    private String lastName;
    private String phoneNumber;

}
