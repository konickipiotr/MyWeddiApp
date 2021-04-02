package com.myweddi.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.myweddi.R;
import com.myweddi.async.AddComment;
import com.myweddi.model.Photo;
import com.myweddi.model.post.Comment;
import com.myweddi.roles.guest.PostActivity;
import com.myweddi.settings.Settings;
import com.myweddi.view.CommentView;
import com.myweddi.view.PostView;
import com.squareup.picasso.Picasso;


import java.util.List;

public class CustomListview extends ArrayAdapter<String> {
    private Activity context;
    private List<PostView> postlist;

    public CustomListview(Activity context, List<PostView> postlist, List<String> titles) {
        super(context, R.layout.mini_post, titles);
        this.postlist = postlist;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r = convertView;
        ViewHolder viewHolder = null;

        if(r == null){
            LayoutInflater inflater = context.getLayoutInflater();
            r = inflater.inflate(R.layout.mini_post, null, true);
            viewHolder = new ViewHolder(r);
            r.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) r.getTag();
        }

        PostView postView = postlist.get(position);
        List<Photo> photos = postView.getPhotos();
        if(photos != null && !photos.isEmpty()){
            if(photos.get(0) != null && !photos.isEmpty()){
                //String path = "http://80.211.245.217:8081" + photos.get(0).getWebAppPath();
                String path =  Settings.server_url + photos.get(0).getWebAppPath();
                Picasso.get().load(path).into(viewHolder.photo1);
            }

        }else{
            viewHolder.photo1.setVisibility(View.GONE);
        }

        viewHolder.userName.setText(postView.getUsername());
        viewHolder.postdate.setText(postView.getPostdate());
        viewHolder.textContent.setText(postView.getDescription());
        viewHolder.postid.setText(postView.getId().toString());
        if(postView.getUserid().equals(Settings.user.getId())){
            viewHolder.removePost.setVisibility(View.VISIBLE);
            viewHolder.removePost.setOnClickListener(new RemovePostListener(postView.getId(), context));
        }

        if(postView.isLiked()){
            viewHolder.addStar.setImageDrawable(ContextCompat.getDrawable(context.getApplicationContext(),android.R.drawable.btn_star_big_on));
        }else{
            viewHolder.addStar.setImageDrawable(ContextCompat.getDrawable(context.getApplicationContext(),android.R.drawable.btn_star_big_off));
        }
        viewHolder.starNum.setText(postView.getLikeNumber());


        List<CommentView> comments = postView.getComments();
        viewHolder.commentNum.setText(Integer.toString(comments.size()));


        if(comments.isEmpty()){
            viewHolder.firstComment.setVisibility(View.GONE);
        }
        else {
            CommentView cv = comments.get(comments.size() - 1);

            Picasso.get().load(cv.getUserphoto()).into(viewHolder.cProfilPhoto);
            viewHolder.userCom.setText(cv.getUsername());
            viewHolder.comentData.setText(cv.getPostdate());
            viewHolder.commentContent.setText(cv.getContent());
            if(cv.getUserid().equals(Settings.user.getId())){
                viewHolder.removePost.setVisibility(View.VISIBLE);
                viewHolder.removePost.setOnClickListener(new CommentToRemoveListener(cv.getId(), context));
            }
        }

        viewHolder.addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText ed = (EditText) context.findViewById(R.id.newComment);
                String com = ed.getText().toString();
                if(com == null || com.isEmpty())
                    return;
                TextView sPostId = (TextView) context.findViewById(R.id.postid);
                Long id = Long.valueOf(sPostId.getText().toString());
                Comment comment = new Comment(id, Settings.user.getId(), com);
                AddComment addComment = new AddComment();
                addComment.execute(comment);
                context.finish();
                context.startActivity(context.getIntent());

            }
        });

        viewHolder.morePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView sPostId = (TextView) context.findViewById(R.id.postid);
                Long id = Long.valueOf(sPostId.getText().toString());
                Intent postintent = new Intent(context, PostActivity.class);
                postintent.putExtra("postid", id);
                context.startActivity(postintent);
            }
        });

        return r;
    }

}

    class ViewHolder{

        //Long postid;

        TextView postid;
        TextView userName;
        TextView postdate;
        ImageButton removePost;
        Button morePhoto;

        ImageView photo1;
        TextView textContent;

        TextView starNum;
        ImageButton addStar;
        TextView commentNum;

        ImageView cProfilPhoto;
        TextView userCom;
        TextView comentData;
        TextView commentContent;

        EditText newComment;
        ImageButton addComment;

        LinearLayout firstComment;

        public ViewHolder(View view) {
            this.postid = (TextView) view.findViewById(R.id.postid);
            this.firstComment = (LinearLayout) view.findViewById(R.id.firstComment);
            this.userName = (TextView) view.findViewById(R.id.userName);
            this.postdate = (TextView) view.findViewById(R.id.postdate);
            this.textContent = (TextView) view.findViewById(R.id.textContent);
            this.starNum = (TextView) view.findViewById(R.id.starNum);
            this.commentNum = (TextView) view.findViewById(R.id.commentNum);
            this.userCom = (TextView) view.findViewById(R.id.userCom);
            this.comentData = (TextView) view.findViewById(R.id.commentDate);
            this.commentContent = (TextView) view.findViewById(R.id.commentContent);
            this.commentContent = (TextView) view.findViewById(R.id.commentContent);

            this.morePhoto = (Button) view.findViewById(R.id.morePhoto);

            this.removePost = (ImageButton) view.findViewById(R.id.removePost3);
            this.addStar = (ImageButton) view.findViewById(R.id.addStar);
            this.addComment = (ImageButton) view.findViewById(R.id.addComment);

            this.photo1 = (ImageView) view.findViewById(R.id.photo1);
            this.cProfilPhoto = (ImageView) view.findViewById(R.id.cProfilPhoto);
            this.newComment = (EditText) view.findViewById(R.id.newComment);

        }
    }


