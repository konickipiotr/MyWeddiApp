package com.myweddi.roles.guest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.myweddi.MainActivity;
import com.myweddi.R;
import com.myweddi.enums.GuestStatus;
import com.myweddi.enums.UserStatus;
import com.myweddi.module.weddinginfo.WeddingInfo;
import com.myweddi.roles.Home;
import com.myweddi.roles.host.EditInfoActivity;
import com.myweddi.settings.Settings;
import com.myweddi.utils.RequestUtils;
import com.myweddi.utils.Utils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.lang.ref.WeakReference;

public class ConfirmActivity extends AppCompatActivity {

    private Switch confirm_isPresent, confirm_with_compan, confirm_needbed;
    private EditText confirm_childrenNum, confirm_bedNum;
    private Button bconfirm_Start, bconfirm_saveAndLogout;
    private LinearLayout confirm_bed_layout, confirm_children_layout;
    private TextView confirm_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        init();
        initListeners();
    }

    private void init(){
        confirm_isPresent = findViewById(R.id.confirm_isPresent);
        confirm_with_compan = findViewById(R.id.confirm_with_compan);
        confirm_needbed = findViewById(R.id.confirm_needbed);
        confirm_childrenNum = findViewById(R.id.confirm_childrenNum);
        confirm_bedNum = findViewById(R.id.confirm_bedNum);

        bconfirm_Start = findViewById(R.id.bconfirm_Start);
        bconfirm_saveAndLogout = findViewById(R.id.bconfirm_saveAndLogout);

        confirm_bed_layout = findViewById(R.id.confirm_bed_layout);
        confirm_children_layout = findViewById(R.id.confirm_children_layout);

        confirm_info = findViewById(R.id.confirm_info);

    }

    private void initListeners(){
        confirm_isPresent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    confirm_with_compan.setVisibility(View.VISIBLE);
                    confirm_children_layout.setVisibility(View.VISIBLE);
                    confirm_needbed.setVisibility(View.VISIBLE);
                    if(confirm_needbed.isChecked())
                        confirm_bed_layout.setVisibility(View.VISIBLE);
                    bconfirm_Start.setVisibility(View.VISIBLE);

                    bconfirm_saveAndLogout.setVisibility(View.GONE);
                    confirm_info.setVisibility(View.GONE);
                }else {
                    confirm_with_compan.setVisibility(View.GONE);
                    confirm_needbed.setVisibility(View.GONE);
                    confirm_bed_layout.setVisibility(View.GONE);
                    confirm_children_layout.setVisibility(View.GONE);
                    bconfirm_Start.setVisibility(View.GONE);

                    confirm_info.setVisibility(View.VISIBLE);
                    bconfirm_saveAndLogout.setVisibility(View.VISIBLE);
                    confirm_info.setVisibility(View.VISIBLE);
                }
            }
        });

        bconfirm_Start.setOnClickListener(v -> {
            collectData();
            if(Settings.guest.getStatus().equals(GuestStatus.CONFIRMED)) {
                new FirstQuestionsSaverAsync().execute();
                finish();
                startActivity(new Intent(ConfirmActivity.this, Home.class));
            }
        });

        bconfirm_saveAndLogout.setOnClickListener(v -> {
            collectData();
            new FirstQuestionsSaverAsync().execute();
            finish();
            Settings.user = null;
            Settings.profilePhotoBitmap = null;
            Settings.username = null;
            Settings.passoword = null;
            startActivity(new Intent(ConfirmActivity.this, MainActivity.class));
        });

        confirm_needbed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    confirm_bed_layout.setVisibility(View.VISIBLE);
                }else {
                    confirm_bed_layout.setVisibility(View.GONE);
                }
            }
        });
    }

    private void collectData(){
        if(!confirm_isPresent.isChecked()){
            Settings.guest.setStatus(GuestStatus.REJECTED);
            return;
        }

        Settings.guest.setStatus(GuestStatus.CONFIRMED);
        Settings.guest.setPartner(confirm_with_compan.isChecked());
        String childrenNum = confirm_childrenNum.getText().toString();
        if(!childrenNum.isEmpty())
            Settings.guest.setChildren(Integer.valueOf(childrenNum));
        Settings.user.setRole("GUEST");
        Settings.user.setUserStatus(UserStatus.ACTIVE);
        if(confirm_needbed.isChecked()){
            Settings.guest.setBed(true);

            String bedNum = confirm_bedNum.getText().toString();
            if(!bedNum.isEmpty())
                Settings.guest.setNumofbed(Integer.valueOf(bedNum));
        }
    }

    private static class FirstQuestionsSaverAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            RequestUtils requestUtils = new RequestUtils();
            RestTemplate restTemplate = requestUtils.getRestTemplate();
            HttpHeaders requestHeaders = requestUtils.getRequestHeaders();
            String path = Settings.server_url + "/api/firstlogin/api";

            try{
            restTemplate.exchange(path,
                    HttpMethod.POST,
                    new HttpEntity<>(Settings.guest, requestHeaders),
                    Void.class);
            }catch (HttpClientErrorException e){
                e.printStackTrace();
            }
            return null;
        }
    }
}