package africa.semicolon.PasswordManager.controllers;

import africa.semicolon.PasswordManager.dtos.requests.AccountRequest;
import africa.semicolon.PasswordManager.dtos.requests.LoginRequest;
import africa.semicolon.PasswordManager.dtos.requests.PasswordUpdateRequest;
import africa.semicolon.PasswordManager.dtos.requests.UpdateAccountRequest;
import africa.semicolon.PasswordManager.dtos.responses.ApiResponse;
import africa.semicolon.PasswordManager.exceptions.AlreadyExistException;
import africa.semicolon.PasswordManager.exceptions.IncorrectDetailsException;
import africa.semicolon.PasswordManager.exceptions.PasswordManagerException;
import africa.semicolon.PasswordManager.exceptions.UserNotFoundException;
import africa.semicolon.PasswordManager.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/users")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/create")

    public ResponseEntity<?> createUser(@RequestBody AccountRequest request) {
        try {
            return new ResponseEntity<>(userService.createNewUserAccount(request), HttpStatus.CREATED);
        } catch (PasswordManagerException error) {
            return new ResponseEntity<>(new ApiResponse(false, error.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/search/{username}")
    public ResponseEntity<?> findUserByUsername(@PathVariable String username){
        try{
            return new ResponseEntity<>(userService.findUserAccountByUsername(username),HttpStatus.FOUND);
        }catch (UserNotFoundException error){
            return new ResponseEntity<>(new ApiResponse(false,error.getMessage()),HttpStatus.BAD_REQUEST);
        }
    }


    @PatchMapping("/update/{username}")
    public ResponseEntity<?> updateUserDetails(@RequestBody UpdateAccountRequest updateAccountRequest, @PathVariable String username) {
        try{
            return new ResponseEntity<>(userService.updateUserAccountDetails(updateAccountRequest, username),HttpStatus.CREATED);
        }catch (AlreadyExistException | IncorrectDetailsException error){
            return new ResponseEntity<>(new ApiResponse(false,error.getMessage()),HttpStatus.BAD_REQUEST);
        }

    }





}
