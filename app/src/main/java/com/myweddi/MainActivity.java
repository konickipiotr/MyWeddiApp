package com.myweddi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.myweddi.model.User;
import com.myweddi.module.forgotpassword.RequestNewPassActivity;
import com.myweddi.module.registration.RegistrationActivity;
import com.myweddi.roles.Home;
import com.myweddi.settings.Settings;
import com.myweddi.utils.Utils;

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

import java.lang.ref.WeakReference;
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

        error_msg = findViewById(R.id.main_error_msg);
        msg = findViewById(R.id.main_msg);
        bRegistration = findViewById(R.id.bRegistration);
        bResetPassword = findViewById(R.id.bResetPassword);
        loginButton = findViewById(R.id.bSignIn);
        eLogin = findViewById(R.id.loginInput);
        ePassword = findViewById(R.id.passwordInput);

        if (errormessage != null && !errormessage.isEmpty()) {
            error_msg.setText(errormessage);
            error_msg.setVisibility(View.VISIBLE);
        }

        if (message != null && !message.isEmpty()) {
            msg.setText(message);
            msg.setVisibility(View.VISIBLE);
        }

        bRegistration.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, RegistrationActivity.class)));

        bResetPassword.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, RequestNewPassActivity.class)));

        loginButton.setOnClickListener(v -> {


            Settings.username = eLogin.getText().toString();
            Settings.passoword = ePassword.getText().toString();
            new LoginTask(this).execute();
        });
    }



    private static class LoginTask extends AsyncTask<Void, Void, User> {

        private final WeakReference<MainActivity> activityReference;

        public LoginTask(MainActivity context) {
            this.activityReference = new WeakReference<>(context);
        }

        @Override
        protected User doInBackground(Void... params) {
            String path= Settings.server_url + "/api/user";

            HttpAuthentication authHeader = new HttpBasicAuthentication(Settings.username,Settings.passoword);
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setAuthorization(authHeader);
            requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            User user = null;
            try {
                ResponseEntity<User> response = restTemplate.exchange(path, HttpMethod.GET, new HttpEntity<>(requestHeaders), User.class);
                if(response.getStatusCode().equals(HttpStatus.OK)){
                    user = response.getBody();
                }
            }catch (HttpClientErrorException e){
                return null;

            }
            return user;
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            MainActivity context = activityReference.get();

            if(user != null){
                Settings.user = user;
                Settings.weddingid = user.getWeddingid();
                if(user.getWebAppPath() != null){
                    String fullPath = Settings.server_url + user.getWebAppPath();
                    Settings.profilePhotoBitmap = Utils.getBitmapFromURL(fullPath);


                }
                context.finish();
                context.startActivity(new Intent(context, Home.class));
            }else {
                Settings.username = "";
                Settings.passoword = "";
                context.error_msg.setVisibility(View.VISIBLE);
                context.error_msg.setText(R.string.err_login_msg);
                context.eLogin.setText("");
                context.ePassword.setText("");
            }
        }
    }
}
