package com.myweddi.async;

import android.os.AsyncTask;

import com.myweddi.model.post.Comment;
import com.myweddi.settings.Settings;
import com.myweddi.utils.RequestUtils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

public class AddComment extends AsyncTask<Comment, Void, Void> {

    @Override
    protected Void doInBackground(Comment... comments) {
        Comment comment = comments[0];
        RequestUtils requestUtils = new RequestUtils();
        RestTemplate restTemplate = requestUtils.getRestTemplate();
        HttpHeaders requestHeaders = requestUtils.getRequestHeaders();

        String path = Settings.server_url + "/api/post/addcomment";
        restTemplate.postForEntity(path, new HttpEntity<Comment>(comment, requestHeaders), Long.class);
        return null;
    }
}