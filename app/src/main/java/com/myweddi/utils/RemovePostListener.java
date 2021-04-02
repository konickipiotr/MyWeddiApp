package com.myweddi.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;

import com.myweddi.model.post.Comment;
import com.myweddi.roles.guest.GuestHome;
import com.myweddi.settings.Settings;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

public class RemovePostListener implements View.OnClickListener {

    private Long postid;
    private Activity context;

    public RemovePostListener(Long postid, Activity context) {
        this.postid = postid;
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        new RemovePostRequest().execute(postid);
        context.finish();
        context.startActivity(new Intent(context, GuestHome.class));
    }

    private class RemovePostRequest extends AsyncTask<Long, Void, Void> {

        @Override
        protected Void doInBackground(Long... params) {

            RequestUtils requestUtils = new RequestUtils();
            RestTemplate restTemplate = requestUtils.getRestTemplate();
            HttpHeaders requestHeaders = requestUtils.getRequestHeaders();

            String path = Settings.server_url + "/api/post/deletepost/" + params[0];
            restTemplate.exchange(path, HttpMethod.DELETE, new HttpEntity<Comment>(requestHeaders), Long.class);
            return null;
        }
    }
}
