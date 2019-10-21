package com.geniuskid.haveitback.activities.reportView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.geniuskid.haveitback.R;
import com.geniuskid.haveitback.activities.itemDetails.ItemDetailsActivity;
import com.geniuskid.haveitback.adapters.ItemsListAdapter;
import com.geniuskid.haveitback.pojos.LostItems;
import com.geniuskid.haveitback.utils.BaseActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;

public class ItemsListActivity extends BaseActivity {

    @BindView(R.id.items_recycler)
    RecyclerView itemsRv;

    @BindView(R.id.noListItems)
    LinearLayout noItemsLayout;

    ImageView backIv;

    private ArrayList<LostItems> lostItemsArrayList = new ArrayList<>();

    private ProgressDialog progressDialog;

    public DatabaseReference postDbRef;
    public StorageReference storageReference;
    public FirebaseStorage firebaseStorage;

    private ItemsListAdapter itemsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_list);

        progressDialog = new ProgressDialog(this);
        backIv = findViewById(R.id.backIv);

        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initFireBase();
        loadItems();
    }

    private void initFireBase() {
        FirebaseApp.initializeApp(this);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        postDbRef = FirebaseDatabase.getInstance().getReference().child("LostItemsPost");
    }

    private void loadItems() {
        showProgress("Loading lost items...");
        itemsRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        itemsListAdapter = new ItemsListAdapter(this, lostItemsArrayList, new ItemsListAdapter.CircularCallBack() {
            @Override
            public void onItemClicked(int position, ImageView imageView) {
                LostItems lostItems = lostItemsArrayList.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("items", new Gson().toJson(lostItems));
                goTo(ItemsListActivity.this, ItemDetailsActivity.class, bundle,false);
            }
        });
        itemsRv.setAdapter(itemsListAdapter);

        postDbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    retriveDataFromDb(dataSnapshot);
                } else {
                    updateUi();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                updateUi();
                Toast.makeText(ItemsListActivity.this, "Error occurred while loading lost items!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUi() {
        cancelProgress();

        if (lostItemsArrayList.size() > 0) {
            itemsListAdapter.notifyDataSetChanged();
            itemsRv.setAdapter(itemsListAdapter);
        }

        itemsRv.setVisibility(lostItemsArrayList.size() == 0 ? View.INVISIBLE : View.VISIBLE);
        noItemsLayout.setVisibility(lostItemsArrayList.size() == 0 ? View.VISIBLE : View.INVISIBLE);
        animateWithData(itemsRv);
    }

    private void animateWithData(RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.fall_down_layout_anim);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    private void retriveDataFromDb(DataSnapshot dataSnapshot) {
        Map<String, LostItems> newsFeedItemsMap = (Map<String, LostItems>) dataSnapshot.getValue();

        lostItemsArrayList.clear();

        for (Map.Entry<String, LostItems> teamEntry : newsFeedItemsMap.entrySet()) {

            Map map = (Map) teamEntry.getValue();

            LostItems lostItems = new LostItems();

            lostItems.setId((String) map.get("id"));
            lostItems.setName((String) map.get("name"));
            lostItems.setImage((String) map.get("image"));
            lostItems.setDesc((String) map.get("desc"));
            lostItems.setPlace((String) map.get("place"));
            lostItems.setDate((String) map.get("date"));

            lostItemsArrayList.add(lostItems);
        }

        updateUi();
    }


    private void showProgress(String msg) {
        if (msg != null) {
            progressDialog.setMessage(msg);
            progressDialog.setCancelable(false);
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
    }

    private void cancelProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

}
