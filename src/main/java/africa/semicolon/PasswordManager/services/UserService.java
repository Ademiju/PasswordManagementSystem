package africa.semicolon.PasswordManager.services;

import africa.semicolon.PasswordManager.datas.models.Url;
import africa.semicolon.PasswordManager.datas.models.User;
import africa.semicolon.PasswordManager.dtos.requests.*;
import africa.semicolon.PasswordManager.dtos.responses.UpdateResponse;
import africa.semicolon.PasswordManager.dtos.responses.UrlResponse;
import africa.semicolon.PasswordManager.dtos.responses.UserResponse;

import java.util.List;
import java.util.Set;

public interface UserService {

    UserResponse createNewUserAccount(AccountRequest accountRequest);
    UpdateResponse updateUserAccountDetails( UpdateAccountRequest updateAccountRequest, String username);
    String updateAccountPassword(PasswordUpdateRequest passwordUpdateRequest, String username);
    User findUserAccountByUsername(String username);
    String deleteUserAccount(DeleteAccountRequest deleteAccountRequest);
    String login(LoginRequest loginRequest);





}
