package com.myweddi.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import com.myweddi.MainActivity;
import com.myweddi.R;
import com.myweddi.roles.guest.GiftsActivity;
import com.myweddi.roles.Home;
import com.myweddi.roles.WeddingInfoActivity;
import com.myweddi.roles.guest.SettingActivity;
import com.myweddi.roles.guest.TableActivity;
import com.myweddi.roles.host.HostGiftActivity;
import com.myweddi.roles.host.HostTableActivity;
import com.myweddi.settings.Settings;

public class MenuHandler {

    public static boolean menu(MenuItem item, Activity context, Context packageContext){
        switch (Settings.user.getRole()){
            case "HOST": return hostMenu(item, context, packageContext);
            case "GUEST": return guestMenu(item, context, packageContext);
            default:
                return false;
        }
    }

    public static boolean guestMenu(MenuItem item, Activity context, Context packageContext){

        switch (item.getItemId()) {
            case R.id.bHome:
                context.finish();
                context.startActivity(new Intent(packageContext, Home.class));
                return true;
            case R.id.bInfo:
                context.startActivity(new Intent(packageContext, WeddingInfoActivity.class));
                return true;
            case R.id.bGifts:
                context.startActivity(new Intent(packageContext, GiftsActivity.class));
                return true;
            case R.id.bTable:
                context.startActivity(new Intent(packageContext, TableActivity.class));
                return true;
            case R.id.bLogout:
                context.finish();
                Settings.user = null;
                Settings.profilePhotoBitmap = null;
                Settings.username = null;
                Settings.passoword = null;
                context.startActivity(new Intent(packageContext, MainActivity.class));
                return true;
            case R.id.bOptions:
                context.startActivity(new Intent(packageContext, SettingActivity.class));
                return true;
            default:
                return false;
        }
    }

    public static boolean hostMenu(MenuItem item, Activity context, Context packageContext){

        switch (item.getItemId()) {
            case R.id.bHome:
                context.finish();
                context.startActivity(new Intent(packageContext, Home.class));
                return true;
            case R.id.bInfo:
                context.startActivity(new Intent(packageContext, WeddingInfoActivity.class));
                return true;
            case R.id.bGifts:
                context.startActivity(new Intent(packageContext, HostGiftActivity.class));
                return true;
            case R.id.bTable:
                context.startActivity(new Intent(packageContext, HostTableActivity.class));
                return true;
            case R.id.bLogout:
                context.finish();
                Settings.user = null;
                Settings.profilePhotoBitmap = null;
                Settings.username = null;
                Settings.passoword = null;
                context.startActivity(new Intent(packageContext, MainActivity.class));
                return true;
            case R.id.bOptions:
                context.startActivity(new Intent(packageContext, SettingActivity.class));
                return true;
            default:
                return false;
        }
    }
}
