package com.myweddi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.TextView;

import com.myweddi.model.User;
import com.myweddi.module.forgotpassword.RequestNewPassActivity;
import com.myweddi.module.registration.RegistrationActivity;
import com.myweddi.roles.guest.GuestHome;
import com.myweddi.settings.Settings;
import com.myweddi.utils.OtherUtils;

import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private EditText eLogin, ePassword;
    private Button loginButton, bRegistration, bResetPassword;
    private TextView error_msg, msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String message = intent.getStringExtra("message");
        String errormessage = intent.getStringExtra("errormessage");

        error_msg = (TextView) findViewById(R.id.main_error_msg);
        msg = (TextView) findViewById(R.id.main_msg);
        bRegistration = (Button) findViewById(R.id.bRegistration);
        bResetPassword = (Button) findViewById(R.id.bResetPassword);

        if(errormessage != null && !errormessage.isEmpty()){
            error_msg.setText(errormessage);
            error_msg.setVisibility(View.VISIBLE);
        }

        if(message != null && !message.isEmpty()) {
            msg.setText(message);
            msg.setVisibility(View.VISIBLE);
        }

        bRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegistrationActivity.class));
            }
        });

        bResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RequestNewPassActivity.class ));
            }
        });
    }

    public void signIn(View view) {

        eLogin = (EditText) findViewById(R.id.loginInput);
        ePassword = (EditText) findViewById(R.id.passwordInput);

        Settings.username = eLogin.getText().toString();
        Settings.passoword = ePassword.getText().toString();

        String path= Settings.server_url + "/api/user";
        LoginTask loginTask = new LoginTask();
        loginTask.execute(path);
    }

    class LoginTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            HttpAuthentication authHeader = new HttpBasicAuthentication(Settings.username,Settings.passoword);
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setAuthorization(authHeader);
            requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            // Create a new RestTemplate instance
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            try {
                ResponseEntity<User> response = restTemplate.exchange(params[0], HttpMethod.GET, new HttpEntity<Object>(requestHeaders), User.class);
                if(response.getStatusCode() == HttpStatus.OK){
                    User user = response.getBody();
                    Settings.user = user;
                    Settings.weddingid = user.getWeddingid();
                    if(user.getWebAppPath() != null){
                        String fullPath = Settings.server_url + user.getWebAppPath();
                        Settings.profilePhotoBitmap = OtherUtils.getBitmapFromURL(fullPath);
                    }
                    startActivity(new Intent(MainActivity.this, GuestHome.class));
                }else{
                    Settings.username = "";
                    Settings.passoword = "";
                }
            }catch (HttpClientErrorException e){

            }
            //ResponseEntity<PostListWraper> response = restTemplate.exchange(params[0], HttpMethod.GET, new HttpEntity<Object>(requestHeaders), PostListWraper.class);
            return null;
        }
    }
}