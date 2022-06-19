package africa.semicolon.PasswordManager.services;

import africa.semicolon.PasswordManager.datas.models.User;
import africa.semicolon.PasswordManager.datas.repositories.UrlRepository;
import africa.semicolon.PasswordManager.datas.repositories.UserRepository;
import africa.semicolon.PasswordManager.dtos.requests.*;
import africa.semicolon.PasswordManager.dtos.responses.UpdateResponse;
import africa.semicolon.PasswordManager.dtos.responses.UserResponse;
import africa.semicolon.PasswordManager.exceptions.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository repository;

    ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private UrlRepository urlRepository;


    @Override
    public UserResponse createNewUserAccount(AccountRequest accountRequest) {
        Optional<User> user = repository.findUserByUserName(accountRequest.getUserName());
        if (user.isPresent()) throw new PasswordManagerException("Username already exist");

        Optional<User> optionalUser = repository.findUserByEmailAddress(accountRequest.getEmailAddress());
        if (optionalUser.isPresent()) throw new PasswordManagerException("Email address already exist");
        User userAccount = new User();
        modelMapper.map(accountRequest, userAccount);
        userAccount.setUrls(new ArrayList<>());
        repository.save(userAccount);
        UserResponse userResponse = new UserResponse();
        modelMapper.map(userAccount, userResponse);
        userResponse.setMessage("Registration Successful");
        return userResponse;
    }

    @Override
    public UpdateResponse updateUserAccountDetails(UpdateAccountRequest updateAccountRequest, String username) {
        User user = repository.findUserByUserName(username).orElseThrow(() -> new UserNotFoundException("Account not found"));

        if (user.isLoggedIn()) {
            if (!updateAccountRequest.getEmailAddress().trim().equals("")) {
                Optional<User> existingUser = repository.findUserByEmailAddress(updateAccountRequest.getEmailAddress());
                if (existingUser.isPresent()) throw new AlreadyExistException("Email address Already Exist");
                user.setEmailAddress(updateAccountRequest.getEmailAddress());
            }
            if (!(updateAccountRequest.getUserName().trim().equals("") || updateAccountRequest.getUserName() == null)) {
                Optional<User> existingUser = repository.findUserByUserName(updateAccountRequest.getUserName());
                if (existingUser.isPresent()) throw new AlreadyExistException("Username Already Exist");
                user.setUserName(updateAccountRequest.getUserName());
            }

            if (!(updateAccountRequest.getFirstName().trim().equals("") || updateAccountRequest.getFirstName() == null)) {
                user.setFirstName(updateAccountRequest.getFirstName());
            }
            if (!(updateAccountRequest.getPhoneNumber().trim().equals("") || updateAccountRequest.getPhoneNumber() == null)) {
                user.setPhoneNumber(updateAccountRequest.getPhoneNumber());
            }
            if (!(updateAccountRequest.getLastName().trim().equals("") || updateAccountRequest.getLastName() == null)) {
                user.setLastName(updateAccountRequest.getLastName());
            }
        } else {
            throw new IncorrectDetailsException("You must be logged in");
        }
        repository.save(user);

        UpdateResponse updateResponse = new UpdateResponse();
        modelMapper.map(user, updateResponse);
        return updateResponse;
    }

    @Override
    public String updateAccountPassword(PasswordUpdateRequest passwordUpdateRequest, String username) {
        User user = repository.findUserByUserName(username).orElseThrow(() -> new UserNotFoundException("Account not found"));
        System.out.println(user.isLoggedIn());
        if (user.isLoggedIn()) {
            if (passwordUpdateRequest.getOldPassword().equals(user.getPassword())) {
                if (passwordUpdateRequest.getNewPassword().equals(passwordUpdateRequest.getConfirmNewPassword())) {
                    user.setPassword(passwordUpdateRequest.getNewPassword());
                } else {
                    throw new UnMatchingDetailsException("New Password Does Not Match");
                }
            } else {
                throw new UnMatchingDetailsException("Incorrect password");
            }
        } else {
            throw new IncorrectDetailsException("Incorrect Login Details");
        }

        repository.save(user);
        return "Password Successfully Updated";
    }

    @Override
    public User findUserAccountByUsername(String username) {
        return repository.findUserByUserName(username).orElseThrow(() -> new UserNotFoundException("User does not exist"));
    }

    @Override
    public String deleteUserAccount(DeleteAccountRequest deleteAccountRequest) {

        User user = repository.findUserByUserName(deleteAccountRequest.getUsername()).orElseThrow(() -> new UserNotFoundException("User does not exist"));
        if(user.isLoggedIn()) {
            if ((deleteAccountRequest.getPassword().equals(deleteAccountRequest.getConfirmPassword()))) {
                if (user.getPassword().equals(deleteAccountRequest.getPassword())) {
                    if (user.getEmailAddress().equals(deleteAccountRequest.getEmailAddress())) {
                        repository.deleteByUserName(deleteAccountRequest.getUsername());
                        return "User Account successfully Deleted";

                    }throw  new IncorrectDetailsException("Incorrect Email address");
                }throw new IncorrectDetailsException("Incorrect Password");
            }
            throw new UnMatchingDetailsException("Password Mismatch!!! Failed To Delete");
        } return "You must be logged in";
    }

    @Override
    public String userLogin(LoginRequest loginRequest) {
        User user = repository.findUserByUserName(loginRequest.getUserName()).orElseThrow(() -> new UserNotFoundException("Account not found"));
        if (!loginRequest.getPassword().equals(user.getPassword()))
            throw new IncorrectDetailsException("Incorrect Login details");
        user.setLoggedIn(true);
        repository.save(user);
        return "Successfully Logged In";
    }

    @Override
    public String userLogout(String username) {
        User user = repository.findUserByUserName(username).orElseThrow(() -> new UserNotFoundException("You must have an account"));
        if (user.isLoggedIn()) {
            user.setLoggedIn(false);
            repository.save(user);
            return "Successfully logged out";}
        return "You must be logged in";}

}
