package com.geniuskid.haveitback.activities.myReports;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.geniuskid.haveitback.R;
import com.geniuskid.haveitback.adapters.ItemsListAdapter;
import com.geniuskid.haveitback.pojos.LostItems;
import com.geniuskid.haveitback.utils.BaseActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;

public class MyReportsActivity extends BaseActivity {

    @BindView(R.id.myReportsRv)
    RecyclerView itemsRv;

    @BindView(R.id.no_reports)
    LinearLayout noItemsLayout;

    ImageView backIv;

    private ArrayList<LostItems> myItemsList = new ArrayList<>();

    private ProgressDialog progressDialog;

    public DatabaseReference postDbRef;
    public StorageReference storageReference;
    public FirebaseStorage firebaseStorage;

    private ItemsListAdapter itemsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reports);

        backIv = findViewById(R.id.backIv);
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        progressDialog = new ProgressDialog(this);

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
        itemsListAdapter = new ItemsListAdapter(this, myItemsList, true, new ItemsListAdapter.CircularCallBack() {
            @Override
            public void onItemClicked(int position, ImageView imageView) {

            }

            @Override
            public void onClaimClicked(int position) {
                LostItems lostItems = myItemsList.get(position);
                showDialogAndConfirm(lostItems);
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
                Toast.makeText(MyReportsActivity.this, "Error occurred while loading lost items!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDialogAndConfirm(final LostItems lostItems) {
        if(lostItems != null) {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            if(!TextUtils.isEmpty(lostItems.getIsClaimed())) {
                if (lostItems.getIsClaimed().equals("0")) {
                    alertDialog.setMessage("Making disclaimed will allow other users to see this item in reports listing. Are you sure, you want to make it as disclaimed?");
                } else {
                    alertDialog.setMessage("Making claimed will not allow other users be able to see it in reports listing. Are you sure, you want to make it as claimed?");
                }
            }
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, Html.fromHtml("<font color='#000000'>Yes</font>"), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(!TextUtils.isEmpty(lostItems.getIsClaimed())) {
                        if (lostItems.getIsClaimed().equals("1")) {
                            updateClaimed(lostItems, false);
                        } else {
                            updateClaimed(lostItems, true);
                        }
                    }
                }
            });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, Html.fromHtml("<font color='#000000'>No</font>"), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertDialog.setCancelable(false);
            if (!alertDialog.isShowing() && !isFinishing()) alertDialog.show();
        }
    }

    private void updateClaimed(LostItems lostItems, boolean isClaimed) {
        showProgress("Updating report informations");

        lostItems.setIsClaimed(isClaimed ? "0" : "1");
        lostItems.setPostedNum(dataStorage.getString("email"));
        lostItems.setPostedName(dataStorage.getString("username"));

        postDbRef.child(lostItems.getId()).setValue(lostItems).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(MyReportsActivity.this, "Updated successfully!", Toast.LENGTH_SHORT).show();
                loadItems();
                cancelProgress();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                cancelProgress();
                Toast.makeText(MyReportsActivity.this, "Some error occurred, try again later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUi() {
        cancelProgress();

        if (myItemsList.size() > 0) {
            itemsListAdapter.notifyDataSetChanged();
            itemsRv.setAdapter(itemsListAdapter);
        }

        itemsRv.setVisibility(myItemsList.size() == 0 ? View.INVISIBLE : View.VISIBLE);
        noItemsLayout.setVisibility(myItemsList.size() == 0 ? View.VISIBLE : View.INVISIBLE);
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

        myItemsList.clear();

        String mobileNum = dataStorage.getString("email");

        for (Map.Entry<String, LostItems> teamEntry : newsFeedItemsMap.entrySet()) {

            Map map = (Map) teamEntry.getValue();

            LostItems lostItems = new LostItems();

            String postedNim = (String) map.get("postedNum");

            if(!TextUtils.isEmpty(postedNim)) {
                if (postedNim != null && postedNim.equalsIgnoreCase(mobileNum)) {
                    lostItems.setId((String) map.get("id"));
                    lostItems.setName((String) map.get("name"));
                    lostItems.setImage((String) map.get("image"));
                    lostItems.setDesc((String) map.get("desc"));
                    lostItems.setPlace((String) map.get("place"));
                    lostItems.setDate((String) map.get("date"));
                    lostItems.setPostedName((String) map.get("postedName"));
                    lostItems.setIsClaimed((String) map.get("isClaimed"));
                    lostItems.setPostedNum((String) map.get("postedNum"));

                    myItemsList.add(lostItems);
                }
            }
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
