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
import com.myweddi.enums.PostAccess;
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
import java.lang.ref.WeakReference;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class CreatePost extends AppCompatActivity {

    private Button bcreatePost, bCreatePost_Cancel;
    private ImageButton bTakePhoto, bGalleryPhoto, bDeletePhoto;
    private EditText postDescription;
    private LinearLayout tool_bar;
    private final List<Bitmap> bitmapList = new ArrayList<>();
    private final List<ImageView> photosList = new ArrayList<>();
    private final int postPhotoLimit = 6;
    public int selectedPhotoIndex = -1;

    private final int CAMERA_SRC = 100;
    private final int STORAGE_SRC = 101;
    private PostAccess postAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);


        Intent intent = getIntent();
        String postType = intent.getStringExtra("postType");
        this.postAccess = PostAccess.valueOf(postType);

        initialize();

        if(ContextCompat.checkSelfPermission(CreatePost.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(CreatePost.this, new String[]{
                    Manifest.permission.CAMERA
            }, CAMERA_SRC);
        }

        bTakePhoto.setOnClickListener(v -> {
            Intent intent12 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent12, 100);
        });

        bGalleryPhoto.setOnClickListener(v -> {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, STORAGE_SRC);
        });

        bcreatePost.setOnClickListener(v -> {

            Post post = new Post();
            post.setUserid(Settings.user.getId());
            post.setCreationdate(LocalDateTime.now());
            postDescription = findViewById(R.id.postDescription);
            post.setDescription(postDescription.getText().toString());

            if(postAccess.equals(PostAccess.PUBLIC))
                post.setPosttype(Posttype.PUBLIC);
            else
                post.setPosttype(Posttype.LOCAL);

            if(bitmapList.isEmpty() && (post.getDescription() == null || post.getDescription().isEmpty()))
                return;

            new AddPostTask(this).execute(post);

            Intent intent1 = new Intent(CreatePost.this, Home.class);
            intent1.putExtra("postType", postAccess.name());
            setResult(100, intent1);
            finish();
        });

        bDeletePhoto.setOnClickListener(v -> {
            bitmapList.remove(selectedPhotoIndex);

            for(ImageView i : photosList){
                i.setImageBitmap(null);
                i.setPadding(0,0,0,0);
                i.setBackgroundColor(Color.WHITE);
                i.setVisibility(View.GONE);
            }
            photosList.forEach(i -> i.setImageBitmap(null));

            for(int i = 0; i < bitmapList.size(); i++){
                photosList.get(i).setImageBitmap(bitmapList.get(i));
                photosList.get(i).setVisibility(View.VISIBLE);
            }
            tool_bar.setVisibility(View.GONE);
        });

        bCreatePost_Cancel.setOnClickListener(v -> {
            Intent intent1 = new Intent(CreatePost.this, Home.class);
            intent1.putExtra("postType", postAccess.name());
            setResult(100, intent1);
            finish();
        });
    }

    private void initialize(){
        photosList.add(findViewById(R.id.photo1));
        photosList.add(findViewById(R.id.photo2));
        photosList.add(findViewById(R.id.photo3));
        photosList.add(findViewById(R.id.photo4));
        photosList.add(findViewById(R.id.photo5));
        photosList.add(findViewById(R.id.photo6));
        bTakePhoto = findViewById(R.id.bTakePhoto);
        bGalleryPhoto = findViewById(R.id.bGalleryPhoto);
        bcreatePost = findViewById(R.id.bAddPost);
        bCreatePost_Cancel = findViewById(R.id.bCreatePost_Cancel);
        postDescription = findViewById(R.id.postDescription);
        tool_bar = findViewById(R.id.tool_bar);
        bDeletePhoto = findViewById(R.id.deletePhoto);

        for(int i = 0; i < photosList.size(); i++){
            photosList.get(i).setVisibility(View.GONE);
            photosList.get(i).setOnTouchListener(new PhotoToRemove(this, i));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data == null)
            return;

        int size = bitmapList.size();
        int idx = size;
        size++;
        if(requestCode == CAMERA_SRC){
            Bitmap newbitmap = (Bitmap) data.getExtras().get("data");
            if(size > postPhotoLimit){
                return;
            }
            bitmapList.add(newbitmap);
            photosList.get(idx).setImageBitmap(newbitmap);
            photosList.get(idx).setVisibility(View.VISIBLE);
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
                photosList.get(idx).setImageBitmap(newbitmap);
                photosList.get(idx).setVisibility(View.VISIBLE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public List<ImageView> getPhotosList() {
        return photosList;
    }

    private static class AddPostTask extends AsyncTask<Post, Void, Void> {

        private WeakReference<CreatePost> weakReference;

        public AddPostTask(CreatePost context) {
            this.weakReference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(Post... post) {

            CreatePost context = weakReference.get();
            RequestUtils requestUtils = new RequestUtils();
            RestTemplate restTemplate = requestUtils.getRestTemplate();
            HttpHeaders requestHeaders = requestUtils.getRequestHeaders();

            String path = Settings.server_url + "api/post";
            HttpEntity<Post> request1 = new HttpEntity<>(post[0], requestHeaders);
            ResponseEntity<Long> response = restTemplate.exchange(path,
                    HttpMethod.POST,
                    request1,
                    Long.class);

            if(context.bitmapList == null || context.bitmapList.isEmpty())
                return null;

            List<String> images = new ArrayList<>();
            for(int i = 0; i < context.bitmapList.size(); i++){
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                context.bitmapList.get(i).compress(Bitmap.CompressFormat.PNG, 50, stream);
                byte[] bitmapdata = stream.toByteArray();
                images.add(Base64.encodeToString(bitmapdata, Base64.NO_WRAP));

            }

            HttpHeaders header2 = requestUtils.getRequestHeaders();
            HttpEntity<Object> request2 = new HttpEntity<>(images, header2);
            path = Settings.server_url + "api/post/" + Settings.user.getId() + "/" + response.getBody();
            restTemplate.postForEntity(path, request2, Long.class);
            return null;
        }
    }
}
