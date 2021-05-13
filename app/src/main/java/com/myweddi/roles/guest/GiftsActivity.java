package com.myweddi.roles.guest;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myweddi.R;
import com.myweddi.module.gift.GiftType;
import com.myweddi.module.gift.model.Gift;
import com.myweddi.module.gift.model.GiftWrapper;
import com.myweddi.roles.host.EditInfoActivity;
import com.myweddi.settings.Settings;
import com.myweddi.module.gift.GiftAdapter;
import com.myweddi.utils.ListUtils;
import com.myweddi.utils.MenuHandler;
import com.myweddi.utils.Utils;
import com.myweddi.utils.RequestUtils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GiftsActivity extends AppCompatActivity {

    private TextView giftInfo;
    private LinearLayout gift_main_layout, small_gift_layout;
    private ImageView giftsIco, arrowIco2, moneyIco, flowerIco, arrowIco, WineIco, BookIco, pottedFlowerIco, lotteryIco,
            fooderIco, charityIco;
    private TextView main_gift_label, flower_label, wine_label, book_label,
            potted_flower_label, lottery_label, fooder_label, charity_label;

    private void setSmallGift(GiftWrapper giftWrapper){
        Map<GiftType, Boolean> selectedGift = giftWrapper.getSelectedGift();

        for(Map.Entry<GiftType, Boolean> gift : selectedGift.entrySet()){
            if(!gift.getValue())
                continue;

            small_gift_layout.setVisibility(View.VISIBLE);
            flower_label.setVisibility(View.VISIBLE);
            switch (gift.getKey()){
                case WINE:
                    WineIco.setVisibility(View.VISIBLE);
                    wine_label.setVisibility(View.VISIBLE);
                    break;
                case BOOKS:
                    BookIco.setVisibility(View.VISIBLE);
                    book_label.setVisibility(View.VISIBLE);
                    break;
                case POTTEDFLOWERS:
                    pottedFlowerIco.setVisibility(View.VISIBLE);
                    potted_flower_label.setVisibility(View.VISIBLE);
                    break;
                case FOODER:
                    fooderIco.setVisibility(View.VISIBLE);
                    fooder_label.setVisibility(View.VISIBLE);
                    break;
                case CHARITY:
                    charityIco.setVisibility(View.VISIBLE);
                    charity_label.setVisibility(View.VISIBLE);
                    break;
                case LOTTERY:
                    lotteryIco.setVisibility(View.VISIBLE);
                    lottery_label.setVisibility(View.VISIBLE);
            }
        }

        if(selectedGift.get(GiftType.GIFTS)){
            gift_main_layout.setVisibility(View.VISIBLE);
        }else if(selectedGift.get(GiftType.MONEY)){
            gift_main_layout.setVisibility(View.VISIBLE);
            arrowIco2.setVisibility(View.VISIBLE);
            moneyIco.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gifts);
        setTitle("");

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);

        Utils.setProfilePhoto(this, GiftsActivity.this);
        init();

        giftInfo = findViewById(R.id.giftInfo);
        GiftAsync giftAsync = new GiftAsync(this);
        giftAsync.execute();
    }

    private void setMainGift(GiftWrapper giftWrapper){
        Map<GiftType, Boolean> selectedGift = giftWrapper.getSelectedGift();

        if(selectedGift.get(GiftType.GIFTS)){
            gift_main_layout.setVisibility(View.VISIBLE);
            main_gift_label.setVisibility(View.VISIBLE);
        }else if(selectedGift.get(GiftType.MONEY)){
            main_gift_label.setVisibility(View.VISIBLE);
            gift_main_layout.setVisibility(View.VISIBLE);
            arrowIco2.setVisibility(View.VISIBLE);
            moneyIco.setVisibility(View.VISIBLE);
        }
    }

    private void init(){
        gift_main_layout = findViewById(R.id.gift_main_layout);
        small_gift_layout = findViewById(R.id.small_gift_layout);
        giftsIco = findViewById(R.id.giftsIco);
        arrowIco2 = findViewById(R.id.arrowIco2);
        moneyIco = findViewById(R.id.moneyIco);
        flowerIco = findViewById(R.id.flowerIco);
        arrowIco = findViewById(R.id.arrowIco);
        WineIco = findViewById(R.id.WineIco);
        BookIco = findViewById(R.id.BookIco);
        pottedFlowerIco = findViewById(R.id.pottedFlowerIco);
        lotteryIco = findViewById(R.id.lotteryIco);
        fooderIco = findViewById(R.id.fooderIco);
        charityIco = findViewById(R.id.charityIco);

        main_gift_label = findViewById(R.id.main_gift_label);
        flower_label = findViewById(R.id.flower_label);
        wine_label = findViewById(R.id.wine_label);
        book_label = findViewById(R.id.book_label);
        potted_flower_label = findViewById(R.id.potted_flower_label);
        lottery_label = findViewById(R.id.lottery_label);
        fooder_label = findViewById(R.id.fooder_label);
        charity_label = findViewById(R.id.charity_label);
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



    private static class GiftAsync extends AsyncTask<Void,  Void, GiftWrapper> {

        private final WeakReference<GiftsActivity> activityReference;

        public GiftAsync(GiftsActivity context) {
            this.activityReference = new WeakReference<>(context);
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
            GiftsActivity context = activityReference.get();

            context.giftInfo.setText(giftWrapper.getGiftInfo());
            context.setMainGift(giftWrapper);
            context.setSmallGift(giftWrapper);

            ListView listView = (ListView) context.findViewById(R.id.giftListView);
            GiftAdapter giftAdapter = new GiftAdapter(context, giftWrapper, getTitles(giftWrapper));
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