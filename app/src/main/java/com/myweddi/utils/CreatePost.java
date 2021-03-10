package com.myweddi.utils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myweddi.MainActivity;
import com.myweddi.R;
import com.myweddi.model.ListWrapper;
import com.myweddi.model.post.Post;
import com.myweddi.settings.Settings;
import com.myweddi.view.PostView;

import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class CreatePost extends AppCompatActivity {
    ImageView photo;
    Button bTakePhoto, bcreatePost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        photo = (ImageView) findViewById(R.id.photo);
        bTakePhoto = (Button) findViewById(R.id.bTakePhoto);
        bcreatePost = (Button) findViewById(R.id.bAddPost);

        if(ContextCompat.checkSelfPermission(CreatePost.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(CreatePost.this, new String[]{
                    Manifest.permission.CAMERA
            }, 100);
        }

        bTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 100);
            }
        });

        bcreatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Post post = new Post(2l, Settings.user.getId(), LocalDateTime.now(), "elo elo to opis z androida");

                AddPostTask addPostTask = new AddPostTask();
                addPostTask.execute(post);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            photo.setImageBitmap(bitmap);
        }
    }

    class AddPostTask extends AsyncTask<Post, Void, Void> {
        @Override
        protected Void doInBackground(Post... post) {
            HttpAuthentication authHeader = new HttpBasicAuthentication(Settings.username,Settings.passoword);
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setAuthorization(authHeader);
            requestHeaders.setContentType(MediaType.APPLICATION_JSON);
            //requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_FORM_URLENCODED));

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            String path = Settings.server_url + "api/post";

            ObjectMapper mapper = new ObjectMapper();
            String jsonObject = "";
            try {
                jsonObject = mapper.writeValueAsString(post);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            Log.i("XXXXXX", jsonObject);
            //HttpEntity<String> request = new HttpEntity<String>(jsonObject, requestHeaders);
            HttpEntity<Post> request = new HttpEntity<Post>(post[0], requestHeaders);
            //ResponseEntity<Long> result = restTemplate.postForEntity(path, request, Long.class);

            ResponseEntity<Long> response = restTemplate.exchange(path,
                    HttpMethod.POST,
                    request,
                    Long.class);

            return null;
        }
    }
}