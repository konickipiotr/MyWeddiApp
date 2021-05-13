package com.myweddi.roles.guest;

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
import android.widget.ImageView;
import android.widget.ListView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myweddi.R;
import com.myweddi.module.table.model.TablePlace;
import com.myweddi.module.table.model.TableWrapper;
import com.myweddi.roles.host.CreateTablesActivity;
import com.myweddi.settings.Settings;
import com.myweddi.utils.MenuHandler;
import com.myweddi.utils.Utils;
import com.myweddi.utils.RequestUtils;
import com.myweddi.module.table.TableListAdapter;
import com.squareup.picasso.Picasso;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class TableActivity extends AppCompatActivity {

    ImageView myProfilPhoto;
    ImageView tableSchema;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        setTitle("");

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);

        Utils.setProfilePhoto(this, TableActivity.this);

        this.tableSchema = findViewById(R.id.tableImage);

        TableAsync tableAsync = new TableAsync();
        tableAsync.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean menu = MenuHandler.menu(item, this, TableActivity.this);
        if(!menu)
            return super.onOptionsItemSelected(item);
        return menu;
    }

    class TableAsync extends AsyncTask<Void,  Void, TableWrapper> {

        @Override
        protected TableWrapper doInBackground(Void... voids) {
            RequestUtils requestUtils = new RequestUtils();
            RestTemplate restTemplate = requestUtils.getRestTemplate();
            HttpHeaders requestHeaders = requestUtils.getRequestHeaders();

            String path = Settings.server_url + "/api/table/" + Settings.weddingid;
            ResponseEntity<TableWrapper> response = restTemplate.exchange(
                    path,
                    HttpMethod.GET,
                    new HttpEntity<Object>(requestHeaders),
                    TableWrapper.class);

            ObjectMapper mapper = new ObjectMapper();
            TableWrapper tableWrapper = mapper.convertValue(response.getBody(), new TypeReference<TableWrapper>() {});
            return  tableWrapper;
        }

        @Override
        protected void onPostExecute(TableWrapper tableWrapper) {

            String webAppPath = tableWrapper.getTables().getWebAppPath();
            if(webAppPath != null && !webAppPath.isEmpty()){
                Picasso.get().load(Settings.server_url + webAppPath)
                        .into(tableSchema);
            }else {
                tableSchema.setVisibility(View.GONE);
            }

            ListView listView = findViewById(R.id.tableListView);
            TableListAdapter tableListAdapter = new TableListAdapter(TableActivity.this, tableWrapper.getTablePlaces(), getTitles(tableWrapper));
            listView.setAdapter(tableListAdapter);
        }

        private List<String> getTitles(TableWrapper tableWrapper){
            List<String> titles = new ArrayList<>();

            for(TablePlace tp : tableWrapper.getTablePlaces()){
                titles.add(tp.getUsername());
            }
            return titles;
        }
    }
}