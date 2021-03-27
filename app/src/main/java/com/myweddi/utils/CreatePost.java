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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.myweddi.R;
import com.myweddi.model.post.Post;
import com.myweddi.roles.guest.GuestHome;
import com.myweddi.settings.Settings;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;


public class CreatePost extends AppCompatActivity {
    ImageView photo;
    Button bTakePhoto, bcreatePost;
    EditText postDescription;
    Bitmap bitmap;

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

                if(bitmap == null && (post.getDescription() == null || post.getDescription().isEmpty()))
                    return;

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

            RequestUtils requestUtils = new RequestUtils();
            RestTemplate restTemplate = requestUtils.getRestTemplate();
            HttpHeaders requestHeaders = requestUtils.getRequestHeaders();


            String path = Settings.server_url + "api/post";
            HttpEntity<Post> request1 = new HttpEntity<Post>(post[0], requestHeaders);
            ResponseEntity<Long> response = restTemplate.exchange(path,
                    HttpMethod.POST,
                    request1,
                    Long.class);

            if(bitmap == null)
                return null;

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
            byte[] bitmapdata = stream.toByteArray();

            MultiValueMap<String, Object> body2 = new LinkedMultiValueMap<>();
            body2.add("images", Base64.encodeToString(bitmapdata, Base64.NO_WRAP));

            HttpHeaders header2 = requestUtils.getRequestHeaders();
            HttpEntity<MultiValueMap<String, Object>> request2 = new HttpEntity<>(body2, header2);
            path = Settings.server_url + "api/post/" + Settings.user.getId() + "/" + response.getBody();
            ResponseEntity<Long> response2 = restTemplate
                    .postForEntity(path, request2, Long.class);
            return null;
        }
    }


}