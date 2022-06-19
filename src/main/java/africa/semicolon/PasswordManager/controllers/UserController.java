package africa.semicolon.PasswordManager.controllers;

import africa.semicolon.PasswordManager.dtos.requests.*;
import africa.semicolon.PasswordManager.dtos.responses.ApiResponse;
import africa.semicolon.PasswordManager.exceptions.*;
import africa.semicolon.PasswordManager.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000/")
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

    @PostMapping("/login")

    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        try{
           return new ResponseEntity<>(userService.userLogin(loginRequest),HttpStatus.FOUND);
        }catch (UserNotFoundException  | IncorrectDetailsException error){
            return new ResponseEntity<>(new ApiResponse(false, error.getMessage()),HttpStatus.BAD_REQUEST);

        }
    }

    @GetMapping("/logout/{username}")

    public ResponseEntity<?> logout(@PathVariable String username){
        try{
            return new ResponseEntity<>(userService.userLogout(username),HttpStatus.OK);
        }catch (UserNotFoundException  error){
            return new ResponseEntity<>(new ApiResponse(false, error.getMessage()),HttpStatus.BAD_REQUEST);
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

    @PatchMapping("/updatepassword/{username}")
    public ResponseEntity<?> updateUserPassword(@RequestBody PasswordUpdateRequest passwordUpdateRequest, @PathVariable String username) {
        try{
            return new ResponseEntity<>(userService.updateAccountPassword(passwordUpdateRequest, username),HttpStatus.CREATED);
        }catch (UserNotFoundException | UnMatchingDetailsException error){
            return new ResponseEntity<>(new ApiResponse(false,error.getMessage()),HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{username}")
    public ResponseEntity<?> deleteUser(@RequestBody DeleteAccountRequest deleteAccountRequest, @PathVariable String username) {
        try{
            return new ResponseEntity<>(userService.deleteUserAccount(deleteAccountRequest),HttpStatus.CREATED);
        }catch (UserNotFoundException | UnMatchingDetailsException error){
            return new ResponseEntity<>(new ApiResponse(false,error.getMessage()),HttpStatus.BAD_REQUEST);
        }
    }


}
