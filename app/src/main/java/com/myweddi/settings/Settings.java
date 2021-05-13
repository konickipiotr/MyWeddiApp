package com.myweddi.settings;

import android.graphics.Bitmap;

import com.myweddi.model.Guest;
import com.myweddi.model.User;

public class Settings {

    public static String username = "";
    public static String passoword = "";
    public static String hashedpassword = "";
    //public static final String server_url = "http://80.211.245.217:8081";
    public static final String server_url = "http://10.0.2.2:8081";
    public static User user;
    public static Guest guest;
    public static Long weddingid;

    public static Bitmap profilePhotoBitmap;
}
