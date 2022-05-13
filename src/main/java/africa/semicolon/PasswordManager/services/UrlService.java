package africa.semicolon.PasswordManager.services;

import africa.semicolon.PasswordManager.datas.models.Url;
import africa.semicolon.PasswordManager.datas.models.User;
import africa.semicolon.PasswordManager.dtos.requests.*;
import africa.semicolon.PasswordManager.dtos.responses.UpdateResponse;
import africa.semicolon.PasswordManager.dtos.responses.UrlResponse;
import africa.semicolon.PasswordManager.dtos.responses.UserResponse;

import java.util.List;
import java.util.Set;

public interface UrlService {
    UrlResponse addNewUrl(UrlRequest newRequest, String username);
    Url searchUrlByUrlAddress(String address);
    Set<Url> searchUrlByUrlUsername(String userUserName,String urlUserName);
    Set<Url> findAllUrl(String userUsername);
    UpdateResponse updateUrlDetails(UrlUpdateRequest urlUpdateRequest);
    void deleteUrl(UrlDeleteRequest newRequest);



}
