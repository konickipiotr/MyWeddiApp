package com.myweddi.module.gift.listeners;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;

import com.myweddi.module.gift.GiftAction;
import com.myweddi.settings.Settings;
import com.myweddi.utils.RequestUtils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class BookGiftListener implements View.OnClickListener {

    private Long giftid;
    private Activity context;
    private GiftAction giftAction;

    public BookGiftListener(Long giftid, Activity context, GiftAction giftAction) {
        this.giftid = giftid;
        this.context = context;
        this.giftAction = giftAction;
    }

    @Override
    public void onClick(View v) {
        new BookGiftAsync().execute(this.giftid);
        context.finish();
        context.startActivity(context.getIntent());
    }

    private class BookGiftAsync extends AsyncTask<Long, Void, Long> {

        @Override
        protected Long doInBackground(Long... params) {
            Long giftid = params[0];
            RequestUtils requestUtils = new RequestUtils();
            RestTemplate restTemplate = requestUtils.getRestTemplate();
            HttpHeaders requestHeaders = requestUtils.getRequestHeaders();

            String path = "";
            if(giftAction.equals(GiftAction.BOOK))
                path = Settings.server_url + "/api/gift/book";
            else
                path = Settings.server_url + "/api/gift/unbook";

            ResponseEntity response = restTemplate.postForEntity(path, new HttpEntity<Long>(giftid, requestHeaders), Void.class);

            return giftid;
        }
    }
}
