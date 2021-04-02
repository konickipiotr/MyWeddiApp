package com.myweddi.utils;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.myweddi.R;
import com.myweddi.settings.Settings;
import com.myweddi.view.CommentView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostCommentListView extends ArrayAdapter<String> {

    private Activity context;
    private List<CommentView> commentViewList;

    public PostCommentListView(Activity context, List<CommentView> commentViewList, List<String> dummy) {
        super(context, R.layout.comment_layout, dummy);
        this.commentViewList = commentViewList;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        CommentViewHolder commentViewHolder = null;

        if(view == null){
            LayoutInflater inflater = context.getLayoutInflater();
            view = inflater.inflate(R.layout.comment_layout, null, true);
            commentViewHolder = new CommentViewHolder(view);
            view.setTag(commentViewHolder);
        }
        else {
            commentViewHolder = (CommentViewHolder) view.getTag();
        }

        if(commentViewList == null || commentViewList.isEmpty())
            return view;

        CommentView commentView = commentViewList.get(position);

        if(commentView.getUserphoto() != null) {
            String path = Settings.server_url + commentView.getUserphoto();
            Picasso.get().load(path).into(commentViewHolder.cProfilPhoto);
        }

        commentViewHolder.userCom.setText(commentView.getUsername());
        commentViewHolder.commentDate.setText(commentView.getPostdate());
        commentViewHolder.commentContent.setText(commentView.getContent());
        if(commentView.getUserid().equals(Settings.user.getId())){
            commentViewHolder.removePost.setVisibility(View.VISIBLE);
            commentViewHolder.removePost.setOnClickListener(new CommentToRemoveListener(commentView.getId(), context));
        }
        return view;
    }

    private class CommentViewHolder {

        public ImageView cProfilPhoto;
        public TextView userCom, commentDate, commentContent, userid;
        public ImageButton removePost;

        public CommentViewHolder(View view) {
            cProfilPhoto = (ImageView) view.findViewById(R.id.cProfilPhoto);
            userCom = (TextView) view.findViewById(R.id.userCom);
            commentDate = (TextView) view.findViewById(R.id.commentDate);
            commentContent = (TextView) view.findViewById(R.id.commentContent);
            removePost = (ImageButton) view.findViewById(R.id.removePost2);
        }
    }
}
