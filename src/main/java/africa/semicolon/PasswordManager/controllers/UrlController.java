package africa.semicolon.PasswordManager.controllers;

import africa.semicolon.PasswordManager.datas.repositories.UrlRepository;
import africa.semicolon.PasswordManager.dtos.requests.*;
import africa.semicolon.PasswordManager.dtos.responses.ApiResponse;
import africa.semicolon.PasswordManager.exceptions.*;
import africa.semicolon.PasswordManager.services.UrlService;
import africa.semicolon.PasswordManager.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/urls")
public class UrlController {
    @Autowired
    UrlService urlService;
    @Autowired
    UserService userService;


    @PostMapping("/addurl/{username}")
    public ResponseEntity<?> addUrl(@RequestBody UrlRequest urlRequest, @PathVariable String username) {
        try {
            return new ResponseEntity<>(urlService.addNewUrl(urlRequest, username), HttpStatus.CREATED);
        } catch (UserNotFoundException | IncorrectDetailsException error) {
            return new ResponseEntity<>(new ApiResponse(false, error.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/search/{userUsername}")
    public ResponseEntity<?> findUrl(@PathVariable String userUsername, @RequestParam String urlUsername) {
        try {
            return new ResponseEntity<>(urlService.searchUrlByUrlUsername(userUsername, urlUsername), HttpStatus.FOUND);
        } catch (UserNotFoundException | UrlDoesNotExistException | IncorrectDetailsException error) {
            return new ResponseEntity<>(new ApiResponse(false, error.getMessage()), HttpStatus.BAD_REQUEST);

        }
    }

    @GetMapping("/searchall/{userUsername}")
    public ResponseEntity<?> findAllUrl(@PathVariable String userUsername) {
        try {
            return new ResponseEntity<>(urlService.findAllUrl(userUsername), HttpStatus.FOUND);
        } catch (UserNotFoundException | IncorrectDetailsException error) {
            return new ResponseEntity<>(new ApiResponse(false, error.getMessage()), HttpStatus.BAD_REQUEST);

        }

    }

    @PatchMapping("/update/{urlId}")
    public ResponseEntity<?> updateUrlDetails(@RequestBody UrlUpdateRequest urlUpdateRequest, @PathVariable String urlId) {
        try{
            return new ResponseEntity<>(urlService.updateUrlDetails(urlUpdateRequest, urlId),HttpStatus.CREATED);
        }catch (UrlDoesNotExistException | IncorrectDetailsException | UserNotFoundException error){
            return new ResponseEntity<>(new ApiResponse(false,error.getMessage()),HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{urlId}")
    public ResponseEntity<?> deleteUrl(@RequestBody UrlDeleteRequest urlDeleteRequest, @PathVariable String urlId) {
        try{
            return new ResponseEntity<>(urlService.deleteUrl(urlDeleteRequest, urlId),HttpStatus.CREATED);
        }catch (UrlDoesNotExistException | IncorrectDetailsException error){
            return new ResponseEntity<>(new ApiResponse(false,error.getMessage()),HttpStatus.BAD_REQUEST);
        }
    }

}
