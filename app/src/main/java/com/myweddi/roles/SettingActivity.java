package com.myweddi.roles;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.myweddi.MainActivity;
import com.myweddi.ProfilePhotoActivity;
import com.myweddi.R;
import com.myweddi.enums.GuestStatus;
import com.myweddi.model.User;
import com.myweddi.module.settings.async.ChangeUserPasswordAsync;
import com.myweddi.module.settings.async.GetUserAsync;
import com.myweddi.module.settings.async.RemoveAccountAsync;
import com.myweddi.module.settings.async.UpdateGuestAsync;
import com.myweddi.settings.Settings;
import com.myweddi.utils.MenuHandler;
import com.myweddi.utils.Utils;

import java.util.Set;
import java.util.concurrent.ExecutionException;

public class SettingActivity extends AppCompatActivity {

    private ImageView settings_profile_photo;
    private Button bsettings_change_photo, bSettings_change_password, bsettings_save_other, bsettings_remove_account;
    private EditText settings_old_password_edit, settings_new_password_edit, settings_new2_password_edit, settings_childrenNum, settings_bedNum;
    private Switch settings_present, settings_compan, settings_needbed;
    private LinearLayout settings_bed_layout, settings_children_layout, settings_other_layout;
    private TextView settings_error_password_msg, settings_info_password_msg;

    private final int PASSWORD_LENGTH = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setTitle("");

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        Utils.setProfilePhoto(this, SettingActivity.this);

        switch (Settings.user.getRole()){
            case "HOST":{
                initCommonElements();
                bsettings_remove_account.setText(R.string.settings_remove_wedding);
            }break;
            case "GUEST":{
                initCommonElements();
                initGuestFields();
            }break;
        }

    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        Utils.setProfilePhoto(this, SettingActivity.this);
        settings_profile_photo = findViewById(R.id.settings_profile_photo);
        Glide.with(SettingActivity.this)
                .load(Settings.profilePhotoBitmap != null ? Settings.profilePhotoBitmap : SettingActivity.this.getResources().getDrawable(R.drawable.user))
                .circleCrop()
                .into(settings_profile_photo);
    }


    private void initCommonElements(){
        settings_profile_photo = findViewById(R.id.settings_profile_photo);
        Glide.with(SettingActivity.this)
                .load(Settings.profilePhotoBitmap != null ? Settings.profilePhotoBitmap : SettingActivity.this.getResources().getDrawable(R.drawable.user))
                .circleCrop()
                .into(settings_profile_photo);

        bsettings_change_photo = findViewById(R.id.bsettings_change_photo);
        bSettings_change_password = findViewById(R.id.bSettings_change_password);
        settings_old_password_edit = findViewById(R.id.settings_old_password_edit);
        settings_new_password_edit = findViewById(R.id.settings_new_password_edit);
        settings_new2_password_edit = findViewById(R.id.settings_new2_password_edit);
        settings_error_password_msg = findViewById(R.id.settings_error_password_msg);
        settings_info_password_msg = findViewById(R.id.settings_info_password_msg);

        bsettings_remove_account = findViewById(R.id.bsettings_remove_account);
        bsettings_remove_account.setOnClickListener(v -> {

            String msg;
            if(Settings.user.getRole().equals("HOST")){
                msg = getResources().getString(R.string.settings_removed_host_account_msg);
            }else if(Settings.user.getRole().equals("GUEST")){
                msg = getResources().getString(R.string.settings_removed_guest_account_msg);
            }else {
                throw new RuntimeException();
            }

            AlertDialog.Builder dialog=new AlertDialog.Builder(this);
            dialog.setMessage(msg);
            dialog.setTitle("Usuwanie konta");
            dialog.setPositiveButton("YES",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            Boolean isOk = null;
                            try {
                                isOk = new RemoveAccountAsync().execute().get();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (isOk) {
                                finish();
                                Settings.user = null;
                                Settings.guest = null;
                                Settings.profilePhotoBitmap = null;
                                Settings.username = null;
                                Settings.passoword = null;
                                startActivity(new Intent(SettingActivity.this, MainActivity.class));
                                Context context = getApplicationContext();
                                Toast toast = Toast.makeText(context, R.string.settings_account_removed, Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }
                    });

            dialog.setNegativeButton("cancel",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getApplicationContext(),"cancel is clicked",Toast.LENGTH_LONG).show();
                }
            });
            AlertDialog alertDialog=dialog.create();
            alertDialog.show();
        });

        bsettings_change_photo.setOnClickListener(v -> {
            startActivity(new Intent(SettingActivity.this, ProfilePhotoActivity.class));
        });

        bSettings_change_password.setOnClickListener(v -> {
            settings_info_password_msg.setVisibility(View.GONE);
            String old_pass = settings_old_password_edit.getText().toString();
            String pass1 = settings_new_password_edit.getText().toString();
            String pass2 = settings_new2_password_edit.getText().toString();

            if(old_pass.isEmpty() || pass1.isEmpty() || pass2.isEmpty()){
                settings_error_password_msg.setText(R.string.settings_password_err_emptyfields);
                return;
            }

            if(pass1.length() < PASSWORD_LENGTH || pass2.length() < PASSWORD_LENGTH){
                settings_error_password_msg.setText(R.string.settings_password_err_tooshort);
                return;
            }

            if(!Settings.passoword.equals(old_pass)){
                settings_error_password_msg.setText(R.string.settings_password_err_wrongOld);
                return;
            }

            User user = null;
            try {
                user = new GetUserAsync().execute().get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(user == null){
                settings_error_password_msg.setText(R.string.settings_password_err_oldNotCurrent);
                return;
            }

            if(!pass1.equals(pass2)){
                settings_error_password_msg.setText(R.string.settings_password_err_notmatch);
                return;
            }

            if(pass1.equals(old_pass)){
                settings_error_password_msg.setText(R.string.settings_password_err_sameAsOld);
                return;
            }

            try {
                Boolean isOk = new ChangeUserPasswordAsync().execute(pass1).get();
                if(isOk) {
                    Settings.passoword = pass1;
                    settings_info_password_msg.setVisibility(View.VISIBLE);
                    settings_error_password_msg.setText("");
                    settings_old_password_edit.setText("");
                    settings_new_password_edit.setText("");
                    settings_new2_password_edit.setText("");
                }else
                    settings_error_password_msg.setText(R.string.errr_msg_something_goes_worng);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private void initGuestFields(){
        bsettings_save_other = findViewById(R.id.bsettings_save_other);
        bsettings_remove_account = findViewById(R.id.bsettings_remove_account);

        settings_present = findViewById(R.id.settings_prestent);
        settings_compan = findViewById(R.id.settings_compan);
        settings_needbed = findViewById(R.id.settings_needbed);
        settings_childrenNum = findViewById(R.id.settings_childrenNum);
        settings_bedNum = findViewById(R.id.settings_bedNum);

        settings_bed_layout = findViewById(R.id.settongs_bed_layout);
        settings_children_layout = findViewById(R.id.settings_children_layout);
        settings_other_layout = findViewById(R.id.settings_other_layout);
        settings_other_layout.setVisibility(View.VISIBLE);
        settings_present.setVisibility(View.VISIBLE);

        settings_present.setChecked(Settings.guest.getStatus().equals(GuestStatus.CONFIRMED));
        settings_compan.setChecked(Settings.guest.isPartner());
        settings_needbed.setChecked(Settings.guest.isBed());
        settings_childrenNum.setText(String.valueOf(Settings.guest.getChildren()));
        settings_bedNum.setText(String.valueOf(Settings.guest.getNumofbed()));

        if(settings_present.isChecked()){
            settings_compan.setVisibility(View.VISIBLE);
            settings_children_layout.setVisibility(View.VISIBLE);
            settings_needbed.setVisibility(View.VISIBLE);

            if(settings_needbed.isChecked()) {
                settings_bed_layout.setVisibility(View.VISIBLE);
            }
            bsettings_save_other.setVisibility(View.VISIBLE);
            bsettings_remove_account.setVisibility(View.VISIBLE);
        }

        settings_present.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    settings_compan.setVisibility(View.VISIBLE);
                    settings_children_layout.setVisibility(View.VISIBLE);
                    settings_needbed.setVisibility(View.VISIBLE);
                    if(settings_needbed.isChecked()){
                        settings_bed_layout.setVisibility(View.VISIBLE);
                    }
                }else {
                    settings_compan.setVisibility(View.GONE);
                    settings_children_layout.setVisibility(View.GONE);
                    settings_needbed.setVisibility(View.GONE);
                    settings_bed_layout.setVisibility(View.GONE);
                    bsettings_save_other.setVisibility(View.GONE);
                }
                bsettings_save_other.setVisibility(View.VISIBLE);
            }
        });

        settings_needbed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    settings_bed_layout.setVisibility(View.VISIBLE);
                else
                    settings_bed_layout.setVisibility(View.GONE);
            }
        });

        bsettings_save_other.setOnClickListener(v -> {
            collectData();
            try {
                Boolean isOk = new UpdateGuestAsync().execute().get();
                if(isOk){
                    Context context = getApplicationContext();
                    Toast toast = Toast.makeText(context, R.string.msg_saved, Toast.LENGTH_SHORT);
                    toast.show();
                }

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });



    }

    private void collectData(){
        if(!settings_present.isChecked()){
            Settings.guest.setStatus(GuestStatus.REJECTED);
            Settings.guest.setNumofbed(0);
            Settings.guest.setBed(false);
            Settings.guest.setPartner(false);
            Settings.guest.setChildren(0);
            return;
        }

        Settings.guest.setStatus(GuestStatus.CONFIRMED);
        Settings.guest.setPartner(settings_compan.isChecked());
        String childrenNum = settings_childrenNum.getText().toString();
        if(!childrenNum.isEmpty())
            Settings.guest.setChildren(Integer.valueOf(childrenNum));

        if(settings_needbed.isChecked()){
            Settings.guest.setBed(true);

            String bedNum = settings_bedNum.getText().toString();
            if(!bedNum.isEmpty())
                Settings.guest.setNumofbed(Integer.valueOf(bedNum));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean menu = MenuHandler.menu(item, this, SettingActivity.this);
        if(!menu)
            return super.onOptionsItemSelected(item);
        return menu;
    }

}