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
import com.myweddi.module.table.model.TablePlace;

import java.util.List;

public class TableListAdapter extends ArrayAdapter<String> {
    private Activity context;
    private List<TablePlace> tablePlaces;

    public TableListAdapter(Activity context, List<TablePlace> tablePlaces, List<String> titles) {
        super(context, R.layout.table_listview, titles);
        this.tablePlaces = tablePlaces;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r = convertView;
        ViewHolder viewHolder = null;

        if(r == null){
            LayoutInflater inflater = context.getLayoutInflater();
            r = inflater.inflate(R.layout.table_listview, null, true);
            viewHolder = new ViewHolder(r);
            r.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) r.getTag();
        }

        TablePlace tablePlace = tablePlaces.get(position);
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

        public ViewHolder(View view) {
            this.tableid = (TextView) view.findViewById(R.id.tableid);
            this.username = (TextView) view.findViewById(R.id.table_username);
        }
    }
}
