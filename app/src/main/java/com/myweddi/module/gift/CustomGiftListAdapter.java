package com.myweddi.module.gift;

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
import com.myweddi.module.gift.listeners.RemoveGiftListener;
import com.myweddi.module.gift.model.Gift;
import com.myweddi.module.showpost.PostCommentAdapter;
import com.myweddi.module.showpost.listeners.CommentToRemoveListener;
import com.myweddi.module.showpost.view.CommentView;
import com.myweddi.settings.Settings;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomGiftListAdapter extends ArrayAdapter<Gift> {

    private List<Gift> gifts;
    private Activity context;

    public CustomGiftListAdapter(Activity context, List<Gift> gifts) {
        super(context, R.layout.comment_layout, gifts);
        this.gifts = gifts;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder = null;

        if(view == null){
            LayoutInflater inflater = context.getLayoutInflater();
            view = inflater.inflate(R.layout.host_gift_layout, null, true);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Gift gift = gifts.get(position);
        viewHolder.host_gift_lp.setText(String.valueOf(position));
        viewHolder.host_gift_customgift.setText(gift.getName());

        String username = gift.getUsername();

        viewHolder.host_gift_guestname.setText(username != null ? username : "FREE");
        viewHolder.host_customgift_remove.setOnClickListener(new RemoveGiftListener(gift.getId(), this.context));
        return view;
    }

    private class ViewHolder {

        TextView host_gift_lp;
        TextView host_gift_customgift;
        TextView host_gift_guestname;
        ImageButton host_customgift_remove;

        public ViewHolder(View view) {
            host_gift_lp = view.findViewById(R.id.host_gift_lp);
            host_gift_customgift = view.findViewById(R.id.host_gift_customgift);
            host_gift_guestname = view.findViewById(R.id.host_gift_guestname);
            host_customgift_remove = (ImageButton) view.findViewById(R.id.host_customgift_remove);
        }
    }
}
