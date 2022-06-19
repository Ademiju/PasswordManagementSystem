package africa.semicolon.PasswordManager.datas.repositories;

import africa.semicolon.PasswordManager.datas.models.Url;
import africa.semicolon.PasswordManager.datas.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UrlRepository extends MongoRepository<Url,String> {

   Optional<Url> findUrlByUsername(String userName);

   Optional<Url> findUrlByUrlAddress(String emailAddress);
}
