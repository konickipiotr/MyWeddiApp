package com.myweddi.module.table;

import android.app.Activity;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.myweddi.module.table.model.DiVal;
import com.myweddi.module.table.model.TableHelper;
import com.myweddi.module.table.model.TablePlace;
import com.myweddi.module.table.model.TableWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static com.myweddi.R.layout.support_simple_spinner_dropdown_item;

public class TableSelectedListener implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

    private TableWrapper tableWrapper;
    private TablePlace tablePlace;
    private Map<Long, String> assigned;
    private Map<Long, String> notassigned;
    private Activity context;
    List<TableHelper> helper;

    public TableSelectedListener(TableWrapper tableWrapper, TablePlace tablePlace, List<TableHelper> helper, Activity context) {
        this.tableWrapper = tableWrapper;
        this.tablePlace = tablePlace;
        this.helper = helper;
        this.assigned = tableWrapper.getAssigned();
        this.notassigned = tableWrapper.getNotassigned();
        this.context = context;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        DiVal diVal = (DiVal) parent.getSelectedItem();
        helper.get(position).setUserid(diVal.getId());

        tableWrapper.getNotassigned().remove(diVal.getId());
        tableWrapper.getAssigned().put(diVal.getId(), diVal.getName());



        Log.i("eeee", "ff" + diVal );
    }

//    private void update(){
//
//        List<TablePlace> tablePlaces = tableWrapper.getTablePlaces();
//        for(int i = 0; i < spinners.size(); i++){
//            TablePlace tablePlace = tablePlaces.get(i);
//
//            List<DiVal> diValList = new ArrayList<>();
//            Long firstId = this.tablePlace.getUserid();
//            diValList.add(new DiVal(firstId, this.tablePlace.getUsername()));
//            for(Map.Entry<Long, String> v : tableWrapper.getNotassigned().entrySet()) {
//                if(v.getKey().equals(firstId))
//                    continue;
//                diValList.add(new DiVal(v.getKey(), v.getValue()));
//            }
//
//            Spinner spinner = spinners.get(i);
//            spinner.setAdapter(new ArrayAdapter<DiVal>(context, support_simple_spinner_dropdown_item, diValList));
//        }
//    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DiVal diVal = (DiVal) parent.getSelectedItem();
        helper.get(position).setUserid(diVal.getId());

        tableWrapper.getNotassigned().remove(diVal.getId());
        tableWrapper.getAssigned().put(diVal.getId(), diVal.getName());



        Log.i("eeee", "ff" + diVal );
    }
}
