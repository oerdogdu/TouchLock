package com.atoi.touchlock;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.atoi.touchlock.Adapters.AdvertisementListAdapter;
import com.atoi.touchlock.Data.Remote.AdvertisementApiCall;
import com.atoi.touchlock.Data.Remote.AdvertisementService;
import com.atoi.touchlock.POJO.Advertisement;
import com.atoi.touchlock.Utils.SessionSharedPreferances;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HostFragment extends Fragment {

    private FloatingActionButton addFab;
    private ListView adList;
    private RelativeLayout emptyLayout;
    private ArrayList<Advertisement> advertisements;
    private SwipeRefreshLayout swipeRefreshLayout;
    private AdvertisementListAdapter advertisementListAdapter;
    ArrayList<Advertisement> advertisementList = new ArrayList<Advertisement>();
    private MainActivity mainActivity;

    public static HostFragment newInstance()
    {
        HostFragment hostFragment = new HostFragment();
        return hostFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.host_fragment, container, false);

        adList = view.findViewById(R.id.ad_list);
        adList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Advertisement advertisement = (Advertisement) parent.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), CertainAdvertisement.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("ad", advertisement);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        swipeRefreshLayout = view.findViewById(R.id.pullToRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAdvertisements();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        advertisements = new ArrayList<>();
        advertisementListAdapter = new AdvertisementListAdapter(getActivity(), advertisements);
        adList.setAdapter(advertisementListAdapter);

        getAdvertisements();

        addFab = view.findViewById(R.id.floatingActionButton);

        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HostActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity){
            mainActivity = (MainActivity) context;
        }
    }

    public synchronized void getAdvertisements() {
        SessionSharedPreferances sessionSharedPreferances = new SessionSharedPreferances(getContext());


        AdvertisementService advertisementService = AdvertisementApiCall.prepareCall();
        Call<List<Advertisement>> findMyAdsCall = advertisementService.getMyAds(sessionSharedPreferances.getEmail());

        findMyAdsCall.enqueue(new Callback<List<Advertisement>>() {
            @Override
            public void onResponse(Call<List<Advertisement>> call, Response<List<Advertisement>> response) {
                advertisementList.clear();
                advertisementList.addAll(response.body());
                refreshList();
                Log.d("adSearch", advertisementList.size()+"");
            }

            @Override
            public void onFailure(Call<List<Advertisement>> call, Throwable t) {

            }
        });
    }

    public void refreshList() {
        if(advertisementList != null) {
            if (advertisementList.size() > 0) {
                emptyLayout = getView().findViewById(R.id.no_ad);
                emptyLayout.setVisibility(View.GONE);
                adList.setVisibility(View.VISIBLE);
                advertisementListAdapter.refreshDataSet(advertisementList);
                adList.refreshDrawableState();
            }
            else {
                emptyLayout = getView().findViewById(R.id.no_ad);
                emptyLayout.setVisibility(View.VISIBLE);
                adList.setVisibility(View.GONE);
                advertisementListAdapter.refreshDataSet(advertisementList);
                adList.refreshDrawableState();
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser && advertisementListAdapter != null) {
            getAdvertisements();
            }
        }
    }

