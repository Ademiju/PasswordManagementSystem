package africa.semicolon.PasswordManager.datas.repositories;

import africa.semicolon.PasswordManager.datas.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findUserByUserName(String userName);

    Optional<User> findUserByEmailAddress(String emailAddress);

    void deleteByUserName(String username);
}

