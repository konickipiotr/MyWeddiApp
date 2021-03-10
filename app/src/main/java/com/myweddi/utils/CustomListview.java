package com.myweddi.utils;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.myweddi.R;
import com.myweddi.model.Photo;
import com.myweddi.roles.guest.GuestHome;
import com.myweddi.settings.Settings;
import com.myweddi.view.PostView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
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
        super(context, R.layout.listview_layout, titles);
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
            r = inflater.inflate(R.layout.listview_layout, null, true);
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
                String path = "http://10.0.2.2:8081" + photos.get(0).getWebAppPath();
                Picasso.get().load(path).into(viewHolder.ivw);
            }

        }
        //Picasso.get().load(postlist.get(position).getPhotos().get(0).getWebAppPath()).into(viewHolder.ivw);
        viewHolder.tvw1.setText(postlist.get(position).getUsername());
        viewHolder.tvw2.setText(postlist.get(position).getPostdate());
        viewHolder.tvw3.setText(postlist.get(position).getDescription());
        return r;
    }

    class ViewHolder {
        TextView tvw1;
        TextView tvw2;
        TextView tvw3;
        ImageView ivw;

        public ViewHolder(View view) {
            this.tvw1 = (TextView) view.findViewById(R.id.posttitle);
            this.tvw2 = (TextView) view.findViewById(R.id.date);
            this.tvw3 = (TextView) view.findViewById(R.id.description);
            this.ivw = (ImageView) view.findViewById(R.id.imageView);
        }

    }
}
