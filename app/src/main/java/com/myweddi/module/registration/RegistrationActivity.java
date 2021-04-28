package com.myweddi.module.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.myweddi.R;
import com.myweddi.roles.guest.PostActivity;
import com.myweddi.settings.Settings;
import com.myweddi.utils.RequestUtils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class RegistrationActivity extends AppCompatActivity {

    private RadioButton rbHost, rbGuest, rbDJ, rbPhoto;
    private LinearLayout hostForm, guestForm, otherCommon;

    private EditText tLogin, tPassword1, tPassword2, tBrideFristname, tBrideLastname,
            tBrideEmail, tBridePhone, tGroomFristname, tGroomLastname, tGroomEmail, tGroomPhone,
            tUserFristName, tUserLastName, tWeddingcode;
    private RadioButton rbFemale, rbMale;
    private Button registerButton;
    private TextView errorMessage, errorMessage2;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        init();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(notEmptyFields()){
                    RegisterFormAsync rfa = new RegisterFormAsync();
                    rfa.execute();
                }else {
                    errorMessage.setVisibility(View.VISIBLE);
                    errorMessage2.setVisibility(View.VISIBLE);
                    errorMessage.setText("Wszystkie pola są wymagane");
                    errorMessage2.setText("Wszystkie pola są wymagane");
                }
            }
        });
    }

    private boolean notEmptyFields(){
        if(tLogin.getText().toString().isEmpty()) return false;
        if(tPassword1.getText().toString().isEmpty()) return false;
        if(tPassword2.getText().toString().isEmpty()) return false;
        if(!tPassword1.getText().toString().equals(tPassword2.getText().toString())) return false;

        if(rbHost.isChecked()){
            if(tBrideFristname.getText().toString().isEmpty()) return false;
            if(tBrideLastname.getText().toString().isEmpty()) return false;
            if(tBrideEmail.getText().toString().isEmpty()) return false;
            if(tBridePhone.getText().toString().isEmpty()) return false;
            if(tGroomFristname.getText().toString().isEmpty()) return false;
            if(tGroomLastname.getText().toString().isEmpty()) return false;
            if(tGroomEmail.getText().toString().isEmpty()) return false;
            if(tGroomPhone.getText().toString().isEmpty()) return false;
        }else {
            if(tUserFristName.getText().toString().isEmpty()) return false;
            if(tUserLastName.getText().toString().isEmpty()) return false;
        }

        if(rbGuest.isChecked()){
            if(tWeddingcode.getText().toString().isEmpty()) return false;
        }
        return true;
    }

    private void init(){
        rbHost = (RadioButton) findViewById(R.id.reg_who_host);
        rbGuest = (RadioButton) findViewById(R.id.reg_who_guest);
        rbDJ = (RadioButton) findViewById(R.id.reg_who_dj);
        rbPhoto = (RadioButton) findViewById(R.id.reg_who_photo);

        tBrideFristname = (EditText) findViewById(R.id.reg_brideFirstname);
        tBrideLastname = (EditText) findViewById(R.id.reg_brideLastname);
        tBrideEmail = (EditText) findViewById(R.id.reg_brideemail);
        tBridePhone = (EditText) findViewById(R.id.reg_bridePhone);

        tGroomFristname = (EditText) findViewById(R.id.reg_groomFirstname);
        tGroomLastname = (EditText) findViewById(R.id.reg_groomLastname);
        tGroomEmail = (EditText) findViewById(R.id.reg_groomemail);
        tGroomPhone = (EditText) findViewById(R.id.reg_groomPhone);

        tLogin = (EditText) findViewById(R.id.reg_Login);
        tPassword1 = (EditText) findViewById(R.id.reg_pass1);
        tPassword2 = (EditText) findViewById(R.id.reg_pass2);

        tUserFristName = (EditText) findViewById(R.id.reg_userFirstname);
        tUserLastName = (EditText) findViewById(R.id.reg_userLastname);
        rbMale = (RadioButton) findViewById(R.id.rbMale);
        rbFemale = (RadioButton) findViewById(R.id.rbFemale);

        tWeddingcode = (EditText) findViewById(R.id.reg_weddingid);
        registerButton = (Button) findViewById(R.id.bRegister);
        errorMessage = (TextView) findViewById(R.id.reg_error_message);
        errorMessage2 = (TextView) findViewById(R.id.reg_error_message2);
        errorMessage.setVisibility(View.GONE);
        errorMessage2.setVisibility(View.GONE);

        hostForm = (LinearLayout) findViewById(R.id.hostForm);
        guestForm = (LinearLayout) findViewById(R.id.guestForm);
        otherCommon = (LinearLayout) findViewById(R.id.otherCommonData);
        hostForm.setVisibility(View.VISIBLE);
        guestForm.setVisibility(View.GONE);
        otherCommon.setVisibility(View.GONE);

        rbHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restRadioButtonWho();
                rbHost.setChecked(true);
                hostForm.setVisibility(View.VISIBLE);
                guestForm.setVisibility(View.GONE);
                otherCommon.setVisibility(View.GONE);
            }
        });

        rbGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restRadioButtonWho();
                rbGuest.setChecked(true);

                hostForm.setVisibility(View.GONE);
                otherCommon.setVisibility(View.VISIBLE);
                guestForm.setVisibility(View.VISIBLE);
            }
        });


        rbDJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restRadioButtonWho();
                rbDJ.setChecked(true);

                hostForm.setVisibility(View.GONE);
                guestForm.setVisibility(View.GONE);
                otherCommon.setVisibility(View.VISIBLE);
            }
        });

        rbPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restRadioButtonWho();
                rbPhoto.setChecked(true);

                hostForm.setVisibility(View.GONE);
                guestForm.setVisibility(View.GONE);
                otherCommon.setVisibility(View.VISIBLE);
            }
        });
    }

    private void restRadioButtonWho(){
        rbHost.setChecked(false);
        rbGuest.setChecked(false);
        rbDJ.setChecked(false);
        rbPhoto.setChecked(false);
    }

    class RegisterFormAsync extends AsyncTask<Void, Void, Long> {

        private final Long USER_EXIST = -11L;
        @Override
        protected Long doInBackground(Void... params) {

            RequestUtils requestUtils = new RequestUtils();
            RestTemplate restTemplate = requestUtils.getRestTemplate();
            //HttpHeaders requestHeaders = requestUtils.getRequestHeaders();

            RegistrationForm rf = new RegistrationForm();
            String roleUrl = "";
            if(rbHost.isChecked()){
                fillHostData(rf);

            }else {
                fillOtherCommonData(rf);
                if(rbGuest.isChecked())
                    roleUrl = "/guest";
            }

            String path = Settings.server_url + "/api/registration" + roleUrl;

            ResponseEntity<RegistrationForm> response = restTemplate.postForEntity(path,
                    rf,
                    RegistrationForm.class);

            Long userid;
            if(response.getStatusCode().equals(HttpStatus.FOUND)){
                userid = USER_EXIST;
            }else {
                userid = response.getBody().getUserid();
            }

            return userid;
        }

        @Override
        protected void onPostExecute(Long userid) {

            if(userid == USER_EXIST){
                errorMessage.setVisibility(View.VISIBLE);
                errorMessage2.setVisibility(View.VISIBLE);
                errorMessage.setText("Użytkownik o takiej nazwie istnieje");
                errorMessage2.setText("Użytkownik o takiej nazwie istnieje");
            }else{
                Intent regComplete = new Intent(RegistrationActivity.this, RegistrationCompleteActivity.class);
                regComplete.putExtra("userid", userid);
                regComplete.putExtra("email1", tBrideEmail.getText().toString());
                regComplete.putExtra("email2", tGroomEmail.getText().toString());
                finish();
                startActivity(regComplete);
            }
        }

        private void fillHostData(RegistrationForm rf){
            rf.setUsername(tLogin.getText().toString());
            rf.setPassword(tPassword1.getText().toString());
            rf.setUsertype("HOST");

            rf.setBridefirstname(tBrideFristname.getText().toString());
            rf.setBridelastname(tBrideLastname.getText().toString());
            rf.setBrideemail(tBrideEmail.getText().toString());
            rf.setBridephone(tBridePhone.getText().toString());

            rf.setGroomfirstname(tGroomFristname.getText().toString());
            rf.setGroomlastname(tGroomLastname.getText().toString());
            rf.setGroomemail(tGroomEmail.getText().toString());
            rf.setGroomphone(tGroomPhone.getText().toString());
        }

        private void fillOtherCommonData(RegistrationForm rf){
            rf.setUsername(tLogin.getText().toString());
            rf.setPassword(tPassword1.getText().toString());
            rf.setUsertype("GUEST"); //TODO Temporary there are only 2 user types.

            rf.setFirstname(tUserFristName.getText().toString());
            rf.setLastname(tUserLastName.getText().toString());

            if(rbMale.isChecked())
                rf.setGender("M");
            else
                rf.setGender("F");

            if(rbGuest.isChecked())
                rf.setWeddingcode(tWeddingcode.getText().toString());
        }
    }
}