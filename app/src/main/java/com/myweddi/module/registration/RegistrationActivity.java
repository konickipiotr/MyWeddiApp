package com.myweddi.module.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.myweddi.R;
import com.myweddi.settings.Settings;
import com.myweddi.utils.RequestUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.lang.ref.WeakReference;

public class RegistrationActivity extends AppCompatActivity {

    private RadioButton rbHost, rbGuest, rbDJ, rbPhoto, rbMale;
    private LinearLayout hostForm, guestForm, otherCommon;

    private EditText tLogin, tPassword1, tPassword2;
    private EditText tGroomFristname, tGroomLastname, tGroomEmail, tGroomPhone;
    private EditText tBrideFristname, tBrideLastname, tBrideEmail, tBridePhone;
    private EditText tUserFirstName, tUserLastName, tWeddingcode;

    private Button registerButton;
    private TextView errorMessage, errorMessage2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        init();
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
            if(tUserFirstName.getText().toString().isEmpty()) return false;
            if(tUserLastName.getText().toString().isEmpty()) return false;
        }

        if(rbGuest.isChecked()){
            return !tWeddingcode.getText().toString().isEmpty();
        }
        return true;
    }

    private void init(){
        rbGuest = findViewById(R.id.reg_who_guest);
        rbHost = findViewById(R.id.reg_who_host);
        rbDJ = findViewById(R.id.reg_who_dj);
        rbPhoto = findViewById(R.id.reg_who_photo);

        hostForm = findViewById(R.id.hostForm);
        guestForm = findViewById(R.id.guestForm);
        otherCommon = findViewById(R.id.otherCommonData);

        tLogin = findViewById(R.id.reg_Login);
        tPassword1 = findViewById(R.id.reg_pass1);
        tPassword2 = findViewById(R.id.reg_pass2);

        tGroomFristname = findViewById(R.id.reg_groomFirstname);
        tGroomLastname = findViewById(R.id.reg_groomLastname);
        tGroomEmail = findViewById(R.id.reg_groomemail);
        tGroomPhone = findViewById(R.id.reg_groomPhone);

        tBrideFristname = findViewById(R.id.reg_brideFirstname);
        tBrideLastname = findViewById(R.id.reg_brideLastname);
        tBrideEmail = findViewById(R.id.reg_brideemail);
        tBridePhone = findViewById(R.id.reg_bridePhone);

        tUserFirstName = findViewById(R.id.reg_userFirstname);
        tUserLastName = findViewById(R.id.reg_userLastname);
        tWeddingcode = findViewById(R.id.reg_weddingid);

        rbMale = findViewById(R.id.rbMale);
        registerButton = findViewById(R.id.bRegister);

        errorMessage = findViewById(R.id.reg_error_message);
        errorMessage2 = findViewById(R.id.reg_error_message2);

        errorMessage.setVisibility(View.GONE);
        errorMessage2.setVisibility(View.GONE);

        hostForm.setVisibility(View.VISIBLE);
        guestForm.setVisibility(View.GONE);
        otherCommon.setVisibility(View.GONE);

        registerButton.setOnClickListener(v -> {
            if(notEmptyFields()){
                new RegisterFormAsync(this).execute();
            }else {
                errorMessage.setVisibility(View.VISIBLE);
                errorMessage2.setVisibility(View.VISIBLE);
            }
        });

        rbHost.setOnClickListener(v -> {
            restRadioButtonWho();
            rbHost.setChecked(true);
            hostForm.setVisibility(View.VISIBLE);
            guestForm.setVisibility(View.GONE);
            otherCommon.setVisibility(View.GONE);
        });

        rbGuest.setOnClickListener(v -> {
            restRadioButtonWho();
            rbGuest.setChecked(true);

            hostForm.setVisibility(View.GONE);
            otherCommon.setVisibility(View.VISIBLE);
            guestForm.setVisibility(View.VISIBLE);
        });

        rbDJ.setOnClickListener(v -> {
            restRadioButtonWho();
            rbDJ.setChecked(true);

            hostForm.setVisibility(View.GONE);
            guestForm.setVisibility(View.GONE);
            otherCommon.setVisibility(View.VISIBLE);
        });

        rbPhoto.setOnClickListener(v -> {
            restRadioButtonWho();
            rbPhoto.setChecked(true);

            hostForm.setVisibility(View.GONE);
            guestForm.setVisibility(View.GONE);
            otherCommon.setVisibility(View.VISIBLE);
        });
    }

    private void restRadioButtonWho(){
        rbHost.setChecked(false);
        rbGuest.setChecked(false);
        rbDJ.setChecked(false);
        rbPhoto.setChecked(false);
    }


    private static class RegisterFormAsync extends AsyncTask<Void, Void, Long> {

        private final WeakReference<RegistrationActivity> activityReference;
        private final Long USER_EXIST = -11L;

        public RegisterFormAsync(RegistrationActivity context) {
            this.activityReference = new WeakReference<>(context);
        }

        @Override
        protected Long doInBackground(Void... params) {

            RegistrationActivity context = activityReference.get();
            RequestUtils requestUtils = new RequestUtils();
            RestTemplate restTemplate = requestUtils.getRestTemplate();

            RegistrationForm rf = new RegistrationForm();
            String roleUrl = "";
            if(context.rbHost.isChecked()){
                fillHostData(rf, context);

            }else {
                fillOtherCommonData(rf, context);
                if(context.rbGuest.isChecked())
                    roleUrl = "/guest";
            }

            String path = Settings.server_url + "/api/registration" + roleUrl;
            ResponseEntity<Long> response = restTemplate.postForEntity(path, rf, Long.class);

            Long userid;
            if(response.getStatusCode().equals(HttpStatus.FOUND))
                userid = USER_EXIST;
            else
                userid = response.getBody();

            return userid;
        }

        @Override
        protected void onPostExecute(Long userid) {

            RegistrationActivity context = activityReference.get();
            if(userid.equals(USER_EXIST)){
                context.errorMessage.setVisibility(View.VISIBLE);
                context.errorMessage2.setVisibility(View.VISIBLE);
                context.errorMessage.setText(R.string.reg_msg1_all_fields_required);
                context.errorMessage2.setText(R.string.reg_msg1_all_fields_required);
            }else{
                Intent regComplete = new Intent(context, RegistrationCompleteActivity.class);
                regComplete.putExtra("userid", userid);
                regComplete.putExtra("email1", context.tBrideEmail.getText().toString());
                regComplete.putExtra("email2", context.tGroomEmail.getText().toString());
                context.finish();
                context.startActivity(regComplete);
            }
        }

        private void fillHostData(RegistrationForm rf, RegistrationActivity context){
            rf.setUsername(context.tLogin.getText().toString());
            rf.setPassword(context.tPassword1.getText().toString());
            rf.setUsertype("HOST");

            rf.setBridefirstname(context.tBrideFristname.getText().toString());
            rf.setBridelastname(context.tBrideLastname.getText().toString());
            rf.setBrideemail(context.tBrideEmail.getText().toString());
            rf.setBridephone(context.tBridePhone.getText().toString());

            rf.setGroomfirstname(context.tGroomFristname.getText().toString());
            rf.setGroomlastname(context.tGroomLastname.getText().toString());
            rf.setGroomemail(context.tGroomEmail.getText().toString());
            rf.setGroomphone(context.tGroomPhone.getText().toString());
        }

        private void fillOtherCommonData(RegistrationForm rf, RegistrationActivity context){
            rf.setUsername(context.tLogin.getText().toString());
            rf.setPassword(context.tPassword1.getText().toString());
            rf.setUsertype("GUEST"); //TODO Temporary there are only 2 user types.

            rf.setFirstname(context.tUserFirstName.getText().toString());
            rf.setLastname(context.tUserLastName.getText().toString());

            if(context.rbMale.isChecked())
                rf.setGender("M");
            else
                rf.setGender("F");

            if(context.rbGuest.isChecked())
                rf.setWeddingcode(context.tWeddingcode.getText().toString());
        }
    }
}
