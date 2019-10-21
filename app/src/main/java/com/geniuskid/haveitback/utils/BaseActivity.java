package com.geniuskid.haveitback.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;

import com.geniuskid.haveitback.R;

import butterknife.ButterKnife;

public class BaseActivity extends AppCompatActivity  {

    protected DataStorage dataStorage;
    protected Typeface poppinsTypeFace;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        poppinsTypeFace = ResourcesCompat.getFont(this, R.font.poppinsregular);
        dataStorage = new DataStorage(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }


    protected void goTo(Context from, Class to, boolean close, String key, String data) {
        if (!this.getClass().getSimpleName().equals(to.getClass().getSimpleName())) {
            if (key != null && data != null) {
                startActivity(new Intent(from, to).putExtra(key, data));
            }
            if (close) {
                finish();
            }
        }

    }

    //for starting activity with two intent values
    protected void goTo(Context from, Class to, boolean close, String key, String data, String key1, String data1) {
        if (!this.getClass().getSimpleName().equals(to.getClass().getSimpleName())) {
            if (key != null && data != null && key1 != null && data1 != null) {
                startActivity(new Intent(from, to).putExtra(key, data).putExtra(key1, data1));
            }
            if (close) {
                finish();
            }
        }

    }

    //for starting activity without intent values
    protected void goTo(Context from, Class to, boolean close) {
        if (!this.getClass().getSimpleName().equals(to.getClass().getSimpleName())) {
            startActivity(new Intent(from, to));
            if (close) {
                finish();
            }
        }
    }

    //for starting activity without intent values
    protected void goTo(Context from, Class to, Bundle bundle,boolean close) {
        if (!this.getClass().getSimpleName().equals(to.getClass().getSimpleName())) {
            startActivity(new Intent(from, to).putExtras(bundle));
            if (close) {
                finish();
            }
        }
    }
}
