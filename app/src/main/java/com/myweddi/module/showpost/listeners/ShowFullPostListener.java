package com.myweddi.module.showpost.listeners;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.myweddi.R;
import com.myweddi.roles.guest.PostActivity;

public class ShowFullPostListener implements View.OnClickListener{

    private Long postid;
    private Activity context;

    public ShowFullPostListener(Long postid, Activity context) {
        this.postid = postid;
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        Intent postintent = new Intent(context, PostActivity.class);
        postintent.putExtra("postid", postid);
        context.startActivity(postintent);
    }
}
