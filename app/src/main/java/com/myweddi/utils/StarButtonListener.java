package com.myweddi.utils;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageButton;

import com.myweddi.R;
import com.myweddi.model.post.Comment;
import com.myweddi.settings.Settings;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class StarButtonListener implements View.OnClickListener {

    private Activity context;
    private Long postid;
    private ImageButton star;

    public StarButtonListener(Activity context, Long postid) {
        this.context = context;
        this.postid = postid;
    }

    @Override
    public void onClick(View v) {
        star = (ImageButton)  v.findViewById(R.id.addStar);
    }

    private class ChangerStarStatus extends AsyncTask<Long, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Long... params) {

            RequestUtils requestUtils = new RequestUtils();
            RestTemplate restTemplate = requestUtils.getRestTemplate();
            HttpHeaders requestHeaders = requestUtils.getRequestHeaders();

            LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("postid", postid);
            body.add("userid", Settings.user.getId());

            String path = Settings.server_url + "/api/post/changerstar";
            ResponseEntity<Boolean> respons = restTemplate.exchange(path, HttpMethod.DELETE, new HttpEntity<Comment>(requestHeaders), Boolean.class);
            Boolean isLiked = respons.getBody();

            return isLiked;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            
        }
    }
}
