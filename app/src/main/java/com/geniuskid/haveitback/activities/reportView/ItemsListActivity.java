package com.geniuskid.haveitback.activities.reportView;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.geniuskid.haveitback.R;
import com.geniuskid.haveitback.adapters.ItemsListAdapter;
import com.geniuskid.haveitback.pojos.LostItems;
import com.geniuskid.haveitback.utils.BaseActivity;

import java.util.ArrayList;

import butterknife.BindView;

public class ItemsListActivity extends BaseActivity {

    @BindView(R.id.items_recycler)
    RecyclerView itemsRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_list);

        loadItems();
    }

    private void loadItems() {
        itemsRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        itemsRv.setAdapter(new ItemsListAdapter(this, getItms(), null));
    }

    private ArrayList<LostItems> getItms() {
        ArrayList<LostItems> items = new ArrayList<>();

        items.add(new LostItems("1", "White Pen", "", "21-11-18"));
        items.add(new LostItems("1", "White Pen", "", "21-11-18"));
        items.add(new LostItems("1", "White Pen", "", "21-11-18"));
        items.add(new LostItems("1", "White Pen", "", "21-11-18"));
        items.add(new LostItems("1", "White Pen", "", "21-11-18"));

        return items;
    }
}
