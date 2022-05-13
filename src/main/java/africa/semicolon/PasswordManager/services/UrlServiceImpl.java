package africa.semicolon.PasswordManager.services;

import africa.semicolon.PasswordManager.datas.models.Url;
import africa.semicolon.PasswordManager.datas.models.User;
import africa.semicolon.PasswordManager.datas.repositories.UrlRepository;
import africa.semicolon.PasswordManager.datas.repositories.UserRepository;
import africa.semicolon.PasswordManager.dtos.requests.*;
import africa.semicolon.PasswordManager.dtos.responses.UpdateResponse;
import africa.semicolon.PasswordManager.dtos.responses.UrlResponse;

import africa.semicolon.PasswordManager.dtos.responses.UserResponse;
import africa.semicolon.PasswordManager.exceptions.IncorrectDetailsException;
import africa.semicolon.PasswordManager.exceptions.UrlDoesNotExistException;
import africa.semicolon.PasswordManager.exceptions.UserNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;

@Service
public class UrlServiceImpl implements UrlService{
    private ModelMapper modelMapper = new ModelMapper();
    @Autowired
    private UserRepository repository;
    @Autowired
    private UrlRepository urlRepository;
    @Autowired
    private UserRepository userRepository;


    @Override
    public UrlResponse addNewUrl(UrlRequest newRequest, String username) {
        User user = repository.findUserByUserName(username).orElseThrow(()-> new UserNotFoundException("User with this Username does not exist"));
        if(user.isLoggedIn()) {
            Url url = new Url();
            modelMapper.map(newRequest, url);
            UserResponse userResponse = new UserResponse();
            modelMapper.map(user,userResponse);
            url.setUrlOwner(userResponse);
            urlRepository.save(url);
            UrlResponse urlResponse = new UrlResponse();
            modelMapper.map(url, urlResponse);
            user.getUrls().add(url);
            repository.save(user);
            return urlResponse;
        }else throw new IncorrectDetailsException("You are not logged In");

    }

    @Override
    public Url searchUrlByUrlAddress(String address) {
        return null;
    }

    @Override
    public Set<Url> searchUrlByUrlUsername(String userUserName, String urlUsername) {
        User user = userRepository.findUserByUserName(userUserName).orElseThrow(()-> new UserNotFoundException("User not found"));
        if (user.isLoggedIn()) {
            Set<Url> searchedUrls = new HashSet<>();
            Set<Url> urls = user.getUrls();
            for (Url url:urls
                 ) {
                if(url.getUsername().equalsIgnoreCase(urlUsername)) searchedUrls.add(url);
            }
            if(searchedUrls.isEmpty()) throw new UrlDoesNotExistException("No url with this username found");
            return searchedUrls;
        }else throw new IncorrectDetailsException("You are not logged in");
    }

    @Override
    public Set<Url> findAllUrl(String userUserName) {
        User user = userRepository.findUserByUserName(userUserName).orElseThrow(() -> new UserNotFoundException("User not found"));
        if (user.isLoggedIn()) {
            return user.getUrls();

        } else throw new IncorrectDetailsException("You are not logged in");
    }
    @Override
    public UpdateResponse updateUrlDetails(UrlUpdateRequest urlUpdateRequest) {
        return null;
    }


    @Override
    public void deleteUrl(UrlDeleteRequest newRequest) {

    }
}
