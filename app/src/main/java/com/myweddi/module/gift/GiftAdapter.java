package com.myweddi.module.gift;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.myweddi.R;
import com.myweddi.module.gift.listeners.BookGiftListener;
import com.myweddi.module.gift.model.Gift;
import com.myweddi.module.gift.model.GiftWrapper;
import com.myweddi.settings.Settings;

import java.util.List;

public class GiftAdapter extends ArrayAdapter<String> {

    private Activity context;
    private GiftWrapper giftWrapper;

    public GiftAdapter(Activity context, GiftWrapper giftWrapper, List<String> titles) {
        super(context, R.layout.gift_list_view, titles);
        this.giftWrapper = giftWrapper;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r = convertView;
        GiftAdapter.ViewHolder viewHolder = null;

        if(r == null){
            LayoutInflater inflater = context.getLayoutInflater();
            r = inflater.inflate(R.layout.gift_list_view, null, true);
            viewHolder = new GiftAdapter.ViewHolder(r);
            r.setTag(viewHolder);
        }
        else {
            viewHolder = (GiftAdapter.ViewHolder) r.getTag();
        }

        Gift gift = giftWrapper.getGifts().get(position);
        viewHolder.giftNr.setText(Integer.toString(position + 1));
        viewHolder.giftName.setText(gift.getName());

        viewHolder.btnBookGift.setVisibility(View.GONE);
        viewHolder.btnUnbookGift.setVisibility(View.GONE);
        viewHolder.giftUsername.setVisibility(View.GONE);

        viewHolder.btnBookGift.setOnClickListener(new BookGiftListener(gift.getId(), this.context, GiftAction.BOOK));
        viewHolder.btnUnbookGift.setOnClickListener(new BookGiftListener(gift.getId(), this.context, GiftAction.UNBOOK));


        if(gift.getUsername() == null){
            viewHolder.btnBookGift.setVisibility(View.VISIBLE);
        }else {
            viewHolder.giftUsername.setText(gift.getUsername());
            viewHolder.btnUnbookGift.setVisibility(View.INVISIBLE);
        }

        if(gift.getUserid() != null && gift.getUserid().equals(Settings.user.getId())){
            viewHolder.btnUnbookGift.setVisibility(View.VISIBLE);
        }

        return r;
    }

    class ViewHolder{

        TextView giftNr;
        TextView giftName;
        TextView giftUsername;
        ImageButton btnBookGift;
        ImageButton btnUnbookGift;


        public ViewHolder(View view) {
            this.giftNr = (TextView) view.findViewById(R.id.gift_nr);
            this.giftName = (TextView) view.findViewById(R.id.gift_name);
            this.giftUsername = (TextView) view.findViewById(R.id.gift_username);
            this.btnBookGift = (ImageButton) view.findViewById(R.id.btnBookGift);
            this.btnUnbookGift = (ImageButton) view.findViewById(R.id.btnunbookgift);


        }
    }
}
