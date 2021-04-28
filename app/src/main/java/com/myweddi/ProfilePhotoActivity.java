package com.myweddi;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.myweddi.model.User;
import com.myweddi.roles.guest.GuestHome;
import com.myweddi.settings.Settings;
import com.myweddi.utils.OtherUtils;
import com.myweddi.utils.RequestUtils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ProfilePhotoActivity extends AppCompatActivity {
    private final int CAMERA_SRC = 100;
    private Bitmap profilePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_photo);

        if(ContextCompat.checkSelfPermission(ProfilePhotoActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ProfilePhotoActivity.this, new String[]{
                    Manifest.permission.CAMERA
            }, CAMERA_SRC);
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_SRC);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAMERA_SRC){
            profilePhoto = (Bitmap) data.getExtras().get("data");
            new ProfilePhotoAsync().execute();
        }

//        finish();
//        startActivity(new Intent(ProfilePhotoActivity.this, GuestHome.class));
    }

    class ProfilePhotoAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            RequestUtils requestUtils = new RequestUtils();
            RestTemplate restTemplate = requestUtils.getRestTemplate();
            HttpHeaders requestHeaders = requestUtils.getRequestHeaders();

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            profilePhoto.compress(Bitmap.CompressFormat.PNG, 50, stream);
            byte[] bitmapdata = stream.toByteArray();
            List<String> sImages = new ArrayList<>();
            sImages.add(Base64.encodeToString(bitmapdata, Base64.NO_WRAP));

            String path = Settings.server_url + "api/profilephoto/" + Settings.user.getId() ;
            ResponseEntity<Void> response = restTemplate
                    .postForEntity(path, new HttpEntity<>(sImages, requestHeaders), Void.class);

            path= Settings.server_url + "/api/user";
            ResponseEntity<User> response2 = restTemplate.exchange(path, HttpMethod.GET, new HttpEntity<Object>(requestHeaders), User.class);
            User user = response2.getBody();
            Settings.user = user;
            if(user.getWebAppPath() != null){
                String fullPath = Settings.server_url + user.getWebAppPath();
                Settings.profilePhotoBitmap = OtherUtils.getBitmapFromURL(fullPath);
            }
            
            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {
            finish();
            startActivity(new Intent(ProfilePhotoActivity.this, GuestHome.class));
        }
    }
}