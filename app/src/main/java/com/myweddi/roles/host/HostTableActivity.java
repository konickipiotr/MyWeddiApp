package com.myweddi.roles.host;

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
import com.myweddi.module.table.HostTableListAdapter;
import com.myweddi.module.table.TableListAdapter;
import com.myweddi.module.table.model.TablePlace;
import com.myweddi.module.table.model.TableWrapper;
import com.myweddi.roles.guest.TableActivity;
import com.myweddi.settings.Settings;
import com.myweddi.utils.MenuHandler;
import com.myweddi.utils.RequestUtils;
import com.myweddi.utils.Utils;
import com.squareup.picasso.Picasso;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class HostTableActivity extends AppCompatActivity {

    ImageView myProfilPhoto;
    ImageView tableSchema;
    private Button btable_create_tables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_table);
        setTitle("");

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);

        Utils.setProfilePhoto(myProfilPhoto, this, HostTableActivity.this);

        btable_create_tables = findViewById(R.id.bhosttable_create_tables);
        this.tableSchema = (ImageView) findViewById(R.id.hosttableImage);

//        btable_create_tables.setOnClickListener(v ->{
//            startActivity(new Intent(HostTableActivity.this, CreateTablesActivity.class));
//        });

//        TableAsync tableAsync = new TableAsync();
//        tableAsync.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean menu = MenuHandler.menu(item, this, HostTableActivity.this);
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

            ListView listView = (ListView) findViewById(R.id.hosttableListView);
            HostTableListAdapter hostTableListAdapter = new HostTableListAdapter(HostTableActivity.this, tableWrapper, getTitles(tableWrapper));
            listView.setAdapter(hostTableListAdapter);
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