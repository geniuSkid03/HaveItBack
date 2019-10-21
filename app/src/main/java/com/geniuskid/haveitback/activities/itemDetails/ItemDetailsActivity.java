package com.geniuskid.haveitback.activities.itemDetails;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.geniuskid.haveitback.R;
import com.geniuskid.haveitback.pojos.LostItems;
import com.google.gson.Gson;

public class ItemDetailsActivity extends AppCompatActivity {

    private LostItems lostItems;

    AppCompatButton claimBtn;
    TextView descTv, datetTv, nameTv;
    ImageView imageView, backIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        claimBtn = findViewById(R.id.claimBtn);
        descTv = findViewById(R.id.descTv);
        datetTv = findViewById(R.id.datetTv);
        nameTv = findViewById(R.id.nameTv);
        imageView = findViewById(R.id.imageView);
        backIv = findViewById(R.id.backIv);

        claimBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ItemDetailsActivity.this, "Available in future!", Toast.LENGTH_SHORT).show();
            }
        });
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String items = getIntent().getStringExtra("items");
        if(!TextUtils.isEmpty(items)) {
            Gson gson = new Gson();
            lostItems = gson.fromJson(items, LostItems.class);

            updateUi(lostItems);
        } else {
            Toast.makeText(this, "Some error occurred!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void updateUi(LostItems lostItems) {
        descTv.setText(lostItems.getDesc());
        Glide.with(this).load(lostItems.getImage()).into(imageView);
        nameTv.setText(lostItems.getName());
        datetTv.setText(lostItems.getDate());
    }
}
