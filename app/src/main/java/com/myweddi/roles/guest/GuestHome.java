package com.myweddi.roles.guest;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myweddi.R;
import com.myweddi.model.ListWrapper;
import com.myweddi.settings.Settings;
import com.myweddi.utils.CreatePost;
import com.myweddi.utils.CustomListview;
import com.myweddi.utils.RequestUtils;
import com.myweddi.view.PostView;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static com.myweddi.R.id.addComment;

public class GuestHome extends AppCompatActivity {

    ListView listView;
    Button bAddPost;
    ImageButton addComment;

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_home);
        setTitle("");

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);

        imageView = (ImageView) findViewById(R.id.myprofilphoto);
        Glide.with(this)
                .load("https://fwcdn.pl/fpo/71/07/707107/7648804.3.jpg")
                .circleCrop()
                .into(imageView);



        bAddPost = (Button) findViewById(R.id.bAddPostg);
        addComment = (ImageButton) findViewById(R.id.addComment);


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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection

        switch (item.getItemId()) {
            case R.id.bHome:
                Log.i("Menu","Home");
                startActivity(new Intent(GuestHome.this, GuestHome.class));
                return true;
            case R.id.bInfo:
                Log.i("Menu","Info");
                startActivity(new Intent(GuestHome.this, GuestInfo.class));
                return true;
            case R.id.bTable:
                Log.i("Menu","Stoły");
                startActivity(new Intent(GuestHome.this, TableActivity.class));
                return true;
            case R.id.bLogout:
                Log.i("Menu","Wyloguj");
                return true;
            case R.id.bOptions:
                Log.i("Menu","Opcje");
                startActivity(new Intent(GuestHome.this, SettingActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
            CustomListview customListview = new CustomListview(GuestHome.this, postViews, titles);
            listView.setAdapter(customListview);
        }
    }


}