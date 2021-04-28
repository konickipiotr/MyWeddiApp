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

    public AddCommentListener(Long postid, Activity context) {
        this.postid = postid;
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        EditText ed = (EditText) context.findViewById(R.id.newComment);
        String com = ed.getText().toString();
        if(com == null || com.isEmpty())
            return;
        Comment comment = new Comment(this.postid, Settings.user.getId(), com);
        AddComment addComment = new AddComment();
        addComment.execute(comment);
        context.finish();
        context.startActivity(context.getIntent());
    }
}
