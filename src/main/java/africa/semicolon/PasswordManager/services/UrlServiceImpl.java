package africa.semicolon.PasswordManager.services;

import africa.semicolon.PasswordManager.datas.models.Url;
import africa.semicolon.PasswordManager.datas.models.User;
import africa.semicolon.PasswordManager.datas.repositories.UrlRepository;
import africa.semicolon.PasswordManager.datas.repositories.UserRepository;
import africa.semicolon.PasswordManager.dtos.requests.*;
import africa.semicolon.PasswordManager.dtos.responses.UpdateResponse;
import africa.semicolon.PasswordManager.dtos.responses.UrlResponse;

import africa.semicolon.PasswordManager.exceptions.IncorrectDetailsException;
import africa.semicolon.PasswordManager.exceptions.UrlDoesNotExistException;
import africa.semicolon.PasswordManager.exceptions.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UrlServiceImpl implements UrlService{
    private ModelMapper modelMapper = new ModelMapper();
    @Autowired
    private UrlRepository urlRepository;
    @Autowired
    private UserRepository userRepository;


    @Override
    public UrlResponse addNewUrl(UrlRequest newRequest, String username) {
        User user = userRepository.findUserByUserName(username).orElseThrow(()-> new UserNotFoundException("User with this Username does not exist"));
        if(user.isLoggedIn()) {
            Url url = new Url();
            modelMapper.map(newRequest, url);
            url.setUrlOwner(user.getUserName());
            urlRepository.save(url);
            UrlResponse urlResponse = new UrlResponse();
            modelMapper.map(url, urlResponse);
            user.getUrls().add(url);
            userRepository.save(user);
            return urlResponse;
        }else throw new IncorrectDetailsException("You are not logged In");

    }

    @Override
    public List<Url> searchUrlByUrlAddress(String userUserName,String address) {
        User user = userRepository.findUserByUserName(userUserName).orElseThrow(()-> new UserNotFoundException("User not found"));
        if (user.isLoggedIn()) {
            List<Url> foundUrls = new ArrayList<>();
            List<Url> urls = user.getUrls();
            for (Url url:urls
            ) {
                if(url.getUrlAddress().equalsIgnoreCase(address)) foundUrls.add(url);
            }
            if(foundUrls.isEmpty()) throw new UrlDoesNotExistException("No url found");
            return foundUrls;
        }else throw new IncorrectDetailsException("You are not logged in");
    }

    @Override
    public List<Url> searchUrlByUrlUsername(String userUserName, String urlUsername) {
        User user = userRepository.findUserByUserName(userUserName).orElseThrow(()-> new UserNotFoundException("User not found"));
        if (user.isLoggedIn()) {
            List<Url> searchedUrls = new ArrayList<>();
            List<Url> urls = user.getUrls();
            for (Url url:urls
                 ) {
                if(url.getUsername().equalsIgnoreCase(urlUsername)) searchedUrls.add(url);
            }
            if(searchedUrls.isEmpty()) throw new UrlDoesNotExistException("No url with this username found");
            return searchedUrls;
        }else throw new IncorrectDetailsException("You are not logged in");
    }

    @Override
    public List<Url> findAllUrl(String userUserName) {
        User user = userRepository.findUserByUserName(userUserName).orElseThrow(() -> new UserNotFoundException("User not found"));
        if (user.isLoggedIn()) {
            return user.getUrls();

        } else throw new IncorrectDetailsException("You are not logged in");
    }
    @Override
    public UpdateResponse updateUrlDetails(UrlUpdateRequest urlUpdateRequest, String urlId) {
        Url url = urlRepository.findById(urlId).orElseThrow(()-> new UrlDoesNotExistException("Url not found"));
           String urlOwner = url.getUrlOwner();
           User foundUser = userRepository.findUserByUserName(urlOwner).orElseThrow(()-> new UserNotFoundException("User does not exist"));

           if(urlOwner.equalsIgnoreCase(foundUser.getUserName())){
               if(foundUser.isLoggedIn()){
                    if (!(urlUpdateRequest.getUsername().trim().equals("") || urlUpdateRequest.getUsername() == null)) {
                        url.setUsername(urlUpdateRequest.getUsername());
                    }
                   if (!(urlUpdateRequest.getUrlAddress().trim().equals("") || urlUpdateRequest.getUrlAddress() == null)) {
                       url.setUrlAddress(urlUpdateRequest.getUrlAddress());
                   }
                   if (!(urlUpdateRequest.getEmailAddress().trim().equals("") || urlUpdateRequest.getUsername() == null)) {
                       url.setEmailAddress(urlUpdateRequest.getEmailAddress());
                   }
                   if (!(urlUpdateRequest.getPassword().trim().equals("") || urlUpdateRequest.getPassword() == null)) {
                       url.setPassword(urlUpdateRequest.getPassword());
                   }
                   if (!(urlUpdateRequest.getFirstName().trim().equals("") || urlUpdateRequest.getFirstName() == null)) {
                        url.setFirstName(urlUpdateRequest.getFirstName());
                    }
                    if (!(urlUpdateRequest.getLastName().trim().equals("") || urlUpdateRequest.getLastName() == null)) {
                        url.setLastName(urlUpdateRequest.getLastName());
                    }
                } else {
                    throw new IncorrectDetailsException("You must be logged in");
                }

               }
           urlRepository.save(url);
           List<Url> urls = foundUser.getUrls();
        for (Url userUrl:urls) {
            if (userUrl.getId().equals(url.getId())){
                modelMapper.map(url,userUrl);
                break;
            }
        }
           userRepository.save(foundUser);
           UpdateResponse urlUpdateResponse = new UpdateResponse();
           modelMapper.map(url,urlUpdateResponse);
           urlUpdateResponse.setMessage("Update Successful");
           return urlUpdateResponse;
    }

    @Override
    public String deleteUrl(UrlDeleteRequest urlDeleteRequest, String urlId) {
        Url url = urlRepository.findUrlByUrlAddress(urlDeleteRequest.getUrlAddress()).orElseThrow(()-> new UrlDoesNotExistException("Url does not exist"));
        User user = userRepository.findUserByUserName(url.getUrlOwner()).get();
        if(!(urlDeleteRequest.getUserPassword().equals(user.getPassword())))
            throw new IncorrectDetailsException("Incorrect Password!! Failed to delete");
        user.getUrls().remove(url);
        userRepository.save(user);
        urlRepository.delete(url);
    return "Delete Successful";

    }
}
