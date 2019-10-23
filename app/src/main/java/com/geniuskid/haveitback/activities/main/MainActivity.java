package com.geniuskid.haveitback.activities.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.widget.TextView;

import com.geniuskid.haveitback.R;
import com.geniuskid.haveitback.activities.myReports.MyReportsActivity;
import com.geniuskid.haveitback.activities.profile.ProfileActivity;
import com.geniuskid.haveitback.activities.reportNew.CreateNewReportActivity;
import com.geniuskid.haveitback.activities.reportView.ItemsListActivity;
import com.geniuskid.haveitback.utils.BaseActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {


    @BindView(R.id.home_nav)
    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        initViewsAndClicks();
    }


    @OnClick(R.id.report_item)
    public void onReportItem() {
        goTo(this, CreateNewReportActivity.class, false);
    }

    @OnClick(R.id.find_item)
    public void onFindItem() {
        goTo(this, ItemsListActivity.class, false);
    }

    private void initViewsAndClicks() {
        TextView nameTv = findViewById(R.id.nameTv);
        nameTv.setText(String.format("Hi, %s", dataStorage.getString("username")));
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_home:
                        break;
                    case R.id.my_reports:
                        goTo(MainActivity.this, MyReportsActivity.class, false);
                        break;
                    case R.id.menu_profile:
                        goTo(MainActivity.this, ProfileActivity.class, false);
                        break;
                }
                return false;
            }
        });
    }
}
