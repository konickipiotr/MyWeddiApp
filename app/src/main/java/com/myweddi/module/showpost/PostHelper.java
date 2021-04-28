package com.myweddi.module.showpost;

import android.app.Activity;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.myweddi.model.Photo;
import com.myweddi.module.showpost.listeners.CommentToRemoveListener;
import com.myweddi.module.showpost.listeners.RemovePostListener;
import com.myweddi.module.showpost.listeners.StarButtonListener;
import com.myweddi.module.showpost.view.CommentView;
import com.myweddi.module.showpost.view.PostView;
import com.myweddi.settings.Settings;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostHelper {

    private PostListAdapter.ViewHolder viewHolder;
    private PostView postView;
    private Activity context;


    public PostHelper(PostListAdapter.ViewHolder viewHolder, PostView postView, Activity context) {
        this.viewHolder = viewHolder;
        this.postView = postView;
        this.context = context;
    }

    public void setMainPhoto(){
        List<Photo> photos = postView.getPhotos();
        if(photos != null && !photos.isEmpty()){
            if(photos.get(0) != null && !photos.isEmpty()){
                String path =  Settings.server_url + photos.get(0).getWebAppPath();
                Picasso.get().load(path).into(viewHolder.photo1);
            }

        }else{
            viewHolder.photo1.setVisibility(View.GONE);
        }
    }

    public void setRemovePost(){

        if(Settings.user.getRole().equals("HOST") ||
                postView.getUserid().equals(Settings.user.getId())){
            viewHolder.removePost.setVisibility(View.VISIBLE);
            viewHolder.removePost.setOnClickListener(new RemovePostListener(postView.getId(), context));
        }
    }

    public void setStars(){
        if(postView.isWeddiLike()){
            viewHolder.addStar.setImageDrawable(ContextCompat.getDrawable(context.getApplicationContext(),android.R.drawable.btn_star_big_on));
        }else{
            viewHolder.addStar.setImageDrawable(ContextCompat.getDrawable(context.getApplicationContext(),android.R.drawable.btn_star_big_off));
        }
        viewHolder.addStar.setOnClickListener(new StarButtonListener(context, postView.getId(), viewHolder.starNum));
        viewHolder.starNum.setText(Integer.toString(postView.getLikeNumber()));
    }

    public void setComments(List<CommentView> comments){
        if(comments.isEmpty()){
            viewHolder.lastComment.setVisibility(View.GONE);
            viewHolder.secondLastComment.setVisibility(View.GONE);
        }
        else {
            CommentView cv = comments.get(comments.size() - 1);
            Picasso.get().load(Settings.server_url + cv.getUserphoto()).into(viewHolder.cProfilPhoto);
            viewHolder.userCom.setText(cv.getUsername());
            viewHolder.comentData.setText(cv.getPostdatetime());
            viewHolder.commentContent.setText(cv.getContent());
            if(cv.getUserid().equals(Settings.user.getId())){
                viewHolder.removeComment1.setVisibility(View.VISIBLE);
                viewHolder.removeComment1.setOnClickListener(new CommentToRemoveListener(cv.getId(), context));
            }
        }

        if(comments.size() > 1){
            CommentView cv = comments.get(comments.size() - 2);
            Picasso.get().load(Settings.server_url + cv.getUserphoto()).into(viewHolder.cProfilPhoto2);
            viewHolder.userCom2.setText(cv.getUsername());
            viewHolder.comentData2.setText(cv.getPostdatetime());
            viewHolder.commentContent2.setText(cv.getContent());
            if(cv.getUserid().equals(Settings.user.getId())){
                viewHolder.removeComment2.setVisibility(View.VISIBLE);
                viewHolder.removeComment2.setOnClickListener(new CommentToRemoveListener(cv.getId(), context));
            }
        }
    }
}
