package com.myweddi.module.createpost;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.myweddi.R;
import com.myweddi.module.showpost.model.Post;
import com.myweddi.module.showpost.model.Posttype;
import com.myweddi.roles.Home;
import com.myweddi.settings.Settings;
import com.myweddi.utils.RequestUtils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class CreatePost extends AppCompatActivity {

    private Button bcreatePost;
    private ImageButton bTakePhoto, bGalleryPhoto, bDeletePhoto;
    private EditText postDescription;
    private LinearLayout tool_bar;
    private List<Bitmap> bitmapList = new ArrayList<>();
    private List<ImageView> photsList = new ArrayList<>();
    private final int postPhotoLimit = 6;
    public int selectedPhotoIndex = -1;

    private final int CAMERA_SRC = 100;
    private final int STORAGE_SRC = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        initialize();

        if(ContextCompat.checkSelfPermission(CreatePost.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(CreatePost.this, new String[]{
                    Manifest.permission.CAMERA
            }, CAMERA_SRC);
        }

        bTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 100);
            }
        });

        bGalleryPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, STORAGE_SRC);
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
                post.setPosttype(Posttype.LOCAL);

                if(bitmapList.isEmpty() && (post.getDescription() == null || post.getDescription().isEmpty()))
                    return;

                AddPostTask addPostTask = new AddPostTask();
                addPostTask.execute(post);
                startActivity(new Intent(CreatePost.this, Home.class));
            }
        });

        bDeletePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitmapList.remove(selectedPhotoIndex);

                for(ImageView i : photsList){
                    i.setImageBitmap(null);
                    i.setPadding(0,0,0,0);
                    i.setBackgroundColor(Color.WHITE);
                    i.setVisibility(View.GONE);
                }
                photsList.forEach(i -> i.setImageBitmap(null));

                for(int i = 0; i < bitmapList.size(); i++){
                    photsList.get(i).setImageBitmap(bitmapList.get(i));
                    photsList.get(i).setVisibility(View.VISIBLE);
                }
                tool_bar.setVisibility(View.GONE);
            }
        });
    }

    private void initialize(){
        photsList.add((ImageView) findViewById(R.id.photo1));
        photsList.add((ImageView) findViewById(R.id.photo2));
        photsList.add((ImageView) findViewById(R.id.photo3));
        photsList.add((ImageView) findViewById(R.id.photo4));
        photsList.add((ImageView) findViewById(R.id.photo5));
        photsList.add((ImageView) findViewById(R.id.photo6));
        bTakePhoto = (ImageButton) findViewById(R.id.bTakePhoto);
        bGalleryPhoto = (ImageButton) findViewById(R.id.bGalleryPhoto);
        bcreatePost = (Button) findViewById(R.id.bAddPost);
        postDescription = (EditText)findViewById(R.id.postDescription);
        tool_bar = (LinearLayout) findViewById(R.id.tool_bar);
        bDeletePhoto = (ImageButton)  findViewById(R.id.deletePhoto);

        for(int i = 0; i < photsList.size(); i++){
            photsList.get(i).setVisibility(View.GONE);
            photsList.get(i).setOnTouchListener(new PhotoToRemove(this, i));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int size = bitmapList.size();
        int idx = size;
        size++;
        if(requestCode == CAMERA_SRC){
            Bitmap newbitmap = (Bitmap) data.getExtras().get("data");
            if(size > postPhotoLimit){
                return;
            }
            bitmapList.add(newbitmap);
            photsList.get(idx).setImageBitmap(newbitmap);
            photsList.get(idx).setVisibility(View.VISIBLE);
        }else if(requestCode == STORAGE_SRC){
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap newbitmap = BitmapFactory.decodeStream(imageStream);
                size++;
                if(size > postPhotoLimit){
                    return;
                }
                bitmapList.add(newbitmap);
                photsList.get(idx).setImageBitmap(newbitmap);
                photsList.get(idx).setVisibility(View.VISIBLE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public List<ImageView> getPhotsList() {
        return photsList;
    }

    public void setPhotsList(List<ImageView> photsList) {
        this.photsList = photsList;
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

            if(bitmapList == null || bitmapList.isEmpty())
                return null;

            List<String> images = new ArrayList<>();
            for(int i = 0; i < bitmapList.size(); i++){
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmapList.get(i).compress(Bitmap.CompressFormat.PNG, 50, stream);
                byte[] bitmapdata = stream.toByteArray();
                images.add(Base64.encodeToString(bitmapdata, Base64.NO_WRAP));

            }

            HttpHeaders header2 = requestUtils.getRequestHeaders();
            HttpEntity<Object> request2 = new HttpEntity<Object>(images, header2);
            path = Settings.server_url + "api/post/" + Settings.user.getId() + "/" + response.getBody();
            ResponseEntity<Long> response2 = restTemplate
                    .postForEntity(path, request2, Long.class);
            return null;
        }
    }
}
