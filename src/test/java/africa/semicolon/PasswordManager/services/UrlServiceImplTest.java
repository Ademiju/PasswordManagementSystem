package africa.semicolon.PasswordManager.services;

import africa.semicolon.PasswordManager.datas.models.Url;
import africa.semicolon.PasswordManager.datas.repositories.UrlRepository;
import africa.semicolon.PasswordManager.datas.repositories.UserRepository;
import africa.semicolon.PasswordManager.dtos.requests.*;
import africa.semicolon.PasswordManager.dtos.responses.UpdateResponse;
import africa.semicolon.PasswordManager.dtos.responses.UrlResponse;
import africa.semicolon.PasswordManager.dtos.responses.UserResponse;
import africa.semicolon.PasswordManager.exceptions.IncorrectDetailsException;
import africa.semicolon.PasswordManager.exceptions.UrlDoesNotExistException;
import africa.semicolon.PasswordManager.exceptions.UserNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ImportAutoConfiguration(exclude = EmbeddedMongoAutoConfiguration.class)

class UrlServiceImplTest {

    @Autowired
    UrlService urlService;
    @Autowired
    UrlRepository urlRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    AccountRequest firstRequest;
    AccountRequest secondRequest;
    UrlRequest firstUrlRequest;
    UrlRequest secondUrlRequest;
    UrlRequest thirdUrlRequest;
    UrlRequest fourthUrlRequest;
    UrlRequest fifthUrlRequest;
    UrlRequest sixthUrlRequest;
    UrlRequest seventhUrlRequest;

    @BeforeEach
    public void setUp(){
        firstRequest = AccountRequest.builder().emailAddress("test@email.com").userName("Miju").password("Ademiju1").firstName("Ademiju").lastName("Taiwo").phoneNumber("08165563818").build();
        secondRequest = AccountRequest.builder().emailAddress("tester@email.com").userName("Juwon").password("Ademiju1#").firstName("Ade").lastName("Taiwo").phoneNumber("08043567890").build();

        firstUrlRequest = UrlRequest.builder().urlAddress("www.facebook.com").emailAddress("myju@email.com").username("Ademiju").password("juwonlo1").build();
        secondUrlRequest = UrlRequest.builder().urlAddress("www.twitter.com").emailAddress("myju@email.com").username("Ademiju1").password("juwonlo1").build();
        thirdUrlRequest = UrlRequest.builder().urlAddress("www.goodreads.com").emailAddress("ademi@email.com").username("Ademiju").password("juwonlo").build();

        fourthUrlRequest = UrlRequest.builder().urlAddress("www.instagram.com").emailAddress("ademiju@email.com").username("Ademiju").password("juwonlo").build();
        fifthUrlRequest = UrlRequest.builder().urlAddress("www.linkedin.com").emailAddress("myju@email.com").username("ademiju1").password("juwonlo1#").build();
        sixthUrlRequest = UrlRequest.builder().urlAddress("www.paypal.com").emailAddress("ademi@email.com").username("Ademiju").password("juwonlo").build();
        seventhUrlRequest = UrlRequest.builder().urlAddress("www.facebook.com").emailAddress("ademi@email.com").username("Ademiju").password("Ademijuwonlo").build();
    }

    @Test
    public void userCanAdd_UrlDetailsWhenLoggedInTest() {
        UserResponse userResponse = userService.createNewUserAccount(firstRequest);
        LoginRequest login = LoginRequest.builder().userName("Miju").password("Ademiju1").build();
        userService.userLogin(login);
        UrlResponse urlResponse = urlService.addNewUrl(firstUrlRequest, userResponse.getUsername());
        assertNotNull(urlResponse);
        assertThat(urlResponse.getUrlAddress(), is(firstUrlRequest.getUrlAddress()));
        assertThat(urlResponse.getEmailAddress(), is(firstUrlRequest.getEmailAddress()));
        assertThat(urlResponse.getUsername(), is(firstUrlRequest.getUsername()));
        assertThat(urlResponse.getPassword(), is(firstUrlRequest.getPassword()));

    }
    @Test
    public void userAdd_UrlDetailsWhenNotLoggedInThrowsExceptionTest() {
        UserResponse userResponse = userService.createNewUserAccount(firstRequest);
        assertThatThrownBy(()->urlService.addNewUrl(firstUrlRequest, userResponse.getUsername())).isInstanceOf(IncorrectDetailsException.class).hasMessage("You are not logged In");
    }

    @Test
    public void add_UrlDetails_WithUsernameThatDoesNotExist_ThrowsExceptionTest() {
        userService.createNewUserAccount(firstRequest);
        LoginRequest login = LoginRequest.builder().userName("Miju").password("Ademiju1").build();
        userService.userLogin(login);
        assertThatThrownBy(() -> urlService.addNewUrl(firstUrlRequest, "Increase")).isInstanceOf(UserNotFoundException.class).hasMessage("User with this Username does not exist");

    }

    @Test
    public void userCanAddMultiple_UrlDetailsWhenLoggedInTest() {
        UserResponse firstUserResponse = userService.createNewUserAccount(firstRequest);
        UserResponse secondUserResponse = userService.createNewUserAccount(secondRequest);
        String firstUserUsername = firstUserResponse.getUsername();
        String secondUserUsername = secondUserResponse.getUsername();

        LoginRequest login = LoginRequest.builder().userName("Miju").password("Ademiju1").build();
        userService.userLogin(login);
        LoginRequest login2 = LoginRequest.builder().userName("Juwon").password("Ademiju1#").build();
        userService.userLogin(login2);
        UrlResponse firstUrlResponse = urlService.addNewUrl(firstUrlRequest, firstUserUsername);
        UrlResponse secondUrlResponse = urlService.addNewUrl(secondUrlRequest,firstUserUsername);
        UrlResponse thirdUrlResponse = urlService.addNewUrl(thirdUrlRequest,firstUserUsername);
        UrlResponse firstUrlResponse2 = urlService.addNewUrl(fourthUrlRequest, secondUserUsername);
        UrlResponse secondUrlResponse2 = urlService.addNewUrl(fifthUrlRequest,secondUserUsername);
        UrlResponse thirdUrlResponse2 = urlService.addNewUrl(sixthUrlRequest,secondUserUsername);
        assertThat(firstUrlResponse.getUrlOwner(),is(firstUserUsername));
        assertThat(secondUrlResponse.getUrlOwner(),is(firstUserUsername));
        assertThat(thirdUrlResponse.getUrlOwner(),is(firstUserUsername));
        assertThat(firstUrlResponse2.getUrlOwner(),is(secondUserUsername));
        assertThat(secondUrlResponse2.getUrlOwner(),is(secondUserUsername));
        assertThat(thirdUrlResponse2.getUrlOwner(),is(secondUserUsername));

    }

    @Test
    public void userCanSearchUrl_ByUrlUsername() {
        UserResponse userResponse = userService.createNewUserAccount(firstRequest);
        UserResponse secondUserResponse = userService.createNewUserAccount(secondRequest);
        String secondUserUsername = secondUserResponse.getUsername();

        LoginRequest login = LoginRequest.builder().userName("Miju").password("Ademiju1").build();
        userService.userLogin(login);

        LoginRequest login2 = LoginRequest.builder().userName("Juwon").password("Ademiju1#").build();
        userService.userLogin(login2);

        urlService.addNewUrl(firstUrlRequest, userResponse.getUsername());
        urlService.addNewUrl(secondUrlRequest, userResponse.getUsername());
        urlService.addNewUrl(thirdUrlRequest, userResponse.getUsername());
        urlService.addNewUrl(fourthUrlRequest, secondUserUsername);
        urlService.addNewUrl(fifthUrlRequest, secondUserUsername);
        urlService.addNewUrl(sixthUrlRequest, secondUserUsername);
        urlService.addNewUrl(seventhUrlRequest, secondUserUsername);

        List<Url> firstUserUrlList = urlService.searchUrlByUrlUsername(login.getUserName(), "ademiju");
        List<Url> secondUserUrlList = urlService.searchUrlByUrlUsername(login2.getUserName(), "ademiju");

        assertThat(firstUserUrlList.size(), is(2));
        assertThat(secondUserUrlList.size(), is(3));
        assertThat(firstUserUrlList.get(0).getUrlAddress(), is(firstUrlRequest.getUrlAddress()));
        assertThat(firstUserUrlList.get(1).getUrlAddress(), is(thirdUrlRequest.getUrlAddress()));
        assertThat(secondUserUrlList.get(0).getUrlAddress(), is(fourthUrlRequest.getUrlAddress()));
        assertThat(secondUserUrlList.get(1).getUrlAddress(), is(sixthUrlRequest.getUrlAddress()));
        assertThat(secondUserUrlList.get(2).getUrlAddress(),is(seventhUrlRequest.getUrlAddress()));

    }
        @Test
        public void find_UrlDetails_WithUsernameThatDoesNotExist_ThrowsExceptionTest() {
            userService.createNewUserAccount(firstRequest);
            LoginRequest login = LoginRequest.builder().userName("Miju").password("Ademiju1").build();
            userService.userLogin(login);
            assertThatThrownBy(() -> urlService.searchUrlByUrlUsername(login.getUserName(), "Increase")).isInstanceOf(UrlDoesNotExistException.class).hasMessage("No url with this username found");

        }

        @Test
        public void find_UrlDetails_WithUrlAddressTest(){
            UserResponse userResponse = userService.createNewUserAccount(firstRequest);
            LoginRequest login = LoginRequest.builder().userName("Miju").password("Ademiju1").build();
            userService.userLogin(login);
            urlService.addNewUrl(firstUrlRequest, userResponse.getUsername());
            urlService.addNewUrl(secondUrlRequest, userResponse.getUsername());
            List<Url> firstUserUrlList = urlService.searchUrlByUrlAddress(login.getUserName(), "www.facebook.com");
            assertThat(firstUserUrlList.get(0).getUsername(), is(firstUrlRequest.getUsername()));
            assertThat(firstUserUrlList.get(0).getPassword(), is(firstUrlRequest.getPassword()));
            assertThat(firstUserUrlList.get(0).getEmailAddress(), is(firstUrlRequest.getEmailAddress()));

        }
        @Test
        public void allUrlCanBeSearchedTest(){
            UserResponse userResponse = userService.createNewUserAccount(firstRequest);
            LoginRequest login = LoginRequest.builder().userName("Miju").password("Ademiju1").build();
            userService.userLogin(login);
            urlService.addNewUrl(firstUrlRequest, userResponse.getUsername());
            urlService.addNewUrl(secondUrlRequest, userResponse.getUsername());
            urlService.addNewUrl(thirdUrlRequest, userResponse.getUsername());
            assertThat(urlService.findAllUrl(userResponse.getUsername()).size(),is(3));

        }
        @Test
        public void urlDetailsCanBeUpdatedTest(){
            UserResponse userResponse = userService.createNewUserAccount(firstRequest);
            LoginRequest login = LoginRequest.builder().userName("Miju").password("Ademiju1").build();
            userService.userLogin(login);
            UrlResponse urlResponse = urlService.addNewUrl(firstUrlRequest, userResponse.getUsername());
            UrlUpdateRequest urlUpdateRequest = UrlUpdateRequest.builder().urlAddress("www.facebuk.com").emailAddress("freemail@email.com").firstName("").lastName("").username("Ademiju").password("Juwonlo*13").build();
            UpdateResponse updateResponse = urlService.updateUrlDetails(urlUpdateRequest,urlResponse.getId());
            assertThat(updateResponse.getUrlAddress(),is(urlUpdateRequest.getUrlAddress()));
            assertThat(updateResponse.getEmailAddress(),is(urlUpdateRequest.getEmailAddress()));
            assertThat(updateResponse.getPassword(),is(urlUpdateRequest.getPassword()));
            assertThat(updateResponse.getUserName(),is(urlUpdateRequest.getUsername()));
            assertThat(updateResponse.getFirstName(),is(firstUrlRequest.getFirstName()));
            assertThat(updateResponse.getLastName(),is(firstUrlRequest.getLastName()));
        }

        @Test
        public void urlCanBeDeletedTest(){
            UserResponse userResponse = userService.createNewUserAccount(firstRequest);
            LoginRequest login = LoginRequest.builder().userName("Miju").password("Ademiju1").build();
            userService.userLogin(login);
            urlService.addNewUrl(firstUrlRequest, userResponse.getUsername());
            UrlResponse urlResponse = urlService.addNewUrl(secondUrlRequest, userResponse.getUsername());
            urlService.addNewUrl(thirdUrlRequest, userResponse.getUsername());

            UrlDeleteRequest urlDeleteRequest = UrlDeleteRequest.builder().urlAddress("www.twitter.com").userPassword("Ademiju1").build();
            assertThat(urlService.deleteUrl(urlDeleteRequest,urlResponse.getId()),is("Delete Successful"));

        }
    @Test
    public void urlDeleteWithWrongPasswordThrowsExceptionTest(){
        UserResponse userResponse = userService.createNewUserAccount(firstRequest);
        LoginRequest login = LoginRequest.builder().userName("Miju").password("Ademiju1").build();
        userService.userLogin(login);
        urlService.addNewUrl(firstUrlRequest, userResponse.getUsername());
        UrlResponse urlResponse = urlService.addNewUrl(secondUrlRequest, userResponse.getUsername());
        urlService.addNewUrl(thirdUrlRequest, userResponse.getUsername());

        UrlDeleteRequest urlDeleteRequest = UrlDeleteRequest.builder().urlAddress("www.twitter.com").userPassword("Ademiju").build();
        assertThatThrownBy(()->urlService.deleteUrl(urlDeleteRequest,urlResponse.getId())).isInstanceOf(IncorrectDetailsException.class).hasMessage("Incorrect Password!! Failed to delete");

    }
    @Test
    public void deleteUrlThatDoesNotExistThrowsExceptionTest(){
        UserResponse userResponse = userService.createNewUserAccount(firstRequest);
        LoginRequest login = LoginRequest.builder().userName("Miju").password("Ademiju1").build();
        userService.userLogin(login);
        urlService.addNewUrl(firstUrlRequest, userResponse.getUsername());
        UrlResponse urlResponse = urlService.addNewUrl(secondUrlRequest, userResponse.getUsername());
        urlService.addNewUrl(thirdUrlRequest, userResponse.getUsername());

        UrlDeleteRequest urlDeleteRequest = UrlDeleteRequest.builder().urlAddress("www.instagram.com").userPassword("Ademiju1").build();
        assertThatThrownBy(()->urlService.deleteUrl(urlDeleteRequest,urlResponse.getId())).isInstanceOf(UrlDoesNotExistException.class).hasMessage("Url does not exist");

    }
        @AfterEach
        void tearDown(){
            urlRepository.deleteAll();
            userRepository.deleteAll();
}
}