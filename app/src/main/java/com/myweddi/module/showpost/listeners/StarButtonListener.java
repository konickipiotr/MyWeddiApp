package com.myweddi.module.showpost.listeners;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.myweddi.R;
import com.myweddi.module.showpost.model.WeddiLike;
import com.myweddi.settings.Settings;
import com.myweddi.utils.RequestUtils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class StarButtonListener implements View.OnClickListener {

    private Activity context;
    private Long postid;
    private ImageButton star;
    private TextView starNum;

    public StarButtonListener(Activity context, Long postid, TextView starNum) {
        this.context = context;
        this.postid = postid;
        this.starNum = starNum;
    }

    @Override
    public void onClick(View v) {

        star = (ImageButton) v.findViewById(R.id.addStar);
        new ChangeStarStatus().execute(this.postid);
    }

    private class ChangeStarStatus extends AsyncTask<Long, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Long... params) {

            RequestUtils requestUtils = new RequestUtils();
            RestTemplate restTemplate = requestUtils.getRestTemplate();
            HttpHeaders requestHeaders = requestUtils.getRequestHeaders();

            WeddiLike weddiLike = new WeddiLike(postid, Settings.user.getId());
            String path = Settings.server_url + "/api/post/changestar";
            ResponseEntity<Boolean> response = restTemplate.postForEntity(path, new HttpEntity<WeddiLike>(weddiLike, requestHeaders), Boolean.class);
            Boolean isLiked = response.getBody();
            return isLiked;
        }

        @Override
        protected void onPostExecute(Boolean isLiked) {
            super.onPostExecute(isLiked);
            int numOfStar = Integer.valueOf(starNum.getText().toString());
            if(isLiked){
                star.setImageDrawable(ContextCompat.getDrawable(context.getApplicationContext(),android.R.drawable.btn_star_big_on));
                numOfStar++;
            }else{
                star.setImageDrawable(ContextCompat.getDrawable(context.getApplicationContext(),android.R.drawable.btn_star_big_off));
                numOfStar--;
            }
            starNum.setText(Integer.toString(numOfStar));
            
        }
    }
}
