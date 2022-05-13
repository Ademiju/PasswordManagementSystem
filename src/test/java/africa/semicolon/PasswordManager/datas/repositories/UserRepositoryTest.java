package africa.semicolon.PasswordManager.datas.repositories;

import africa.semicolon.PasswordManager.datas.models.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@ExtendWith(SpringExtension.class)
class UserRepositoryTest {
    @Autowired
    UserRepository repository;
    @Test
    void userAccountCanBeSavedTest(){
        User account = new User("Adelove","Ademijuwonlo1","hardaymyju@gmail.com");
        repository.save(account);
        assertNotNull(account);
        assertThat(repository.count(),is(1L));
    }
    @Test
    void moreThanOneUserAccountsCanBeSavedTest(){
        User firstAccount = new User("Adelove","Ademijuwonlo1","hardaymyju@gmail.com");
        User secondAccount = new User("Adelove","Ademijuwonlo1","hardaymyju@gmail.com");
        User thirdAccount = new User("Adelove","Ademijuwonlo1","hardaymyju@gmail.com");
        User fourthAccount = new User("Adelove","Ademijuwonlo1","hardaymyju@gmail.com");
        repository.save(firstAccount);
        repository.save(secondAccount);
        repository.save(thirdAccount);
        repository.save(fourthAccount);
        assertThat(repository.count(), is(4L));
    }


}