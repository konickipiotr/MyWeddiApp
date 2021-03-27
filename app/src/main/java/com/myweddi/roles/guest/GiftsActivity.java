package com.myweddi.roles.guest;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.myweddi.R;

public class GiftsActivity extends AppCompatActivity {
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gifts);
        setTitle("");

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);

        imageView = (ImageView) findViewById(R.id.myprofilphoto);
        Glide.with(this)
                .load("https://fwcdn.pl/fpo/71/07/707107/7648804.3.jpg")
                .circleCrop()
                .into(imageView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.bHome:
                Log.i("Menu","Home");
                startActivity(new Intent(GiftsActivity.this, GuestHome.class));
                return true;
            case R.id.bInfo:
                Log.i("Menu","Info");
                startActivity(new Intent(GiftsActivity.this, GuestInfo.class));
                return true;
            case R.id.bTable:
                Log.i("Menu","Stoły");
                startActivity(new Intent(GiftsActivity.this, TableActivity.class));
                return true;
            case R.id.bLogout:
                Log.i("Menu","Wyloguj");
                return true;
            case R.id.bOptions:
                Log.i("Menu","Opcje");
                startActivity(new Intent(GiftsActivity.this, SettingActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}