package com.myweddi.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.myweddi.ProfilePhotoActivity;
import com.myweddi.R;
import com.myweddi.settings.Settings;

import java.io.ByteArrayOutputStream;

public class Utils {

    public static void setProfilePhoto(Activity activity,  Context packageContext){
        ImageView myProfilPhoto = activity.findViewById(R.id.myprofilphoto);
        Glide.with(activity)
                .load(Settings.profilePhotoBitmap != null ? Settings.profilePhotoBitmap : activity.getResources().getDrawable(R.drawable.user))
                .circleCrop()
                .into(myProfilPhoto);

        myProfilPhoto.setOnClickListener(v -> activity.startActivity(new Intent(packageContext, ProfilePhotoActivity.class)));
    }

    public static String imgToString(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
        byte[] bitmapdata = stream.toByteArray();
        return Base64.encodeToString(bitmapdata, Base64.NO_WRAP);
    }
}
