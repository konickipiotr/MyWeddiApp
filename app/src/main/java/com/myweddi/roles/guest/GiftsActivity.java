package com.myweddi.roles.guest;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myweddi.R;
import com.myweddi.module.gift.model.Gift;
import com.myweddi.module.gift.model.GiftWrapper;
import com.myweddi.settings.Settings;
import com.myweddi.module.gift.GiftAdapter;
import com.myweddi.utils.ListUtils;
import com.myweddi.utils.MenuHandler;
import com.myweddi.utils.OtherUtils;
import com.myweddi.utils.RequestUtils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class GiftsActivity extends AppCompatActivity {
    private ImageView myProfilPhoto;
    private TextView giftInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gifts);
        setTitle("");

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);

        OtherUtils.setProfilePhoto(myProfilPhoto, this, GiftsActivity.this);

        giftInfo = (TextView) findViewById(R.id.giftInfo);
        GiftAsync giftAsync = new GiftAsync();
        giftAsync.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean menu = MenuHandler.menu(item, this, GiftsActivity.this);
        if(!menu)
            return super.onOptionsItemSelected(item);
        return menu;
    }

    class GiftAsync extends AsyncTask<Void,  Void, GiftWrapper> {

        @Override
        protected GiftWrapper doInBackground(Void... voids) {
            RequestUtils requestUtils = new RequestUtils();
            RestTemplate restTemplate = requestUtils.getRestTemplate();
            HttpHeaders requestHeaders = requestUtils.getRequestHeaders();

            String path = Settings.server_url + "/api/gift";
            ResponseEntity<GiftWrapper> response = restTemplate.exchange(
                    path,
                    HttpMethod.GET,
                    new HttpEntity<Object>(requestHeaders),
                    GiftWrapper.class);

            ObjectMapper mapper = new ObjectMapper();
            GiftWrapper giftWrapper = mapper.convertValue(response.getBody(), new TypeReference<GiftWrapper>() {});
            return  giftWrapper;
        }

        @Override
        protected void onPostExecute(GiftWrapper giftWrapper) {

            giftInfo.setText(giftWrapper.getGiftInfo());

            ListView listView = (ListView) findViewById(R.id.giftListView);
            GiftAdapter giftAdapter = new GiftAdapter(GiftsActivity.this, giftWrapper, getTitles(giftWrapper));
            listView.setAdapter(giftAdapter);

            ListUtils.setDynamicHeight(listView);
        }

        private List<String> getTitles(GiftWrapper giftWrapper){
            List<String> titles = new ArrayList<>();

            for(Gift g : giftWrapper.getGifts()){
                titles.add(g.getUsername());
            }
            return titles;
        }
    }
}