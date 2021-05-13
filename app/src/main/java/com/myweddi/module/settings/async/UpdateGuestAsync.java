package com.myweddi.module.settings.async;

import android.os.AsyncTask;

import com.myweddi.model.Guest;
import com.myweddi.settings.Settings;
import com.myweddi.utils.RequestUtils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class UpdateGuestAsync  extends AsyncTask<Void, Void, Boolean>{

    @Override
    protected Boolean doInBackground(Void... params) {
        RequestUtils requestUtils = new RequestUtils();
        RestTemplate restTemplate = requestUtils.getRestTemplate();
        HttpHeaders requestHeaders = requestUtils.getRequestHeaders();
        String path = Settings.server_url + "/api/settings/updateguest";

        Guest guest = Settings.guest;
        try{
            restTemplate.exchange(path,
                    HttpMethod.POST,
                    new HttpEntity<>(Settings.guest, requestHeaders),
                    Void.class);
            return true;
        }catch (HttpClientErrorException e){
            e.printStackTrace();
            return false;
        }

    }


}