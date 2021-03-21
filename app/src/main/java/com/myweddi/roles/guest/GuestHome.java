package com.myweddi.roles.guest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myweddi.R;
import com.myweddi.model.ListWrapper;
import com.myweddi.settings.Settings;
import com.myweddi.utils.CreatePost;
import com.myweddi.utils.CustomListview;
import com.myweddi.view.PostView;

import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GuestHome extends AppCompatActivity {

    ListView listView;
    Button bAddPost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_home);


        bAddPost = (Button) findViewById(R.id.bAddPostg);

        FetchPosts lp = new FetchPosts();
        String path = Settings.server_url + "api/post/2/1";
        lp.execute(path);

//        if(ContextCompat.checkSelfPermission(GuestHome.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(GuestHome.this, new String[]{
//                    Manifest.permission.CAMERA
//            }, 100);
//        }

        bAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GuestHome.this, CreatePost.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting_menu, menu);
        return true;
    }

    class FetchPosts extends AsyncTask<String, List<PostView>, List<PostView>> {
        @Override
        protected List<PostView> doInBackground(String... params) {
            HttpAuthentication authHeader = new HttpBasicAuthentication(Settings.username,Settings.passoword);
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setAuthorization(authHeader);
            requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

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
            CustomListview customListview = new CustomListview(GuestHome.this, postViews, titles);
            listView.setAdapter(customListview);
        }
    }
}