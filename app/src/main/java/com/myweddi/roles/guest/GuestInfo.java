package com.myweddi.roles.guest;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.myweddi.ChurchMap;
import com.myweddi.ChurchMaps;
import com.myweddi.R;
import com.myweddi.model.ChurchInfo;
import com.myweddi.model.Host;
import com.myweddi.model.WeddingInfo;
import com.myweddi.settings.Settings;
import com.myweddi.utils.RequestUtils;
import com.squareup.picasso.Picasso;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class GuestInfo extends AppCompatActivity {

    ImageView myProfilPhoto, hostsPhoto, churchPhoto, partyHousePhoto;
    TextView brideName, groomName, bridePhone, groomPhone, churchName, churchAddress, weddingTime,
    partyHouseName, partyHouseAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_info);
        setTitle("");

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);

        myProfilPhoto = (ImageView) findViewById(R.id.myprofilphoto);
        Glide.with(this)
                .load("https://fwcdn.pl/fpo/71/07/707107/7648804.3.jpg")
                .circleCrop()
                .into(myProfilPhoto);
        initialize();

        LatLng churchCoord = new LatLng(50.290992, 16.87383);
        ChurchMap mapFragment = new ChurchMap(churchCoord);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, mapFragment)
                .commit();

        LatLng sydney = new LatLng(50.2827394, 16.880499);
        ChurchMap weddingM = new ChurchMap(sydney);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.wedding_house_map, weddingM)
                .commit();

        WeddingInfoTasks weddingInfoTasks = new WeddingInfoTasks();
        weddingInfoTasks.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection

        switch (item.getItemId()) {
            case R.id.bHome:
                Log.i("Menu","Home");
                startActivity(new Intent(GuestInfo.this, GuestHome.class));
                return true;
            case R.id.bInfo:
                Log.i("Menu","Info");
                startActivity(new Intent(GuestInfo.this, GuestInfo.class));
                //this.finish();
                return true;
            case R.id.bTable:
                Log.i("Menu","Stoły");
                startActivity(new Intent(GuestInfo.this, TableActivity.class));
                return true;
            case R.id.bLogout:
                Log.i("Menu","Wyloguj");
                return true;
            case R.id.bOptions:
                Log.i("Menu","Opcje");
                startActivity(new Intent(GuestInfo.this, SettingActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initialize() {
        hostsPhoto = (ImageView) findViewById(R.id.hostsPhoto);
        churchPhoto = (ImageView) findViewById(R.id.churchPhoto);
        partyHousePhoto = (ImageView) findViewById(R.id.partyHousePhoto);

        brideName = (TextView) findViewById(R.id.brideName);
        bridePhone = (TextView) findViewById(R.id.bridePhone);
        groomName = (TextView) findViewById(R.id.groomName);
        groomPhone = (TextView) findViewById(R.id.groomPhone);
        churchName = (TextView) findViewById(R.id.churchName);
        churchAddress = (TextView) findViewById(R.id.weddingAddress);
        weddingTime = (TextView) findViewById(R.id.weddingTime);
        partyHouseName = (TextView) findViewById(R.id.weddingHouseName);
        partyHouseAddress = (TextView) findViewById(R.id.partyAddress);
    }

    class WeddingInfoTasks extends AsyncTask<Void, Void, Object[]> {
        @Override
        protected Object[] doInBackground(Void... params) {

            RequestUtils requestUtils = new RequestUtils();
            RestTemplate restTemplate = requestUtils.getRestTemplate();
            HttpHeaders requestHeaders = requestUtils.getRequestHeaders();

            String path = Settings.server_url + "/api/churchinfo/" + Settings.weddingid;
            ResponseEntity<ChurchInfo> response = restTemplate.exchange(path,
                    HttpMethod.GET,
                    new HttpEntity<Object>(requestHeaders),
                    ChurchInfo.class);

            ChurchInfo churchInfo = response.getBody();

            path = Settings.server_url + "/api/weddinginfo/" + Settings.weddingid;
            ResponseEntity<WeddingInfo> response2 = restTemplate.exchange(path,
                    HttpMethod.GET,
                    new HttpEntity<Object>(requestHeaders),
                    WeddingInfo.class);
            WeddingInfo weddingInfo = response2.getBody();

            path = Settings.server_url + "/api/user/host";
            ResponseEntity<Host> response3 = restTemplate.postForEntity(path,
                    new HttpEntity<Long>(Settings.weddingid, requestHeaders),
                    Host.class);
            Host host = response3.getBody();


            Object[] retVal = {(Object)weddingInfo, (Object)churchInfo, (Object)host};
            return retVal;
        }


        @Override
        protected void onPostExecute(Object[] retVal) {
            WeddingInfo weddingInfo = (WeddingInfo) retVal[0];
            ChurchInfo churchInfo = (ChurchInfo) retVal[1];
            Host host = (Host) retVal[2];
            brideName.setText(host.getBrideName());
            bridePhone.setText(host.getBridephone());

            groomName.setText(host.getGroomName());
            groomPhone.setText(host.getGroomphone());

            churchName.setText(churchInfo.getName());
            churchAddress.setText(churchInfo.getAddress());
            weddingTime.setText("not implemented");

            partyHouseName.setText(weddingInfo.getName());
            partyHouseAddress.setText(weddingInfo.getAddress());
            Picasso.get().load(churchInfo.getWebAppPath()).into(churchPhoto);
            Picasso.get().load(weddingInfo.getWebAppPath()).into(partyHousePhoto);
            Picasso.get().load(host.getPhoto()).into(hostsPhoto);

        }
    }
}