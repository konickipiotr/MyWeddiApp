package com.myweddi.roles.host;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myweddi.R;
import com.myweddi.module.gift.CustomGiftListAdapter;
import com.myweddi.module.gift.GiftType;
import com.myweddi.module.gift.model.GiftIn;
import com.myweddi.module.gift.model.GiftWrapper;
import com.myweddi.roles.Home;
import com.myweddi.settings.Settings;
import com.myweddi.utils.ListUtils;
import com.myweddi.utils.MenuHandler;
import com.myweddi.utils.RequestUtils;
import com.myweddi.utils.Utils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.lang.ref.WeakReference;
import java.util.Map;

public class HostGiftActivity extends AppCompatActivity {

    private GiftWrapper giftWrapper;
    private Map<GiftType, Boolean> gifts;
    private GiftIn giftIn = new GiftIn();

    private ImageButton bhost_gift_gifts, bhost_gift_money, bhost_gift_pottedflower, bhost_gift_fooder,
            bhost_gift_wine, bhost_gift_lottery, bhost_gift_book, bhost_gift_charity;
    private EditText host_gift_info;
    private Button bhost_gift_save, bhost_gift_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_gift);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);

        Utils.setProfilePhoto(this, HostGiftActivity.this);

        init();
        initListeners();
        new HostGiftFetcherAsync(this).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean menu = MenuHandler.menu(item, this, HostGiftActivity.this);
        if(!menu)
            return super.onOptionsItemSelected(item);
        return menu;
    }

    private void init(){
        bhost_gift_gifts = findViewById(R.id.bhost_gift_gifts);
        bhost_gift_money = findViewById(R.id.bhost_gift_money);
        bhost_gift_pottedflower = findViewById(R.id.bhost_gift_pottedflower);
        bhost_gift_fooder = findViewById(R.id.bhost_gift_fooder);
        bhost_gift_wine = findViewById(R.id.bhost_gift_wine);
        bhost_gift_lottery = findViewById(R.id.bhost_gift_lottery);
        bhost_gift_book = findViewById(R.id.bhost_gift_book);
        bhost_gift_charity = findViewById(R.id.bhost_gift_charity);

        host_gift_info = findViewById(R.id.host_gift_info);

        bhost_gift_save = findViewById(R.id.bhost_gift_save);
        bhost_gift_add = findViewById(R.id.bhost_gift_add);
    }

    private void initListeners(){
        bhost_gift_gifts.setOnClickListener(v ->{
            if(giftIn.isGIFTS()){
                giftIn.setGIFTS(false);
                bhost_gift_gifts.setBackgroundTintList(getResources().getColorStateList(R.color.white));
            }else {
                giftIn.setGIFTS(true);
                giftIn.setMONEY(false);
                bhost_gift_money.setBackgroundTintList(getResources().getColorStateList(R.color.white));
                bhost_gift_gifts.setBackgroundTintList(getResources().getColorStateList(R.color.selectedbtnColor));
            }
        });

        bhost_gift_money.setOnClickListener(v ->{
            if(giftIn.isMONEY()){
                giftIn.setMONEY(false);
                bhost_gift_money.setBackgroundTintList(getResources().getColorStateList(R.color.white));
            }else {
                giftIn.setGIFTS(false);
                giftIn.setMONEY(true);
                bhost_gift_money.setBackgroundTintList(getResources().getColorStateList(R.color.selectedbtnColor));
                bhost_gift_gifts.setBackgroundTintList(getResources().getColorStateList(R.color.white));
            }
        });

        bhost_gift_pottedflower.setOnClickListener(v ->{
            if(giftIn.isPOTTEDFLOWERS()){
                giftIn.setPOTTEDFLOWERS(false);
                bhost_gift_pottedflower.setBackgroundTintList(getResources().getColorStateList(R.color.white));
            }else {
                giftIn.setPOTTEDFLOWERS(true);
                bhost_gift_pottedflower.setBackgroundTintList(getResources().getColorStateList(R.color.selectedbtnColor));
            }
        });

        bhost_gift_fooder.setOnClickListener(v ->{
            if(giftIn.isFOODER()){
                giftIn.setFOODER(false);
                bhost_gift_fooder.setBackgroundTintList(getResources().getColorStateList(R.color.white));
            }else {
                giftIn.setFOODER(true);
                bhost_gift_fooder.setBackgroundTintList(getResources().getColorStateList(R.color.selectedbtnColor));
            }
        });

        bhost_gift_wine.setOnClickListener(v ->{
            if(giftIn.isWINE()){
                giftIn.setWINE(false);
                bhost_gift_wine.setBackgroundTintList(getResources().getColorStateList(R.color.white));
            }else {
                giftIn.setWINE(true);
                bhost_gift_wine.setBackgroundTintList(getResources().getColorStateList(R.color.selectedbtnColor));
            }
        });

        bhost_gift_lottery.setOnClickListener(v ->{
            if(giftIn.isLOTTERY()){
                giftIn.setLOTTERY(false);
                bhost_gift_lottery.setBackgroundTintList(getResources().getColorStateList(R.color.white));
            }else {
                giftIn.setLOTTERY(true);
                bhost_gift_lottery.setBackgroundTintList(getResources().getColorStateList(R.color.selectedbtnColor));
            }
        });

        bhost_gift_book.setOnClickListener(v ->{
            if(giftIn.isBOOKS()){
                giftIn.setBOOKS(false);
                bhost_gift_book.setBackgroundTintList(getResources().getColorStateList(R.color.white));
            }else {
                giftIn.setBOOKS(true);
                bhost_gift_book.setBackgroundTintList(getResources().getColorStateList(R.color.selectedbtnColor));
            }
        });

        bhost_gift_charity.setOnClickListener(v ->{
            if(giftIn.isCHARITY()){
                giftIn.setCHARITY(false);
                bhost_gift_charity.setBackgroundTintList(getResources().getColorStateList(R.color.white));
            }else {
                giftIn.setCHARITY(true);
                bhost_gift_charity.setBackgroundTintList(getResources().getColorStateList(R.color.selectedbtnColor));
            }
        });

        bhost_gift_save.setOnClickListener(v -> {
            giftIn.setGiftInfo(host_gift_info.getText().toString());
            new HostGiftSaveAsync(this).execute();
        });

        bhost_gift_add.setOnClickListener(v -> {
            String text = host_gift_info.getText().toString();
            if(text.isEmpty())
                return;

            new HostGiftSaveCustomGiftAsync(this).execute(text);
        });

    }

    private void setGiftHightlight(GiftType type){
        ImageButton result;
        switch(type){
            case MONEY: result = this.bhost_gift_money; break;
            case GIFTS: result = this.bhost_gift_gifts; break;
            case POTTEDFLOWERS: result = this.bhost_gift_pottedflower; break;
            case WINE: result = this.bhost_gift_wine; break;
            case BOOKS: result = this.bhost_gift_book; break;
            case FOODER: result = this.bhost_gift_fooder; break;
            case LOTTERY: result = this.bhost_gift_lottery; break;
            case CHARITY: result = this.bhost_gift_charity; break;
            default: return;
        }
        result.setBackgroundTintList(getResources().getColorStateList(R.color.selectedbtnColor));
    }


    class HostGiftFetcherAsync extends AsyncTask<Void,  Void, GiftWrapper> {

        private WeakReference<HostGiftActivity>  activityWeakReference;

        public HostGiftFetcherAsync(HostGiftActivity context) {
            this.activityWeakReference = new WeakReference<>(context);
        }

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
            HostGiftActivity context = activityWeakReference.get();
            context.giftWrapper = giftWrapper;
            context.gifts = giftWrapper.getSelectedGift();

            Map<GiftType, Boolean> selectedGift = giftWrapper.getSelectedGift();

            for(Map.Entry<GiftType, Boolean> v : selectedGift.entrySet()){
                if(v.getValue())
                    context.setGiftHightlight(v.getKey());
            }

            context.giftIn.mapToGift(selectedGift);
            context.host_gift_info.setText(giftWrapper.getGiftInfo());


            ListView listView = (ListView) findViewById(R.id.host_gift_listView);
            CustomGiftListAdapter customGiftListAdapter = new CustomGiftListAdapter(HostGiftActivity.this, giftWrapper.getGifts());
            listView.setAdapter(customGiftListAdapter);
            ListUtils.setDynamicHeight(listView);
        }
    }


    class HostGiftSaveAsync extends AsyncTask<Void,  Void, Void> {

        private WeakReference<HostGiftActivity>  activityWeakReference;

        public HostGiftSaveAsync(HostGiftActivity context) {
            this.activityWeakReference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HostGiftActivity context = activityWeakReference.get();
            RequestUtils requestUtils = new RequestUtils();
            RestTemplate restTemplate = requestUtils.getRestTemplate();
            HttpHeaders requestHeaders = requestUtils.getRequestHeaders();

            String path = Settings.server_url + "/api/gift";
            restTemplate.exchange(
                    path,
                    HttpMethod.POST,
                    new HttpEntity<Object>(context.giftIn, requestHeaders),
                    Void.class);
            return null;
        }
    }

    class HostGiftSaveCustomGiftAsync extends AsyncTask<String,  Void, Void> {

        private WeakReference<HostGiftActivity> activityWeakReference;

        public HostGiftSaveCustomGiftAsync(HostGiftActivity context) {
            this.activityWeakReference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(String... params) {
            HostGiftActivity context = activityWeakReference.get();
            RequestUtils requestUtils = new RequestUtils();
            RestTemplate restTemplate = requestUtils.getRestTemplate();
            HttpHeaders requestHeaders = requestUtils.getRequestHeaders();

            String path = Settings.server_url + "/api/gift/add";
            restTemplate.exchange(
                    path,
                    HttpMethod.POST,
                    new HttpEntity<Object>(params[0], requestHeaders),
                    Void.class);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            HostGiftActivity context = activityWeakReference.get();
            context.finish();
            context.startActivity(new Intent(context, HostGiftActivity.class));
        }
    }
}
