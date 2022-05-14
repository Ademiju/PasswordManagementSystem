package africa.semicolon.PasswordManager.datas.repositories;

import africa.semicolon.PasswordManager.datas.models.Url;
import africa.semicolon.PasswordManager.datas.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Set;

public interface UrlRepository extends MongoRepository<Url,String> {

   Set<Url> findUrlByUsername(String userName);
}
