package africa.semicolon.PasswordManager.dtos.responses;

import lombok.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private String id;
    private String username;
    private String emailAddress;
    private String message;
}
