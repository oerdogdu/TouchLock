package com.atoi.touchlock;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.atoi.touchlock.Adapters.AdvertisementListAdapter;
import com.atoi.touchlock.POJO.Advertisement;

import java.util.ArrayList;

public class SearchResult extends AppCompatActivity {

    private ArrayList<Advertisement> advertisements = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ArrayList<Advertisement> ads = (ArrayList<Advertisement>) getIntent().getSerializableExtra("list");

        advertisements.clear();
        advertisements.addAll(ads);

        ListView searchList = findViewById(R.id.search_list);
        AdvertisementListAdapter advertisementListAdapter = new AdvertisementListAdapter(this, advertisements);
        searchList.setAdapter(advertisementListAdapter);

        searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Advertisement advertisement = (Advertisement) parent.getItemAtPosition(position);
                Intent intent = new Intent(SearchResult.this, InspectAd.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("ad", advertisement);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
