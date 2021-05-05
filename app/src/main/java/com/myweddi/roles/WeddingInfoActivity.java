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
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.myweddi.ChurchMap;
import com.myweddi.R;
import com.myweddi.model.Host;
import com.myweddi.module.weddinginfo.WeddingInfo;
import com.myweddi.roles.host.EditInfoActivity;
import com.myweddi.settings.Settings;
import com.myweddi.utils.MenuHandler;
import com.myweddi.utils.Utils;
import com.myweddi.utils.RequestUtils;
import com.squareup.picasso.Picasso;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class WeddingInfoActivity extends AppCompatActivity {

    private ImageView myProfilPhoto, hostsPhoto, churchPhoto, partyHousePhoto;
    private TextView brideName, groomName, bridePhone, groomPhone, churchName, churchAddress, weddingTime,
    partyHouseName, partyHouseAddress;
    private Button edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wedding_info);
        setTitle("");

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);

        Utils.setProfilePhoto(myProfilPhoto, this, WeddingInfoActivity.this);
        initialize();

        if(Settings.user.getRole().equals("HOST")) {
            edit.setVisibility(View.VISIBLE);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(WeddingInfoActivity.this, EditInfoActivity.class));
                }
            });
        }

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
        boolean menu = MenuHandler.menu(item, this, WeddingInfoActivity.this);
        if(!menu)
            return super.onOptionsItemSelected(item);
        return menu;
    }

    private void initialize() {
        hostsPhoto = (ImageView) findViewById(R.id.hostsPhoto);
        churchPhoto = (ImageView) findViewById(R.id.churchPhoto);
        partyHousePhoto = (ImageView) findViewById(R.id.partyHousePhoto);
        edit = (Button) findViewById(R.id.bInfo_Edit);

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

            String path = Settings.server_url + "/api/weddinginfo/" + Settings.weddingid;
            ResponseEntity<WeddingInfo> response = restTemplate.exchange(path,
                    HttpMethod.GET,
                    new HttpEntity<Object>(requestHeaders),
                    WeddingInfo.class);

            WeddingInfo weddingInfo = response.getBody();


            path = Settings.server_url + "/api/user/host";
            ResponseEntity<Host> response2 = restTemplate.postForEntity(path,
                    new HttpEntity<Long>(Settings.weddingid, requestHeaders),
                    Host.class);
            Host host = response2.getBody();


            Object[] retVal = {(Object)weddingInfo, (Object)host};
            return retVal;
        }


        @Override
        protected void onPostExecute(Object[] retVal) {
            WeddingInfo weddingInfo = (WeddingInfo) retVal[0];
            Host host = (Host) retVal[1];
            brideName.setText(host.getBrideName());
            bridePhone.setText(host.getBridephone());

            groomName.setText(host.getGroomName());
            groomPhone.setText(host.getGroomphone());

            churchName.setText(weddingInfo.getChurchname());
            churchAddress.setText(weddingInfo.getChurchaddress());
            weddingTime.setText("not implemented");

            partyHouseName.setText(weddingInfo.getWeddinghousename());
            partyHouseAddress.setText(weddingInfo.getwAddress());
            Picasso.get().load(Settings.server_url + weddingInfo.getChWebAppPath()).into(churchPhoto);
            Picasso.get().load(Settings.server_url +weddingInfo.getwWebAppPath()).into(partyHousePhoto);
            Picasso.get().load(Settings.server_url +host.getWebAppPath()).into(hostsPhoto);
        }
    }
}