package com.myweddi.utils;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;

import com.myweddi.model.post.Comment;
import com.myweddi.settings.Settings;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import javax.xml.validation.SchemaFactoryLoader;

public class CommentToRemoveListener implements View.OnClickListener {

    private Long commentId;
    private Activity context;

    public CommentToRemoveListener(Long commentId, Activity context) {
        this.commentId = commentId;
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        new RemoveRequest().execute(commentId);
        context.finish();
        context.startActivity(context.getIntent());
    }


    private class RemoveRequest extends AsyncTask<Long, Void, Void>{

        @Override
        protected Void doInBackground(Long... params) {

            RequestUtils requestUtils = new RequestUtils();
            RestTemplate restTemplate = requestUtils.getRestTemplate();
            HttpHeaders requestHeaders = requestUtils.getRequestHeaders();

            String path = Settings.server_url + "/api/post/deletecomment/" + params[0];
            restTemplate.exchange(path, HttpMethod.DELETE, new HttpEntity<Comment>(requestHeaders), Long.class);
            return null;
        }
    }
}
