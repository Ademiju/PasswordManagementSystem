package africa.semicolon.PasswordManager.datas.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Set;

@Document("User")
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private String id;
    @NonNull
    private String userName;
    @NonNull
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    @NonNull
    private String emailAddress;
    private List<Url> urls;
    private boolean isLoggedIn;

}
