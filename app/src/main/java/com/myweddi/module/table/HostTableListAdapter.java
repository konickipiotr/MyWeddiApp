package com.myweddi.module.table;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.myweddi.R;
import com.myweddi.module.table.model.TableHelper;
import com.myweddi.module.table.model.TablePlace;
import com.myweddi.module.table.model.TableWrapper;

import java.util.ArrayList;
import java.util.List;

public class HostTableListAdapter extends ArrayAdapter<String> {
    private Activity context;
    private TableWrapper tableWrapper;

    private List<TableHelper> helper = new ArrayList<>();

    public HostTableListAdapter(Activity context, TableWrapper tableWrapper, List<String> titles) {
        super(context, R.layout.host_table_listview, titles);
        this.tableWrapper = tableWrapper;
        this.context = context;

        for(TablePlace tp : tableWrapper.getTablePlaces()){
            TableHelper th = new TableHelper();
            th.setUserid(tp.getUserid());
            th.setPlaceid(tp.getPlaceid());
            th.setTableid(tp.getTableid());
            helper.add(th);
        }
    }
    
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r = convertView;
        ViewHolder viewHolder = null;

        if(r == null){
            LayoutInflater inflater = context.getLayoutInflater();
            r = inflater.inflate(R.layout.host_table_listview, null, true);
            viewHolder = new ViewHolder(r);
            r.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) r.getTag();
        }

        TablePlace tablePlace = tableWrapper.getTablePlaces().get(position);

//        viewHolder.host_assignedList.setAdapter(null);
//        List<DiVal> diValList = new ArrayList<>();
//        Long firstId = tablePlace.getUserid();
//        diValList.add(new DiVal(firstId, tablePlace.getUsername()));
//        for(Map.Entry<Long, String> v : tableWrapper.getNotassigned().entrySet()) {
//            if(v.getKey().equals(firstId))
//                continue;
//            diValList.add(new DiVal(v.getKey(), v.getValue()));
//        }

//        viewHolder.host_assignedList.setAdapter(new ArrayAdapter<DiVal>(context, support_simple_spinner_dropdown_item, diValList));
//        viewHolder.host_assignedList.setOnItemSelectedListener(new TableSelectedListener(tableWrapper, tablePlace, helper , context));

        viewHolder.tableid.setText(Integer.toString(tablePlace.getTableid()));
        String username = tablePlace.getUsername();
        if(username == null || username.isEmpty()){
            viewHolder.username.setText("Brak");
        }else {
            viewHolder.username.setText(tablePlace.getUsername());
        }

        return r;
    }

    class ViewHolder{

        TextView tableid;
        TextView username;
        //Spinner host_assignedList;

        public ViewHolder(View view) {
            this.tableid = (TextView) view.findViewById(R.id.tableid);
            this.username = (TextView) view.findViewById(R.id.host_table_username);
            //this.host_assignedList = view.findViewById(R.id.host_assignedList);
        }
    }
}
