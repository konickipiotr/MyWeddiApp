package com.myweddi.roles;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.myweddi.R;
import com.myweddi.model.Host;
import com.myweddi.module.weddinginfo.WeddingInfo;
import com.myweddi.roles.host.EditInfoActivity;
import com.myweddi.settings.Settings;
import com.myweddi.utils.DateUtils;
import com.myweddi.utils.MenuHandler;
import com.myweddi.utils.Utils;
import com.myweddi.utils.RequestUtils;
import com.myweddi.utils.WeddiMap;
import com.squareup.picasso.Picasso;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.lang.ref.WeakReference;

public class WeddingInfoActivity extends AppCompatActivity {

    private ImageView hostsPhoto, churchPhoto, partyHousePhoto;
    private TextView brideName, groomName, bridePhone, groomPhone, churchName, churchAddress, weddingTime,
    partyHouseName, partyHouseAddress;
    private Button edit;
    private FrameLayout info_church_map_fragment, wedding_house_map_fragment;

    private WeddingInfo weddingInfo;
    private WeddiMap churchMapFragment;
    private WeddiMap weddinghouseMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wedding_info);
        setTitle("");

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);

        Utils.setProfilePhoto(this, WeddingInfoActivity.this);
        initialize();

        if(Settings.user.getRole().equals("HOST")) {
            edit.setVisibility(View.VISIBLE);
            edit.setOnClickListener(v -> startActivity(new Intent(WeddingInfoActivity.this, EditInfoActivity.class)));
        }

        WeddingInfoTasks weddingInfoTasks = new WeddingInfoTasks(this);
        weddingInfoTasks.execute();
    }

    private void updateChurchCoords(){
        if(weddingInfo == null)
            return;

        double lat = this.weddingInfo.getChLatitude();
        double lng = this.weddingInfo.getChLongitude();

        LatLng churchCoord = new LatLng(lat, lng);
        churchMapFragment = new WeddiMap(churchCoord);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.info_church_map_fragment, churchMapFragment)
                .commit();
    }

    private void updateWeddingHouseCoords(){
        if(weddingInfo == null)
            return;
        double lat = this.weddingInfo.getwLatitude();
        double lng = this.weddingInfo.getwLongitude();

        LatLng weddinghouseCoord = new LatLng(lat, lng);
        weddinghouseMapFragment = new WeddiMap(weddinghouseCoord);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.wedding_house_map_fragment, weddinghouseMapFragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean menu = MenuHandler.menu(item, this, WeddingInfoActivity.this);
        if(!menu)
            return super.onOptionsItemSelected(item);
        return menu;
    }

    private void initialize() {
        hostsPhoto = findViewById(R.id.hostsPhoto);
        churchPhoto = findViewById(R.id.info_churchPhoto);
        partyHousePhoto = findViewById(R.id.info_weddinghouse_img);
        edit = findViewById(R.id.bInfo_Edit);

        brideName = findViewById(R.id.brideName);
        bridePhone = findViewById(R.id.bridePhone);
        groomName = findViewById(R.id.groomName);
        groomPhone = findViewById(R.id.groomPhone);
        churchName = findViewById(R.id.info_church_name);
        churchAddress = findViewById(R.id.info_church_address);
        weddingTime = findViewById(R.id.weddingtime);
        partyHouseName = findViewById(R.id.info_weddinghouse_name);
        partyHouseAddress = findViewById(R.id.info_weddinghouse_address);
        info_church_map_fragment = findViewById(R.id.info_church_map_fragment);
        wedding_house_map_fragment =  findViewById(R.id.wedding_house_map_fragment);
    }


    private static class WeddingInfoTasks extends AsyncTask<Void, Void, Object[]> {

        private final WeakReference<WeddingInfoActivity> activityReference;

        public WeddingInfoTasks(WeddingInfoActivity context) {
            this.activityReference = new WeakReference<>(context);
        }

        @Override
        protected Object[] doInBackground(Void... params) {

            RequestUtils requestUtils = new RequestUtils();
            RestTemplate restTemplate = requestUtils.getRestTemplate();
            HttpHeaders requestHeaders = requestUtils.getRequestHeaders();

            String path = Settings.server_url + "/api/weddinginfo/" + Settings.weddingid;
            ResponseEntity<WeddingInfo> response = restTemplate.exchange(path,
                    HttpMethod.GET,
                    new HttpEntity<>(requestHeaders),
                    WeddingInfo.class);

            WeddingInfo weddingInfo = response.getBody();

            path = Settings.server_url + "/api/user/host";
            ResponseEntity<Host> response2 = restTemplate.postForEntity(path,
                    new HttpEntity<>(Settings.weddingid, requestHeaders),
                    Host.class);
            Host host = response2.getBody();

            return new Object[]{weddingInfo, host};
        }

        @Override
        protected void onPostExecute(Object[] retVal) {
            WeddingInfo weddingInfo = (WeddingInfo) retVal[0];
            Host host = (Host) retVal[1];
            WeddingInfoActivity context = activityReference.get();
            context.weddingInfo = weddingInfo;

            context.updateChurchCoords();
            context.updateWeddingHouseCoords();

            context.brideName.setText(host.getBrideName());
            context.bridePhone.setText(host.getBridephone());

            context.groomName.setText(host.getGroomName());
            context.groomPhone.setText(host.getGroomphone());

            context.churchName.setText(weddingInfo.getChurchname());
            context.churchAddress.setText(weddingInfo.getChurchaddress());
            String sDate = DateUtils.getWeddingDate(weddingInfo) + " " + DateUtils.getWeddingTime(weddingInfo);
            context.weddingTime.setText(sDate);

            context.partyHouseName.setText(weddingInfo.getWeddinghousename());
            context.partyHouseAddress.setText(weddingInfo.getwAddress());
            Picasso.get().load(Settings.server_url + weddingInfo.getChWebAppPath()).into(context.churchPhoto);
            Picasso.get().load(Settings.server_url +weddingInfo.getwWebAppPath()).into(context.partyHousePhoto);
            Picasso.get().load(Settings.server_url +host.getWebAppPath()).into(context.hostsPhoto);
        }
    }
}
