package africa.semicolon.PasswordManager.dtos.requests;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PasswordUpdateRequest {
    private String oldPassword;
    private String newPassword;
    private String confirmNewPassword;
}
