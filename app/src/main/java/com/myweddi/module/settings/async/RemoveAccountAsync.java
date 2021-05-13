package com.myweddi.module.settings.async;

import android.os.AsyncTask;

import com.myweddi.settings.Settings;
import com.myweddi.utils.RequestUtils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.Map;

public class RemoveAccountAsync extends AsyncTask<Void, Void, Boolean> {
    @Override
    protected Boolean doInBackground(Void... voids) {
        RequestUtils requestUtils = new RequestUtils();
        RestTemplate restTemplate = requestUtils.getRestTemplate();
        HttpHeaders requestHeaders = requestUtils.getRequestHeaders();

        String path = Settings.server_url + "/api/settings";
        Map<String, Long> body = new LinkedHashMap<>();
        body.put("userid", Settings.user.getId());

        if(Settings.user.getRole().equals("GUEST")){
            path += "/removeguest";
        }else if(Settings.user.getRole().equals("HOST")){
            path += "/removewedding";
            body.put("weddingid", Settings.user.getId());
        }else {
            //TODO in future
        }

        try {
            restTemplate.exchange(path,
                    HttpMethod.POST,
                    new HttpEntity<>(body, requestHeaders),
                    Void.class);
        }catch (HttpClientErrorException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
