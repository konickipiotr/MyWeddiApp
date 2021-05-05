package com.myweddi.roles;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myweddi.R;
import com.myweddi.model.ListWrapper;
import com.myweddi.settings.Settings;
import com.myweddi.module.createpost.CreatePost;
import com.myweddi.module.showpost.PostListAdapter;
import com.myweddi.utils.MenuHandler;
import com.myweddi.utils.Utils;
import com.myweddi.utils.RequestUtils;
import com.myweddi.module.showpost.view.PostView;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    ListView listView;
    Button bAddPost;
    ImageButton addComment;

    ImageView myProfilPhoto;
    private final int CAMERA_SRC = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_home);
        setTitle("");

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);

        Utils.setProfilePhoto(myProfilPhoto, this, Home.this);

        bAddPost = (Button) findViewById(R.id.bAddPostg);
        addComment = (ImageButton) findViewById(R.id.addComment);


        FetchPosts lp = new FetchPosts();
        String path = Settings.server_url + "/api/post/2/1";
        lp.execute(path);


        bAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, CreatePost.class));
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean menu = MenuHandler.menu(item, this, Home.this);
        if(!menu)
            return super.onOptionsItemSelected(item);
        return menu;
    }

    class FetchPosts extends AsyncTask<String, List<PostView>, List<PostView>> {
        @Override
        protected List<PostView> doInBackground(String... params) {

            RequestUtils requestUtils = new RequestUtils();
            RestTemplate restTemplate = requestUtils.getRestTemplate();
            HttpHeaders requestHeaders = requestUtils.getRequestHeaders();

            ResponseEntity<ListWrapper> response = restTemplate.exchange(params[0],
                    HttpMethod.GET,
                    new HttpEntity<Object>(requestHeaders),
                    ListWrapper.class);
            ObjectMapper mapper = new ObjectMapper();
            ListWrapper<PostView> posts = mapper.convertValue(response.getBody(), new TypeReference<ListWrapper<PostView>>() {});
            return  posts.getList();
        }

        public List<String> getTitles(List<PostView> postViews){
            List<String> title_list = new ArrayList<>();
            for(PostView pv : postViews) {
                title_list.add(pv.getUsername());
            }
            return title_list;
        }

        @Override
        protected void onPostExecute(List<PostView> postViews) {
            ListView listView = (ListView) findViewById(R.id.listview);
            List<String> titles = getTitles(postViews);
            PostListAdapter postListAdapter = new PostListAdapter(Home.this, postViews, titles);
            listView.setAdapter(postListAdapter);
        }
    }


}