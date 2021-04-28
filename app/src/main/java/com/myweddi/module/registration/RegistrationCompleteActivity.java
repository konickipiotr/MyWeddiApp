package com.myweddi.module.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.myweddi.MainActivity;
import com.myweddi.R;
import com.myweddi.settings.Settings;
import com.myweddi.utils.RequestUtils;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class RegistrationCompleteActivity extends AppCompatActivity {

    private Button bSendAgain, bBackToLogin;
    private TextView email;
    private Long userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_complete);

        Intent intent = getIntent();
        userid = intent.getLongExtra("userid", -1);
        String email1 = intent.getStringExtra("email1");
        String email2 = intent.getStringExtra("email2");

        init();
        this.email.setText(email1 + " " + email2);
    }

    private void init(){
        bSendAgain = (Button) findViewById(R.id.bSendAgain);
        bBackToLogin = (Button) findViewById(R.id.bBackToLogin);
        email = (TextView) findViewById(R.id.emailLabel);

        bBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(RegistrationCompleteActivity.this, MainActivity.class));
            }
        });

        bSendAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterFormAsync registerFormAsync = new RegisterFormAsync();
                registerFormAsync.execute(userid);
            }
        });
    }

    class RegisterFormAsync extends AsyncTask<Long, Void, Void> {
        @Override
        protected Void doInBackground(Long... params) {

            RequestUtils requestUtils = new RequestUtils();
            RestTemplate restTemplate = requestUtils.getRestTemplate();
            Long userid = params[0];

            String path = Settings.server_url + "/api/registration/sendagain";

            ResponseEntity<Void> response = restTemplate.postForEntity(path,
                    userid,
                    Void.class);

            return null;
        }
    }
}