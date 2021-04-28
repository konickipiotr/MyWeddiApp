package com.myweddi.module.forgotpassword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.myweddi.MainActivity;
import com.myweddi.R;
import com.myweddi.module.registration.RegistrationActivity;
import com.myweddi.module.registration.RegistrationCompleteActivity;
import com.myweddi.settings.Settings;
import com.myweddi.utils.RequestUtils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class RequestNewPassActivity extends AppCompatActivity {

    private Button bReqRestPass, bBackToLogin;
    private TextView login, error_msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_new_pass);

        init();
    }

    private void init(){
        bReqRestPass = (Button) findViewById(R.id.bRequestResetPass);
        bBackToLogin = (Button) findViewById(R.id.bFrgotToLogin);
        login = (EditText) findViewById(R.id.loginForNewPass);
        error_msg = (TextView) findViewById(R.id.pas_error_message);

        bBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(RequestNewPassActivity.this, MainActivity.class));
            }
        });

        bReqRestPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(login.getText() != null && !login.getText().toString().isEmpty()){
                    String username = login.getText().toString();
                    RequestForNewPasswordAsync requestForNewPasswordAsync = new RequestForNewPasswordAsync();
                    requestForNewPasswordAsync.execute(username);
                }

            }
        });
    }


    class RequestForNewPasswordAsync extends AsyncTask<String, Void, HttpStatus> {
        @Override
        protected HttpStatus doInBackground(String... params) {

            RequestUtils requestUtils = new RequestUtils();
            RestTemplate restTemplate = requestUtils.getRestTemplate();
            String username = params[0];

            String path = Settings.server_url + "/api/forgotpassword";

            ResponseEntity<Void> response = restTemplate.postForEntity(path, username, Void.class);

            return response.getStatusCode();
        }


        @Override
        protected void onPostExecute(HttpStatus httpStatus) {
            super.onPostExecute(httpStatus);

            Intent mainIntent = new Intent(RequestNewPassActivity.this, MainActivity.class);
            if(httpStatus.equals(HttpStatus.OK)) {
                mainIntent.putExtra("message", "Aby zmienić hasło kliknij link w emailu");
                finish();
                startActivity(mainIntent);
            }else if(httpStatus.equals(HttpStatus.NOT_FOUND)) {
                error_msg.setText("Taki login/email nie istnieje");
                error_msg.setTextColor(Color.RED);
                error_msg.setVisibility(View.VISIBLE);
            }else {
                mainIntent.putExtra("errormessage", "cos poszło nie tak");
                finish();
                startActivity(mainIntent);
            }
        }
    }


}