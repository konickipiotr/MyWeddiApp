package com.myweddi.roles.guest;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.myweddi.R;
import com.myweddi.model.Photo;
import com.myweddi.settings.Settings;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostPhotoListView extends ArrayAdapter<String> {

    private Activity context;
    private List<Photo> photoList;

    public PostPhotoListView(Activity context, List<Photo> photoList, List<String> dummy) {
        super(context, R.layout.photo_layout, dummy);
        this.photoList = photoList;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        PhotoViewHolder photoviewHolder = null;

        if(view == null){
            LayoutInflater inflater = context.getLayoutInflater();
            view = inflater.inflate(R.layout.photo_layout, null, true);
            photoviewHolder = new PhotoViewHolder(view);
            view.setTag(photoviewHolder);
        }
        else {
            photoviewHolder = (PhotoViewHolder) view.getTag();
        }

        if(photoList == null || photoList.isEmpty())
            return view;

        Photo photo = photoList.get(position);
        String path =  Settings.server_url + photo.getWebAppPath();
        Picasso.get().load(path).into(photoviewHolder.postPhoto);
        return view;
    }

    private class PhotoViewHolder {

        public ImageView postPhoto;

        public PhotoViewHolder(View view) {
            postPhoto = view.findViewById(R.id.postPhoto);
        }
    }
}
