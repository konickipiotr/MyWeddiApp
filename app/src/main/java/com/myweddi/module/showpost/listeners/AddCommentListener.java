package com.myweddi.module.showpost.listeners;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.myweddi.R;
import com.myweddi.module.showpost.AddComment;
import com.myweddi.module.showpost.model.Comment;
import com.myweddi.settings.Settings;

public class AddCommentListener implements View.OnClickListener {

    private Long postid;
    private Activity context;
    private EditText text;

    public AddCommentListener(Long postid, Activity context, EditText text) {
        this.postid = postid;
        this.context = context;
        this.text = text;
    }

    @Override
    public void onClick(View v) {
        String com = text.getText().toString();
        if(com == null || com.isEmpty())
            return;
        Comment comment = new Comment(this.postid, Settings.user.getId(), com);
        AddComment addComment = new AddComment();
        addComment.execute(comment);
        context.finish();
        context.startActivity(context.getIntent());
    }
}
