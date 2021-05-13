package com.myweddi.module.settings.async;

import android.os.AsyncTask;

import com.myweddi.settings.Settings;
import com.myweddi.utils.RequestUtils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.TreeMap;

public class ChangeUserPasswordAsync extends AsyncTask<String, Void, Boolean> {

    @Override
    protected Boolean doInBackground(String... params) {
        RequestUtils requestUtils = new RequestUtils();
        RestTemplate restTemplate = requestUtils.getRestTemplate();
        HttpHeaders requestHeaders = requestUtils.getRequestHeaders();
        String newPassword = params[0];

        String path = Settings.server_url + "/api/settings/password";
        Map<String, String> body = new TreeMap<>();
        body.put("newPassword", newPassword);
        try {
            restTemplate.exchange(path, HttpMethod.POST, new HttpEntity<>(body, requestHeaders), Void.class);
        }catch (HttpClientErrorException e){
            return false;
        }
        return true;
    }
}