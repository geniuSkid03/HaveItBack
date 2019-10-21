package com.geniuskid.haveitback.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.geniuskid.haveitback.R;
import com.geniuskid.haveitback.pojos.LostItems;

import java.util.ArrayList;

public class ItemsListAdapter extends RecyclerView.Adapter<ItemsListAdapter.CircularFeedView> {

    private ArrayList<LostItems> circularDataItemsArrayList;
    private Context context;
    private CircularCallBack circularCallBack;

    public ItemsListAdapter(Context context, ArrayList<LostItems> circularDataItemsArrayList,
                            CircularCallBack circularCallBack) {
        this.context = context;
        this.circularDataItemsArrayList = circularDataItemsArrayList;
        this.circularCallBack = circularCallBack;
    }

    @NonNull
    @Override
    public CircularFeedView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CircularFeedView(LayoutInflater.from(context).inflate(R.layout.lost_items_layout,parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CircularFeedView holder, int position) {
        holder.setCicularFeedView(position);
    }

    @Override
    public int getItemCount() {
        return circularDataItemsArrayList.size();
    }

    class CircularFeedView extends RecyclerView.ViewHolder {

        private TextView titleTv, descTv, dateTv;
        private ImageView imageView;
        private CardView itemsParent;

        CircularFeedView(View view) {
            super(view);

            titleTv = itemView.findViewById(R.id.title);
            descTv = itemView.findViewById(R.id.desc);
            dateTv = itemView.findViewById(R.id.date);
            imageView = itemView.findViewById(R.id.image);
            itemsParent = itemView.findViewById(R.id.itemsParent);
        }

        void setCicularFeedView(final int position) {
            setView(circularDataItemsArrayList.get(position));

            itemsParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    circularCallBack.onItemClicked(position, imageView);
                }
            });
        }

        private void setView(LostItems circularDataItems) {
            titleTv.setText(circularDataItems.getName());
            descTv.setText(circularDataItems.getDate());
            dateTv.setText(circularDataItems.getDate());

            Glide.with(context).load(circularDataItems.getImage()).into(imageView);
        }
    }

    public interface CircularCallBack {
        void onItemClicked(int position, ImageView imageView);
    }
}
