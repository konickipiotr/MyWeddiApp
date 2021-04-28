package com.myweddi.module.showpost;

import android.app.Activity;
import android.content.Intent;
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
import com.myweddi.model.Photo;
import com.myweddi.module.showpost.listeners.AddCommentListener;
import com.myweddi.module.showpost.listeners.ShowFullPostListener;
import com.myweddi.module.showpost.model.Comment;
import com.myweddi.module.showpost.listeners.CommentToRemoveListener;
import com.myweddi.module.showpost.listeners.RemovePostListener;
import com.myweddi.module.showpost.listeners.StarButtonListener;
import com.myweddi.roles.guest.PostActivity;
import com.myweddi.settings.Settings;
import com.myweddi.module.showpost.view.CommentView;
import com.myweddi.module.showpost.view.PostView;
import com.squareup.picasso.Picasso;


import java.util.List;

public class PostListAdapter extends ArrayAdapter<String> {
    private Activity context;
    private List<PostView> postlist;

    public PostListAdapter(Activity context, List<PostView> postlist, List<String> titles) {
        super(context, R.layout.mini_post, titles);
        this.postlist = postlist;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder = null;

        if(view == null){
            LayoutInflater inflater = context.getLayoutInflater();
            view = inflater.inflate(R.layout.mini_post, null, true);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }

        PostView postView = postlist.get(position);
        PostHelper postHelper = new PostHelper(viewHolder, postView, this.context);

        postHelper.setMainPhoto();
        viewHolder.userName.setText(postView.getUsername());
        viewHolder.postdate.setText(postView.getPostdatetime());
        viewHolder.textContent.setText(postView.getDescription());

        postHelper.setRemovePost();
        postHelper.setStars();

        List<CommentView> comments = postView.getComments();
        viewHolder.commentNum.setText(Integer.toString(comments.size()));
        postHelper.setComments(comments);

        viewHolder.addComment.setOnClickListener(new AddCommentListener(postView.getId(), this.context));
        viewHolder.morePhoto.setOnClickListener(new ShowFullPostListener(postView.getId(), this.context));

        return view;
    }

    class ViewHolder{

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

        ImageView cProfilPhoto2;
        TextView userCom2;
        TextView comentData2;
        TextView commentContent2;

        EditText newComment;
        ImageButton addComment;

        LinearLayout lastComment;
        LinearLayout secondLastComment;

        ImageButton removeComment1;
        ImageButton removeComment2;

        public ViewHolder(View view) {
            this.lastComment = (LinearLayout) view.findViewById(R.id.LastComment);
            this.secondLastComment = (LinearLayout) view.findViewById(R.id.SecondLastComment);
            this.userName = (TextView) view.findViewById(R.id.userName);
            this.postdate = (TextView) view.findViewById(R.id.postdate);
            this.textContent = (TextView) view.findViewById(R.id.textContent);
            this.starNum = (TextView) view.findViewById(R.id.starNum);
            this.commentNum = (TextView) view.findViewById(R.id.commentNum);

            this.userCom = (TextView) view.findViewById(R.id.userCom);
            this.comentData = (TextView) view.findViewById(R.id.commentDate);
            this.commentContent = (TextView) view.findViewById(R.id.commentContent);
            this.commentContent = (TextView) view.findViewById(R.id.commentContent);

            this.userCom2 = (TextView) view.findViewById(R.id.user2Com);
            this.comentData2 = (TextView) view.findViewById(R.id.comment2Date);
            this.commentContent2 = (TextView) view.findViewById(R.id.comment2Content);
            this.commentContent2 = (TextView) view.findViewById(R.id.comment2Content);

            this.morePhoto = (Button) view.findViewById(R.id.morePhoto);

            this.removePost = (ImageButton) view.findViewById(R.id.removePost3);
            this.addStar = (ImageButton) view.findViewById(R.id.addStar);
            this.addComment = (ImageButton) view.findViewById(R.id.addComment);

            this.photo1 = (ImageView) view.findViewById(R.id.photo1);
            this.cProfilPhoto = (ImageView) view.findViewById(R.id.cProfilPhoto);
            this.cProfilPhoto2 = (ImageView) view.findViewById(R.id.c2ProfilPhoto);

            this.newComment = (EditText) view.findViewById(R.id.newComment);

            this.removeComment1 = (ImageButton) view.findViewById(R.id.removeComment);
            this.removeComment2 = (ImageButton) view.findViewById(R.id.removeComment2);
        }
    }
}




