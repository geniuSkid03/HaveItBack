package com.geniuskid.haveitback.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.geniuskid.haveitback.R;
import com.geniuskid.haveitback.pojos.LostItems;

import java.util.ArrayList;

public class ItemsListAdapter extends RecyclerView.Adapter<ItemsListAdapter.CircularFeedView> {

    private ArrayList<LostItems> circularDataItemsArrayList;
    private Context context;
    private CircularCallBack circularCallBack;
    private boolean isMyList;

    public ItemsListAdapter(Context context, ArrayList<LostItems> circularDataItemsArrayList, boolean isMyList,
                            CircularCallBack circularCallBack) {
        this.context = context;
        this.isMyList = isMyList;
        this.circularDataItemsArrayList = circularDataItemsArrayList;
        this.circularCallBack = circularCallBack;
    }

    @NonNull
    @Override
    public CircularFeedView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CircularFeedView(LayoutInflater.from(context).inflate(R.layout.lost_items_layout, parent, false));
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
        private AppCompatButton claimedBtn;

        CircularFeedView(View view) {
            super(view);

            titleTv = itemView.findViewById(R.id.title);
            descTv = itemView.findViewById(R.id.desc);
            dateTv = itemView.findViewById(R.id.date);
            imageView = itemView.findViewById(R.id.image);
            itemsParent = itemView.findViewById(R.id.itemsParent);
            claimedBtn = itemView.findViewById(R.id.statusBtn);
        }

        void setCicularFeedView(final int position) {

            LostItems lostItems = circularDataItemsArrayList.get(position);

            if (lostItems != null) {
                if (!TextUtils.isEmpty(lostItems.getName()))
                    titleTv.setText(lostItems.getName());

                if (!TextUtils.isEmpty(lostItems.getDesc()))
                    descTv.setText(lostItems.getDesc());

                if (!TextUtils.isEmpty(lostItems.getDate()))
                    dateTv.setText(lostItems.getDate());

                if (!TextUtils.isEmpty(lostItems.getIsClaimed()))
                    claimedBtn.setText(lostItems.getIsClaimed().equals("0") ? "Make Claimed" : "Make Disclaimed");

                if (!TextUtils.isEmpty(lostItems.getImage()))
                    Glide.with(context).load(lostItems.getImage()).into(imageView);

                claimedBtn.setVisibility(isMyList ? View.VISIBLE : View.GONE);

                if(isMyList) {
                    claimedBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            circularCallBack.onClaimClicked(position);
                        }
                    });
                } else {
                    itemsParent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            circularCallBack.onItemClicked(position, imageView);
                        }
                    });
                }
            }
        }

        private void setView(LostItems circularDataItems) {

        }
    }

    public interface CircularCallBack {
        void onItemClicked(int position, ImageView imageView);

        void onClaimClicked(int position);
    }
}
