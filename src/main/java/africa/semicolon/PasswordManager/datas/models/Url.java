package africa.semicolon.PasswordManager.datas.models;

import africa.semicolon.PasswordManager.dtos.responses.UserResponse;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
@Document("Url")
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Url {

    @Id
    private String id;
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
    @NonNull
    private String urlOwner;



}
