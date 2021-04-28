package com.myweddi.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.myweddi.ProfilePhotoActivity;
import com.myweddi.R;
import com.myweddi.roles.guest.GuestHome;
import com.myweddi.settings.Settings;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class OtherUtils {

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    public static void setProfilePhoto(ImageView myProfilPhoto, Activity activity,  Context packageContext){
        myProfilPhoto = (ImageView) activity.findViewById(R.id.myprofilphoto);
        Glide.with(activity)
                .load(Settings.profilePhotoBitmap)
                .circleCrop()
                .into(myProfilPhoto);

        myProfilPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
                activity.startActivity(new Intent(packageContext, ProfilePhotoActivity.class));
            }
        });
    }
}
