package com.myweddi.roles.host;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.myweddi.R;
import com.myweddi.module.table.model.NewTableAddapter;
import com.myweddi.module.table.model.TableTempObject;
import com.myweddi.roles.guest.TableActivity;
import com.myweddi.settings.Settings;
import com.myweddi.utils.ListUtils;
import com.myweddi.utils.RequestUtils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class CreateTablesActivity extends AppCompatActivity {

    private Button btable_numOfTableConfirm, bNewTableSave;
    private EditText table_num_of_tables;
    private TextView table_error_msg;
    ListView newTablesListView;
    NewTableAddapter newTableAddapter;
    int num = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tables);

        btable_numOfTableConfirm = findViewById(R.id.btable_numOfTableConfirm);
        bNewTableSave = findViewById(R.id.bNewTableSave);
        table_num_of_tables = findViewById(R.id.table_num_of_tables);
        table_error_msg = findViewById(R.id.table_error_msg);

        btable_numOfTableConfirm.setOnClickListener(v ->
        {
            String sNum = table_num_of_tables.getText().toString();
            if(sNum == null || sNum.isEmpty())
                return;

            num = Integer.valueOf(sNum);
            if(num < 1)
                return;

            newTablesListView = (ListView) findViewById(R.id.new_tables_listView);
            List<String> titles = getListTitle(num);

            newTableAddapter = new NewTableAddapter(CreateTablesActivity.this, titles);
            newTablesListView.setAdapter(newTableAddapter);
            ListUtils.setDynamicHeight(newTablesListView);

        });

        bNewTableSave.setOnClickListener(v ->{

            List<Integer> capacity = new ArrayList<>();
            for (int a = 0; a < newTablesListView.getCount(); a++) {
                EditText et = (EditText) newTablesListView.getChildAt(a).findViewById(R.id.host_gift_customgift);
                String sval = et.getText().toString();

                if(sval.isEmpty()){
                    table_error_msg.setText("Pola nie mogą być puste");
                    return;
                }
                int iVal = Integer.valueOf(sval);
                if(iVal<1){
                    table_error_msg.setText("Ilośc miejsc nie może być mniejsza od 1");
                    return;
                }

                capacity.add(iVal);
            }
            TableTempObject tto = new TableTempObject(capacity, Settings.weddingid, Settings.user.getId());
            new SaveTablesInfoAsync(this).execute(tto);
        });
    }

    public List<String> getListTitle(int num){
        List<String> titles = new ArrayList<>();

        for(int i = 0; i < num; i++)
            titles.add(getResources().getString(R.string.table_table) + " " + String.valueOf(i + 1));
        return titles;
    }

    private static class SaveTablesInfoAsync extends AsyncTask<TableTempObject, Void, Void> {

        private final WeakReference<CreateTablesActivity> activityReference;

        public SaveTablesInfoAsync(CreateTablesActivity context) {
            this.activityReference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(TableTempObject... params) {
            RequestUtils requestUtils = new RequestUtils();
            RestTemplate restTemplate = requestUtils.getRestTemplate();
            HttpHeaders requestHeaders = requestUtils.getRequestHeaders();

            TableTempObject tto = params[0];
            String path = Settings.server_url + "/api/table";
            restTemplate.postForEntity(path, new HttpEntity<>(tto, requestHeaders), Void.class);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            CreateTablesActivity context = activityReference.get();
            context.finish();
            context.startActivity(new Intent(context, TableActivity.class));
        }
    }
}