package africa.semicolon.PasswordManager.services;

import africa.semicolon.PasswordManager.datas.models.User;
import africa.semicolon.PasswordManager.datas.repositories.UserRepository;
import africa.semicolon.PasswordManager.dtos.requests.*;
import africa.semicolon.PasswordManager.dtos.responses.UpdateResponse;
import africa.semicolon.PasswordManager.dtos.responses.UserResponse;
import africa.semicolon.PasswordManager.exceptions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ImportAutoConfiguration(exclude = EmbeddedMongoAutoConfiguration.class)
class UserServiceImplTest {
    @Autowired
    UserService service;

    @Autowired
    UserRepository repository;


    AccountRequest firstRequest;
    AccountRequest secondRequest;

    @BeforeEach
    public void setUp(){
        firstRequest = AccountRequest.builder().emailAddress("test@email.com").userName("Miju").password("Ademiju1").firstName("Ademiju").lastName("Taiwo").phoneNumber("08165563818").build();
        secondRequest = AccountRequest.builder().emailAddress("tester@email.com").userName("Juwon").password("Ademiju1#").firstName("Ade").lastName("Taiwo").phoneNumber("08043567890").build();
    }

    @Test
    public void userCanBeCreatedTest() {
        UserResponse firstResponse = service.createNewUserAccount(firstRequest);
        UserResponse secondResponse = service.createNewUserAccount(secondRequest);
        assertNotNull(firstResponse);
        assertNotNull(secondResponse);
        assertThat(secondResponse.getUsername(),is(secondRequest.getUserName()));
        assertThat(firstResponse.getUsername(),is(firstRequest.getUserName()));

    }

    @Test
    public void userCanBeCreatedIfOnlyRequiredFieldsAreFilledTest() {
        AccountRequest request = AccountRequest.builder().emailAddress("test@email.com").userName("Miju").password("Ademiju1").build();
        UserResponse response = service.createNewUserAccount(request);
        assertNotNull(response);
        assertThat(response.getUsername(), is(request.getUserName()));
        assertThat(response.getEmailAddress(), is(request.getEmailAddress()));

    }

    @Test
    public void createNewUser_WithExistingUsername_ThrowsExceptionTest() {
        service.createNewUserAccount(firstRequest);
        assertThatThrownBy(() -> service.createNewUserAccount(firstRequest)).isInstanceOf(PasswordManagerException.class).hasMessage("Username already exist");

    }

    @Test
    public void createNewUser_WithExistingEmail_ThrowsExceptionTest() {
        service.createNewUserAccount(firstRequest);
        AccountRequest secondRequest = AccountRequest.builder().emailAddress("test@email.com").userName("Juwon").password("Ademiju1").firstName("Ademiju").lastName("Taiwo").phoneNumber("08165563818").build();
        assertThatThrownBy(() -> service.createNewUserAccount(secondRequest)).isInstanceOf(PasswordManagerException.class).hasMessage("Email address already exist");

    }
    @Test
    public void userCanLoginTest(){
        service.createNewUserAccount(firstRequest);
        service.createNewUserAccount(secondRequest);
        LoginRequest loginRequest = LoginRequest.builder().userName("Miju").password("Ademiju1").build();
        String message = service.login(loginRequest);
        assertThat(message,is("Successfully Logged In"));
    }
    @Test
    public void userLoginWithIncorrectUsernameOrPasswordThrowsExceptionTest(){
        service.createNewUserAccount(firstRequest);
        service.createNewUserAccount(secondRequest);
        LoginRequest loginRequest = LoginRequest.builder().userName("Miju").password("Ademiju1#").build();
        assertThatThrownBy(()->service.login(loginRequest)).isInstanceOf(IncorrectDetailsException.class).hasMessage("Incorrect Login details");
    }
    @Test
    public void userCanUpdate_AccountDetailsTest(){
        service.createNewUserAccount(firstRequest);
        service.createNewUserAccount(secondRequest);
        LoginRequest login = LoginRequest.builder().userName("Miju").password("Ademiju1").build();
        service.login(login);
        UpdateAccountRequest accountUpdateRequest = UpdateAccountRequest.builder().emailAddress("Ademiju@email.com").userName("Adelove").firstName("Ademijuwonlo").lastName("").phoneNumber("08156454321").build();
        UpdateResponse updateResponse = service.updateUserAccountDetails(accountUpdateRequest, login.getUserName());

        assertThat(updateResponse.getUserName(),is(accountUpdateRequest.getUserName()));
        assertThat(updateResponse.getEmailAddress(),is(accountUpdateRequest.getEmailAddress()));
        assertThat(updateResponse.getFirstName(),is(accountUpdateRequest.getFirstName()));
        assertThat(updateResponse.getLastName(), is(accountUpdateRequest.getLastName()));
        assertThat(updateResponse.getPhoneNumber(),is(accountUpdateRequest.getPhoneNumber()));

        LoginRequest loginWithUpdatedUserName = LoginRequest.builder().userName("Adelove").password("Ademiju1").build();
        String message = service.login(loginWithUpdatedUserName);
        assertThat(message,is("Successfully Logged In"));


    }
    @Test
    public void UsernameAndEmailAddressUpdateRemainUnchanged_WhenUsernameAndEmailAddressFieldsAreEmptyTest(){
        UserResponse userResponse = service.createNewUserAccount(firstRequest);
        service.createNewUserAccount(secondRequest);
        LoginRequest login = LoginRequest.builder().userName("Miju").password("Ademiju1").build();
        service.login(login);
        UpdateAccountRequest accountUpdateRequest = UpdateAccountRequest.builder().userName("").emailAddress("").lastName("JohnDoe").firstName("Ademiju").phoneNumber("08156454321").build();
        UpdateResponse updateResponse = service.updateUserAccountDetails(accountUpdateRequest,login.getUserName());
        assertThat(updateResponse.getUserName(),is(userResponse.getUsername()));
        assertThat(updateResponse.getEmailAddress(),is(userResponse.getEmailAddress()));
        assertThat(updateResponse.getFirstName(),is(accountUpdateRequest.getFirstName()));
        assertThat(updateResponse.getLastName(), is(accountUpdateRequest.getLastName()));
        assertThat(updateResponse.getPhoneNumber(),is(accountUpdateRequest.getPhoneNumber()));

    }
    @Test
    public void userCanUpdateAnyFieldAsTheyChooseTest(){
        UserResponse userResponse = service.createNewUserAccount(firstRequest);
        service.createNewUserAccount(secondRequest);
        LoginRequest login = LoginRequest.builder().userName("Miju").password("Ademiju1").build();
        service.login(login);
        UpdateAccountRequest accountUpdateRequest = UpdateAccountRequest.builder().userName("").emailAddress("ademijuwon@email.com").lastName("JohnDoe").firstName("Ademiju").phoneNumber("08165563818").build();
        UpdateResponse updateResponse = service.updateUserAccountDetails(accountUpdateRequest, login.getUserName());
        assertThat(updateResponse.getUserName(),is(userResponse.getUsername()));
        assertThat(updateResponse.getEmailAddress(),is(accountUpdateRequest.getEmailAddress()));
        assertThat(updateResponse.getFirstName(),is(accountUpdateRequest.getFirstName()));
        assertThat(updateResponse.getLastName(), is(accountUpdateRequest.getLastName()));
        assertThat(updateResponse.getPhoneNumber(),is(accountUpdateRequest.getPhoneNumber()));

    }

    @Test
    public void updateUsernameToAlreadyExistingUsername_ThrowsExceptionTest(){
        service.createNewUserAccount(firstRequest);
        service.createNewUserAccount(secondRequest);
        LoginRequest login = LoginRequest.builder().userName("Miju").password("Ademiju1").build();
        service.login(login);
        UpdateAccountRequest accountUpdateRequest = UpdateAccountRequest.builder().emailAddress("Ademiju@email.com").userName("Juwon").phoneNumber("08156454321").build();
        assertThatThrownBy(()-> service.updateUserAccountDetails(accountUpdateRequest, login.getUserName())).isInstanceOf(AlreadyExistException.class).hasMessage("Username Already Exist");

    }
    @Test
    public void updateEmailAddressToAlreadyExistingEmailAddress_ThrowsExceptionTest(){
        service.createNewUserAccount(firstRequest);
        service.createNewUserAccount(secondRequest);
        LoginRequest login = LoginRequest.builder().userName("Miju").password("Ademiju1").build();
        service.login(login);
        UpdateAccountRequest accountUpdateRequest = UpdateAccountRequest.builder().emailAddress("tester@email.com").userName("Ademiju").phoneNumber("08156454321").build();
        assertThatThrownBy(()-> service.updateUserAccountDetails(accountUpdateRequest,login.getUserName())).isInstanceOf(AlreadyExistException.class).hasMessage("Email address Already Exist");

    }
    @Test
    public void updateUserAccountPasswordTest(){
        service.createNewUserAccount(firstRequest);
        service.createNewUserAccount(secondRequest);
        PasswordUpdateRequest passwordUpdateRequest = PasswordUpdateRequest.builder().oldPassword("Ademiju1").newPassword("Ademijuwonlo#").confirmNewPassword("Ademijuwonlo#").build();
        LoginRequest login = LoginRequest.builder().userName("Miju").password("Ademiju1").build();
        service.login(login);
        String message = service.updateAccountPassword(passwordUpdateRequest,login.getUserName());
        assertThat(message,is("Password Successfully Updated"));
        LoginRequest loginWithUpdatedPassword = LoginRequest.builder().userName("Miju").password("Ademijuwonlo#").build();
        service.login(loginWithUpdatedPassword);
        assertThat(message,is("Password Successfully Updated"));

    }

    @Test
    public void updatePasswordWithIncorrectOldPasswordThrowExceptionTest(){
        service.createNewUserAccount(firstRequest);
        service.createNewUserAccount(secondRequest);
        PasswordUpdateRequest passwordUpdateRequest = PasswordUpdateRequest.builder().oldPassword("Ademiju").newPassword("Ademijuwonlo#").confirmNewPassword("Ademijuwonlo#").build();
        LoginRequest login = LoginRequest.builder().userName("Miju").password("Ademiju1").build();
        service.login(login);
        assertThatThrownBy(()->service.updateAccountPassword(passwordUpdateRequest,login.getUserName())).isInstanceOf(UnMatchingDetailsException.class).hasMessage("Incorrect password");

    }
    @Test
    public void updatePasswordWithUnMatchingNewPasswordThrowExceptionTest(){
        service.createNewUserAccount(firstRequest);
        service.createNewUserAccount(secondRequest);
        PasswordUpdateRequest passwordUpdateRequest = PasswordUpdateRequest.builder().oldPassword("Ademiju1").newPassword("Ademijuwonlo").confirmNewPassword("Ademijuwonlo#").build();
        LoginRequest login = LoginRequest.builder().userName("Miju").password("Ademiju1").build();
        service.login(login);

        assertThatThrownBy(()->service.updateAccountPassword(passwordUpdateRequest,login.getUserName())).isInstanceOf(UnMatchingDetailsException.class).hasMessage("New Password Does Not Match");

    }
    @Test
    public void findUserByUserNameTest(){
        service.createNewUserAccount(firstRequest);
        UserResponse userResponse = service.createNewUserAccount(secondRequest);
        User userFromRepository = service.findUserAccountByUsername("Juwon");
        assertThat(userFromRepository.getUserName(),is(userResponse.getUsername()));
    }

    @Test
    public void searchUserWithNonExistingUsernameThrowsExceptionTest(){
        service.createNewUserAccount(firstRequest);
        service.createNewUserAccount(secondRequest);
        assertThatThrownBy(()->service.findUserAccountByUsername("Adelove")).isInstanceOf(UserNotFoundException.class).hasMessage("User does not exist");

    }
    @Test
    public void userCanBeDeletedByUsernameTest(){
        service.createNewUserAccount(firstRequest);
        service.createNewUserAccount(secondRequest);
        DeleteAccountRequest accountRequest = DeleteAccountRequest.builder().username("Juwon").emailAddress("tester@email.com").password("Ademiju1#").confirmPassword("Ademiju1#").build();
        String message = service.deleteUserAccount(accountRequest);
        assertThatThrownBy(()->service.findUserAccountByUsername("Juwon")).isInstanceOf(UserNotFoundException.class).hasMessage("User does not exist");
        assertThat(message, is( "User Account successfully Deleted"));

    }

    @Test
    public void deleteUserWithWrongUserDetailsThrowExceptionTest(){
        service.createNewUserAccount(firstRequest);
        service.createNewUserAccount(secondRequest);
        DeleteAccountRequest accountRequest = DeleteAccountRequest.builder().username("Juwon").emailAddress("teste@email.com").password("Ademiju1#").confirmPassword("Ademiju1#").build();
        assertThatThrownBy(()->service.deleteUserAccount(accountRequest)).isInstanceOf(IncorrectDetailsException.class).hasMessage("Incorrect user details!!! Failed To Delete");

    }

    @AfterEach
    void tearDown(){
        repository.deleteAll();
    }


}
