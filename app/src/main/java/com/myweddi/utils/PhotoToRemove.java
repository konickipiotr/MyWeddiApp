package com.myweddi.utils;

import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.myweddi.R;

public class PhotoToRemove implements View.OnTouchListener {

    private CreatePost createPost;
    int idx = -1;

    public PhotoToRemove(CreatePost createPost, int idx) {
        this.createPost = createPost;
        this.idx = idx;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            LinearLayout tool_bar = (LinearLayout) createPost.findViewById(R.id.tool_bar);
            tool_bar.setVisibility(View.VISIBLE);
            this.createPost.selectedPhotoIndex = idx;
            ImageView iv = this.createPost.getPhotsList().get(idx);
            for(ImageView i : this.createPost.getPhotsList()){
                i.setPadding(0,0,0,0);
                iv.setBackgroundColor(Color.WHITE);
            }
            iv.setBackgroundColor(Color.RED);
            iv.setPadding(5,5,5,5);
        }
        return false;
    }
}
