package com.myweddi.utils;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
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

import com.myweddi.R;
import com.myweddi.model.Photo;
import com.myweddi.model.post.Comment;
import com.myweddi.settings.Settings;
import com.myweddi.view.CommentView;
import com.myweddi.view.PostView;
import com.squareup.picasso.Picasso;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Text;

import java.util.List;

public class CustomListview extends ArrayAdapter<String> {


//    private List<String> titles;
//    private List<String> date;
//    private List<String> imgurl;

    private Activity context;
    private List<PostView> postlist;
//    public CustomListview(Activity context, List<String> titles, List<String> date, List<String> imgurl) {
//        super(context, R.layout.listview_layout, titles);
//        this.context = context;
//        this.titles = titles;
//        this.date = date;
//        this.imgurl = imgurl;
//    }

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

        List<Photo> photos = postlist.get(position).getPhotos();
        if(photos != null && !photos.isEmpty()){
            if(photos.get(0) != null && !photos.isEmpty()){
                //String path = "http://80.211.245.217:8081" + photos.get(0).getWebAppPath();
                String path =  "http://10.0.2.2:8081" + photos.get(0).getWebAppPath();
                Picasso.get().load(path).into(viewHolder.photo1);
            }

        }else{
            viewHolder.photo1.setVisibility(View.GONE);
        }
        //Picasso.get().load(postlist.get(position).getPhotos().get(0).getWebAppPath()).into(viewHolder.ivw);
        viewHolder.userName.setText(postlist.get(position).getUsername());
        viewHolder.postdate.setText(postlist.get(position).getPostdate());
        viewHolder.textContent.setText(postlist.get(position).getDescription());
        viewHolder.postid.setText(postlist.get(position).getId().toString());
        //viewHolder.postid = postlist.get(position).getId();

        List<CommentView> comments = postlist.get(position).getComments();
        viewHolder.commentNum.setText(Integer.toString(comments.size()));


        if(comments.isEmpty()){
            viewHolder.firstComment.setVisibility(View.GONE);
        }
        else {
            CommentView cv = comments.get(0);

            Picasso.get().load(cv.getUserphoto()).into(viewHolder.cProfilPhoto);
            viewHolder.userCom.setText(cv.getUsername());
            viewHolder.comentData.setText(cv.getPostdate());
            viewHolder.commentContent.setText(cv.getContent());
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
            this.comentData = (TextView) view.findViewById(R.id.comentData);
            this.commentContent = (TextView) view.findViewById(R.id.commentContent);
            this.commentContent = (TextView) view.findViewById(R.id.commentContent);

            this.morePhoto = (Button) view.findViewById(R.id.morePhoto);

            this.removePost = (ImageButton) view.findViewById(R.id.removePost);
            this.addStar = (ImageButton) view.findViewById(R.id.addStar);
            this.addComment = (ImageButton) view.findViewById(R.id.addComment);

            this.photo1 = (ImageView) view.findViewById(R.id.photo1);
            this.cProfilPhoto = (ImageView) view.findViewById(R.id.cProfilPhoto);
            this.newComment = (EditText) view.findViewById(R.id.newComment);

        }
    }

    class AddComment extends AsyncTask<Comment, Void, Void> {

        @Override
        protected Void doInBackground(Comment... comments) {
            Comment comment = comments[0];
            RequestUtils requestUtils = new RequestUtils();
            RestTemplate restTemplate = requestUtils.getRestTemplate();
            HttpHeaders requestHeaders = requestUtils.getRequestHeaders();

            String path = Settings.server_url + "api/post/addcomment";
            restTemplate.postForEntity(path, new HttpEntity<Comment>(comment, requestHeaders), Long.class);
            return null;
        }
}
