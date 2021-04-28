package com.myweddi.roles.guest;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myweddi.R;
import com.myweddi.module.showpost.AddComment;
import com.myweddi.model.Photo;
import com.myweddi.module.showpost.model.Comment;
import com.myweddi.settings.Settings;
import com.myweddi.utils.ListUtils;
import com.myweddi.module.showpost.PostCommentAdapter;
import com.myweddi.module.showpost.listeners.RemovePostListener;
import com.myweddi.utils.OtherUtils;
import com.myweddi.utils.RequestUtils;
import com.myweddi.module.showpost.listeners.StarButtonListener;
import com.myweddi.module.showpost.view.CommentView;
import com.myweddi.module.showpost.view.PostView;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class PostActivity extends AppCompatActivity {
    private ImageView myProfilPhoto;
    private ImageButton addComment, removepost, btnStar;
    private EditText newComment;
    private Long postid;
    private TextView starNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        setTitle("");
        initialize();
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);

        OtherUtils.setProfilePhoto(myProfilPhoto, this, PostActivity.this);

        Intent intent = getIntent();
        postid = intent.getLongExtra("postid", -1);

        removepost.setOnClickListener(new RemovePostListener(postid, this));
        btnStar.setOnClickListener(new StarButtonListener(this, postid, starNum));


        FetchPost post = new FetchPost();
        post.execute(postid);
    }

    private void initialize(){
        addComment = (ImageButton) findViewById(R.id.addComment);
        newComment = (EditText) findViewById(R.id.newComment);
        removepost = (ImageButton) findViewById(R.id.removePost3);
        btnStar = (ImageButton) findViewById(R.id.addStar);
        starNum = (TextView) findViewById(R.id.starNum);

    }

    public void addComment(View view){
        String com = newComment.getText().toString();
        if(com == null || com.isEmpty())
            return;
        Comment comment = new Comment(postid, Settings.user.getId(), com);
        AddComment addComment = new AddComment();
        addComment.execute(comment);
        this.finish();
        startActivity(getIntent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting_menu, menu);
        return true;
    }

    class FetchPost extends AsyncTask<Long, Void, PostView> {
        @Override
        protected PostView doInBackground(Long... params) {

            RequestUtils requestUtils = new RequestUtils();
            RestTemplate restTemplate = requestUtils.getRestTemplate();
            HttpHeaders requestHeaders = requestUtils.getRequestHeaders();

            String path = Settings.server_url + "/api/post/" + Settings.weddingid + "/post/" + params[0];
            ResponseEntity<PostView> response = restTemplate.exchange(path,
                                                        HttpMethod.GET,
                                                        new HttpEntity<Object>(requestHeaders),
                                                        PostView.class);

            ObjectMapper mapper = new ObjectMapper();
            PostView post = mapper.convertValue(response.getBody(), new TypeReference<PostView>() {});
            return  post;
        }

        public List<String> getTitlesPhoto(PostView postView){
            List<String> dummy_titles = new ArrayList<>();

            int i = 0;
            for(Photo pv : postView.getPhotos()) {
                dummy_titles.add( "" + pv.getId());
            }
            return dummy_titles;
        }

        public List<String> getCommentTitle(PostView postView){
            List<String> dummy_titles = new ArrayList<>();

            int i = 0;
            for(CommentView cv : postView.getComments()) {
                dummy_titles.add( "" + cv.getId());
            }
            return dummy_titles;
        }

        @Override
        protected void onPostExecute(PostView postView) {

            ListView photoListView = (ListView) findViewById(R.id.photoListView);
            ListView commentListView = (ListView) findViewById(R.id.commentListView);
            List<String> titles = getTitlesPhoto(postView);

            PostPhotoListView postPhotoListView = new PostPhotoListView(PostActivity.this, postView.getPhotos(), getTitlesPhoto(postView));
            photoListView.setAdapter(postPhotoListView);

            PostCommentAdapter postCommentAdapter = new PostCommentAdapter(PostActivity.this, postView.getComments(), getCommentTitle(postView));
            commentListView.setAdapter(postCommentAdapter);

            ListUtils.setDynamicHeight(photoListView);
            ListUtils.setDynamicHeight(commentListView);

            if(postView.getUserid().equals(Settings.user.getId())) {
                removepost.setVisibility(View.VISIBLE);
            }else {
                removepost.setVisibility(View.GONE);
            }

            if(postView.isWeddiLike()){
                btnStar.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_on));
            }else{
                btnStar.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_off));
            }

            starNum.setText(Integer.toString(postView.getLikeNumber()));
        }
    }

}