package com.myweddi.module.gift.listeners;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;

import com.myweddi.roles.host.HostGiftActivity;
import com.myweddi.settings.Settings;
import com.myweddi.utils.RequestUtils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

public class RemoveGiftListener implements View.OnClickListener {

    private Long giftid;
    private Activity context;

    public RemoveGiftListener(Long giftid, Activity context) {
        this.giftid = giftid;
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        new RemoveCustomGiftAsync().execute();
        context.finish();
        context.startActivity(new Intent(context, HostGiftActivity.class));
    }

    class RemoveCustomGiftAsync extends AsyncTask<Void,  Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            RequestUtils requestUtils = new RequestUtils();
            RestTemplate restTemplate = requestUtils.getRestTemplate();
            HttpHeaders requestHeaders = requestUtils.getRequestHeaders();

            String path = Settings.server_url + "/api/gift/remove";
            restTemplate.exchange(
                    path,
                    HttpMethod.POST,
                    new HttpEntity<Object>(giftid, requestHeaders),
                    Void.class);
            return null;
        }
    }
}
