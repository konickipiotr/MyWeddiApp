package com.myweddi;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.myweddi.model.User;
import com.myweddi.roles.Home;
import com.myweddi.settings.Settings;
import com.myweddi.utils.GetImgAsync;
import com.myweddi.utils.Utils;
import com.myweddi.utils.RequestUtils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ProfilePhotoActivity extends AppCompatActivity {
    private final int CAMERA_SRC = 100;
    private final int STORAGE_SRC = 200;
    private Bitmap profilePhotoBitmap;
    private ImageView profilePhotoImg;
    private Button bprofilePhoto_save, bprofilePhoto_cancel;
    private ImageButton bprofilePhoto_camera, bprofilePhoto_gallery, bprofilePhoto_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_photo);
        init();
        initListeneres();

        if(ContextCompat.checkSelfPermission(ProfilePhotoActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ProfilePhotoActivity.this, new String[]{
                    Manifest.permission.CAMERA
            }, CAMERA_SRC);
        }

    }

    private void init(){
        profilePhotoImg = findViewById(R.id.bprofilePhoto_photoImg);
        bprofilePhoto_save = findViewById(R.id.bprofilePhoto_save);
        bprofilePhoto_cancel = findViewById(R.id.bprofilePhoto_cancel);
        bprofilePhoto_camera = findViewById(R.id.bprofilePhoto_camera);
        bprofilePhoto_gallery = findViewById(R.id.bprofilePhoto_gallery);
        bprofilePhoto_delete = findViewById(R.id.bprofilePhoto_delete);
    }

    private void initListeneres(){
        bprofilePhoto_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_SRC);
            }
        });

        bprofilePhoto_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, STORAGE_SRC);
            }
        });

        bprofilePhoto_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profilePhotoBitmap = null;
                Glide.with(ProfilePhotoActivity.this)
                        .load(getResources().getDrawable(R.drawable.user))
                        .circleCrop()
                        .into(profilePhotoImg);
            }
        });

        bprofilePhoto_cancel.setOnClickListener(v -> {
            finish();
        });

        bprofilePhoto_save.setOnClickListener(v ->{
            try {
                Boolean isOk = new ProfilePhotoAsync().execute().get();
                if(isOk)
                    Settings.profilePhotoBitmap = profilePhotoBitmap;
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            finish();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data == null) return;


        if(requestCode == CAMERA_SRC){
            profilePhotoBitmap = (Bitmap) data.getExtras().get("data");
            profilePhotoImg.setImageBitmap(profilePhotoBitmap);
        }else if(requestCode == STORAGE_SRC){
            try {
            final Uri imageUri = data.getData();
            final InputStream imageStream = getContentResolver().openInputStream(imageUri);
            profilePhotoBitmap = BitmapFactory.decodeStream(imageStream);
            profilePhotoImg.setImageBitmap(profilePhotoBitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    class ProfilePhotoAsync extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            RequestUtils requestUtils = new RequestUtils();
            RestTemplate restTemplate = requestUtils.getRestTemplate();
            HttpHeaders requestHeaders = requestUtils.getRequestHeaders();

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            profilePhotoBitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
            byte[] bitmapdata = stream.toByteArray();
            List<String> sImages = new ArrayList<>();
            sImages.add(Base64.encodeToString(bitmapdata, Base64.NO_WRAP));

            String path = Settings.server_url + "api/profilephoto/" + Settings.user.getId() ;
            try {
                ResponseEntity<Void> response = restTemplate
                    .postForEntity(path, new HttpEntity<>(sImages, requestHeaders), Void.class);
            }catch (
            HttpClientErrorException e) {
                return false;
            }
            return true;
        }
    }
}