package com.geniuskid.haveitback.activities.main;

import android.os.Bundle;

import com.geniuskid.haveitback.R;
import com.geniuskid.haveitback.activities.reportView.ItemsListActivity;
import com.geniuskid.haveitback.utils.BaseActivity;

import butterknife.OnClick;

public class MainActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @OnClick(R.id.report_item)
    public void onReportItem() {

    }

    @OnClick(R.id.find_item)
    public void onFindItem() {
        goTo(this, ItemsListActivity.class, false);
    }
}
