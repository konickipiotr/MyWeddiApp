package com.myweddi.utils;

import android.widget.EditText;

import com.myweddi.module.weddinginfo.WeddingInfo;

import java.time.LocalDateTime;

public class DateUtils {

    public static LocalDateTime getWeddingLocalDateTime(EditText date, EditText time){
        if(date.getText().toString().isEmpty()) {
            return LocalDateTime.of(0,0,0,0,0);
        }
        String[] splitDate = date.getText().toString().split("-");
        int dd = Integer.valueOf(splitDate[0]);
        int MM = Integer.valueOf(splitDate[1]);
        int yyyy = Integer.valueOf(splitDate[2]);

        int hour = 0;
        int min = 0;
        if(!time.getText().toString().isEmpty()){
            String[] splitTime = time.getText().toString().split(":");
            hour = Integer.valueOf(splitDate[0]);
            if(splitTime.length > 1)
                min = Integer.valueOf(splitDate[1]);
        }
        return LocalDateTime.of(yyyy, MM, dd, hour, min);
    }

    public static String getWeddingDate(WeddingInfo weddingInfo){
        String result = "";
        LocalDateTime date = weddingInfo.getCeremenytime();
        if(date != null){
            String day = Integer.toString(date.getDayOfMonth());
            String month = Integer.toString(date.getMonthValue());
            String year = Integer.toString(date.getYear());


            result = align(day) + "-" + align(month) + "-" + year;
        }
        return result;
    }

    public static String getWeddingTime(WeddingInfo weddingInfo){
        String result = "";
        LocalDateTime date = weddingInfo.getCeremenytime();
        if(date != null){
            String hour = Integer.toString(date.getHour());
            String min = Integer.toString(date.getMinute());

            result = hour + ":" + align(min);
        }
        return result;
    }

    private static String align(String val){
        if(val.length() == 1)
            return "0" + val;
        else return val;
    }
}
