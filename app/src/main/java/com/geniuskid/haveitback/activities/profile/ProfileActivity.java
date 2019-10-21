package com.geniuskid.haveitback.activities.profile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.ImageView;

import com.geniuskid.haveitback.R;
import com.geniuskid.haveitback.utils.DataStorage;

public class ProfileActivity extends AppCompatActivity {

    private ImageView backIv;
    private AppCompatEditText nameEd, emailEd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        backIv = findViewById(R.id.profileBackIv);
        nameEd = findViewById(R.id.nameEd);
        emailEd = findViewById(R.id.emailEd);

        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        DataStorage dataStorage = new DataStorage(this);
        nameEd.setText(dataStorage.getString("username"));
        emailEd.setText(dataStorage.getString("email"));
    }
}
