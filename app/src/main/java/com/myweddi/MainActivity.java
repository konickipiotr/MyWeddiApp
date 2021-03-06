package com.myweddi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.myweddi.model.User;
import com.myweddi.model.UserAuth;
import com.myweddi.roles.guest.GuestHome;
import com.myweddi.settings.Settings;

import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private EditText eLogin, ePassword;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void signIn(View view) {

        eLogin = (EditText) findViewById(R.id.loginInput);
        ePassword = (EditText) findViewById(R.id.passwordInput);

        Settings.username = eLogin.getText().toString();
        Settings.passoword = ePassword.getText().toString();

        String path= Settings.server_url + "api/user";
        //String paht= "http://80.211.245.217:8080/login";
        LoginTask loginTask = new LoginTask();
        loginTask.execute(path);


    }

    class LoginTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
//            RestTemplate rest = new RestTemplate();
//            rest.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
//            List<ClientHttpRequestInterceptor> list = new ArrayList<>();
//            ChexInterceptor cheint = new ChexInterceptor(username, password);
//
//            list.add(cheint);
//            rest.setInterceptors(list);
//
//            ResponseEntity<PostListWraper> response = rest.getForEntity(params[0], PostListWraper.class);



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
                    Log.i("XXXXXXX", user.toString());
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