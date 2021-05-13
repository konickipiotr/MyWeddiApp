package com.myweddi.roles;

import androidx.annotation.Nullable;
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
import com.myweddi.enums.PostAccess;
import com.myweddi.model.ListWrapper;
import com.myweddi.roles.host.EditInfoActivity;
import com.myweddi.settings.Settings;
import com.myweddi.module.createpost.CreatePost;
import com.myweddi.module.showpost.PostListAdapter;
import com.myweddi.utils.ListUtils;
import com.myweddi.utils.MapsActivity;
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

    private ListView listView;
    private Button bAddPost, private_posts, public_posts, more_posts;

    private ImageView myProfilPhoto;
    private int pages = 1;
    PostAccess postAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("");

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);

        Utils.setProfilePhoto(this, Home.this);
        init();
        updateData();

        bAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, CreatePost.class);
                intent.putExtra("postType", postAccess.name());
                startActivityForResult(intent, 100);
            }
        });
    }

    private void updateData(){
        String path;
        if(this.postAccess == null || this.postAccess.equals(PostAccess.PRIVATE)) {
            this.postAccess = PostAccess.PRIVATE;
            path = Settings.server_url + "/api/post/" + Settings.weddingid + "/1";
        }
        else {
            this.postAccess = PostAccess.PUBLIC;
            path = Settings.server_url + "/api/post/public/1";
        }

        new FetchPosts().execute(path);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) return;

        if (requestCode == 100) {
            String postType = data.getStringExtra("postType");
            this.postAccess = PostAccess.valueOf(postType);
            updateData();
        }
    }

    private void init(){
        bAddPost = (Button) findViewById(R.id.bAddPostg);
        private_posts = findViewById(R.id.private_posts);
        public_posts = findViewById(R.id.public_posts);
        more_posts = findViewById(R.id.more_posts);

        if(Settings.user.getRole().equals("HOST")){
            private_posts.setVisibility(View.VISIBLE);
            public_posts.setVisibility(View.VISIBLE);

            private_posts.setOnClickListener(v -> {
                this.postAccess = PostAccess.PRIVATE;
                String path = Settings.server_url + "/api/post/" + Settings.weddingid + "/1";
                new FetchPosts().execute(path);
            });

            public_posts.setOnClickListener(v -> {
                this.postAccess = PostAccess.PUBLIC;
                String path = Settings.server_url + "/api/post/public/1";
                new FetchPosts().execute(path);
            });
        }

        more_posts.setOnClickListener(v -> {
            pages++;
            if(postAccess.equals(PostAccess.PRIVATE)){
                String path = Settings.server_url + "/api/post/" + Settings.weddingid + "/" + pages;
                new FetchPosts().execute(path);
            }else {
                String path = Settings.server_url + "/api/post/public/" + pages;
                new FetchPosts().execute(path);
            }
        });
    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        Utils.setProfilePhoto(this, Home.this);
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
            PostListAdapter postListAdapter = new PostListAdapter(Home.this, postViews, titles, postAccess);
            listView.setAdapter(postListAdapter);

            ListUtils.setDynamicHeight(listView);
        }
    }


}