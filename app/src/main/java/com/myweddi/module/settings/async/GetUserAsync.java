package com.myweddi.module.settings.async;

import android.os.AsyncTask;

import com.myweddi.model.User;
import com.myweddi.settings.Settings;
import com.myweddi.utils.RequestUtils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class GetUserAsync extends AsyncTask<Void, Void, User> {

    @Override
    protected User doInBackground(Void... voids) {
        RequestUtils requestUtils = new RequestUtils();
        RestTemplate restTemplate = requestUtils.getRestTemplate();
        HttpHeaders requestHeaders = requestUtils.getRequestHeaders();

        String path = Settings.server_url + "/api/gift";
        User user;
        try {
            ResponseEntity<User> response = restTemplate.exchange(path, HttpMethod.GET, new HttpEntity<>(requestHeaders), User.class);
            user = response.getBody();
        }catch (HttpClientErrorException e){
            return null;
        }

        return user;
    }
}
