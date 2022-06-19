package africa.semicolon.PasswordManager.services;

import africa.semicolon.PasswordManager.datas.models.Url;
import africa.semicolon.PasswordManager.dtos.requests.*;
import africa.semicolon.PasswordManager.dtos.responses.UpdateResponse;
import africa.semicolon.PasswordManager.dtos.responses.UrlResponse;

import java.util.List;

public interface UrlService {
    UrlResponse addNewUrl(UrlRequest newRequest, String username);
    List<Url> searchUrlByUrlAddress(String userUserName, String address);
    List<Url> searchUrlByUrlUsername(String userUserName,String urlUserName);
    List<Url> findAllUrl(String userUsername);
    UpdateResponse updateUrlDetails(UrlUpdateRequest urlUpdateRequest,String urlId);
    String deleteUrl(UrlDeleteRequest urlDeleteRequest, String urlId);



}
