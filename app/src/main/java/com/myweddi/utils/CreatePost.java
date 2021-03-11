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
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myweddi.MainActivity;
import com.myweddi.R;
import com.myweddi.model.ListWrapper;
import com.myweddi.model.post.Post;
import com.myweddi.roles.guest.GuestHome;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class CreatePost extends AppCompatActivity {
    ImageView photo;
    Button bTakePhoto, bcreatePost;
    EditText postDescription;
    Bitmap bitmap;
    String encodedString;

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

                Post post = new Post();
                post.setUserid(Settings.user.getId());
                post.setCreationdate(LocalDateTime.now());
                postDescription = (EditText)findViewById(R.id.postDescription);
                post.setDescription(postDescription.getText().toString());
                post.setWeddingid(Settings.user.getWeddingid());

                AddPostTask addPostTask = new AddPostTask();
                addPostTask.execute(post);
                startActivity(new Intent(CreatePost.this, GuestHome.class));
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100){
            bitmap = (Bitmap) data.getExtras().get("data");
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

            HttpEntity<Post> request = new HttpEntity<Post>(post[0], requestHeaders);

            ResponseEntity<Long> response = restTemplate.exchange(path,
                    HttpMethod.POST,
                    request,
                    Long.class);


            LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
            byte[] byte_arr = stream.toByteArray();
            encodedString = Base64.encodeToString(byte_arr, 0);
            body.add("images", byte_arr);

            path = Settings.server_url + "api/post/" + Settings.user.getId() + "/" + response.getBody();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            headers.setAuthorization(authHeader);
            HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            restTemplate.postForObject(path, requestEntity, String.class);


            return null;
        }
    }
}