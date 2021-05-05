package com.myweddi.module.table.model;

import android.app.Activity;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.myweddi.R;

import java.util.ArrayList;
import java.util.List;

public class NewTableAddapter extends ArrayAdapter {

    private Activity context;
    List<String> titles;
    List<Integer> val = new ArrayList<>();
    List<EditText> editTextList = new ArrayList<>();

    public NewTableAddapter(Activity context, List<String> titles) {
        super(context, R.layout.newtable_layout, titles);
        this.titles = titles;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder = null;

        if(view == null){
            LayoutInflater inflater = context.getLayoutInflater();
            view = inflater.inflate(R.layout.newtable_layout, null, true);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }

        //TablePlace tablePlace = tablePlaces.get(position);
        viewHolder.tabelname.setText(titles.get(position));
        editTextList.add(viewHolder.tableVal);
        return view;
    }

    class ViewHolder{

        EditText tableVal;
        TextView tabelname;

        public ViewHolder(View view) {
            this.tableVal = view.findViewById(R.id.host_gift_customgift);
            this.tabelname = view.findViewById(R.id.host_gift_lp);
        }
    }

    @Nullable
    @Override
    public String getItem(int position) {
        EditText editText = editTextList.get(position);
        Editable text = editText.getText();
        String s = text.toString();
        return s;
    }
}
