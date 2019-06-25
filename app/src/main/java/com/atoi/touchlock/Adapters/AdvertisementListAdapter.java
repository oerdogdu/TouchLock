package com.atoi.touchlock.Adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.atoi.touchlock.POJO.Advertisement;
import com.atoi.touchlock.R;

import java.util.ArrayList;

public class AdvertisementListAdapter extends BaseAdapter {

    private Activity activity;
    private static LayoutInflater inflater = null;
    private ArrayList<Advertisement> advertisements = new ArrayList<>();


    public AdvertisementListAdapter(Activity activity, ArrayList<Advertisement> advertisements) {
        this.activity = activity;
        this.advertisements = advertisements;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return advertisements.size();
    }

    public void refreshDataSet(ArrayList<Advertisement> advertisementList) {
        advertisements.clear();
        advertisements.addAll(advertisementList);
        this.notifyDataSetChanged();
        Log.w("refresh_list", "called");
    }

    @Override
    public Advertisement getItem(int position) {
        return advertisements.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if(convertView == null) {
            vi = inflater.inflate(R.layout.row_advertisement, null);
        }

        Advertisement ad = advertisements.get(position);

        TextView adTitle = vi.findViewById(R.id.ad_title);
        TextView adCity = vi.findViewById(R.id.ad_city);
        TextView adPrice = vi.findViewById(R.id.ad_price);

        adTitle.setText(ad.getAdName());
        adCity.setText(ad.getCity());
        adPrice.setText(ad.getPrice() + " TL");

        return vi;
    }
}
