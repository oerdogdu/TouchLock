package com.atoi.touchlock;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Toast;

import com.atoi.touchlock.Data.Local.AdvertisementRepository;
import com.atoi.touchlock.Data.Remote.AdvertisementApiCall;
import com.atoi.touchlock.Data.Remote.AdvertisementService;
import com.atoi.touchlock.Data.Remote.AuthApiCall;
import com.atoi.touchlock.Data.Remote.AuthService;
import com.atoi.touchlock.POJO.Advertisement;
import com.atoi.touchlock.Utils.DateConverter;
import com.atoi.touchlock.Utils.SessionSharedPreferances;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HostActivity extends FragmentActivity {

    private ViewPager mPager;
    private PagerAdapter pagerAdapter;
    private String adName;
    private String type;
    private String checkInDateText;
    private String checkOutDateText;
    private String city;
    private String address, description;
    private ArrayList<MultipartBody.Part> images = new ArrayList<>();
    private String country;
    private String currency;
    private int price, minDay, userId, nOfRoom, nOfBathroom, nOfBed;
    private boolean wifi, tv, basicNeeds, fireExt, petAllowed, airConditioner;
    private AdvertisementRepository advertisementRepository;
    private Date checkInDate, checkOutDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.host_activity);

        advertisementRepository = AdvertisementRepository.getInstance(getApplication());

        mPager = findViewById(R.id.pager);

        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(pagerAdapter);

    }

    public void firstFragmentData(String adName, int price, String type, Date checkInDate, Date checkOutDate, String currency, int minDay) {
        this.adName = adName;
        this.price = price;
        this.type = type;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.currency = currency;
        this.minDay = minDay;
        Log.d("checDate", DateConverter.fromDate(checkInDate));
    }

    public void secondFragmentData(String country, String city, String address, ArrayList<MultipartBody.Part> images) {
        this.country = country;
        this.city = city;
        this.address = address;
        this.images.clear();
        if(images.size() != 0) {
            this.images.addAll(images);
            uploadPhotos();
        }
    }

    private void uploadPhotos() {
        final SessionSharedPreferances session = new SessionSharedPreferances(getApplicationContext());
        final String emailAddress = session.getEmail();
        AuthService authService = AuthApiCall.prepareCall();
        Call<ResponseBody> getUserId = authService.getUserId(emailAddress);
        getUserId.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    String responseAuth = null;
                    try {
                        responseAuth = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    userId = Integer.valueOf(responseAuth);
                    session.setUserId(userId);
                    startUpload();

            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    private void startUpload() {
        AdvertisementService advertisementService = AdvertisementApiCall.prepareCall();
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), adName);
        RequestBody requestUserId = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(userId));
        Log.d("imagesSize", images.size() + "");
        for(int i=0;i<images.size();i++) {
            Call<ResponseBody> uploadCall = advertisementService.uploadPhoto(requestUserId, name, images.get(i));
            uploadCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.d("uploadResponse", response.code() + "");
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("uploadResponseFail", t.getLocalizedMessage());

                }
            });
        }
    }

    public void thirdFragmentData(boolean wifi, boolean tv, boolean airConditioner, boolean petAllowed,
                                  boolean fireExt, boolean basicNeeds) {
        this.wifi = wifi;
        this.tv = tv;
        this.airConditioner = airConditioner;
        this.petAllowed = petAllowed;
        this.fireExt = fireExt;
        this.basicNeeds = basicNeeds;
    }

    public void fourthFragmentData(int nOfRoom, int nOfBathroom, int nOfBed, String description) {
        this.nOfRoom = nOfRoom;
        this.nOfBathroom = nOfBathroom;
        this.nOfBed = nOfBed;
        this.description = description;
    }

    public void publishAdvertisement() {
        SessionSharedPreferances session = new SessionSharedPreferances(getApplicationContext());
        final String emailAddress = session.getEmail();

        AdvertisementService advertisementService = AdvertisementApiCall.prepareCall();

        Call<ResponseBody> addCall = advertisementService.publishAdvertisement(emailAddress, userId, adName, type, DateConverter.fromDate(checkInDate), DateConverter.fromDate(checkOutDate),
                minDay, nOfRoom, nOfBathroom, nOfBed, country, city, address, price, currency, description, wifi, tv, basicNeeds,
                fireExt, petAllowed, airConditioner);

        addCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String responseAuth = "";
                try {
                    responseAuth = response.body().string();
                    if (Integer.valueOf(responseAuth) == 0) {
                        Advertisement advertisement = new Advertisement(emailAddress, userId, adName, type, checkInDate, checkOutDate,
                                minDay, nOfRoom, nOfBathroom, nOfBed, country, city, address, price, currency, description, wifi, tv, basicNeeds,
                                fireExt, petAllowed, airConditioner);
                        advertisementRepository.insertAdvertisement(advertisement);
                        Toast.makeText(HostActivity.this, "Advertisement Added!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(HostActivity.this, "Failed!", Toast.LENGTH_SHORT).show();

                    }
                    } catch (
                        IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure (Call < ResponseBody > call, Throwable t){
                    Toast.makeText(HostActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
        });
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new HostFirstFragment();
                case 1:
                    return new HostSecondFragment();
                case 2:
                    return new HostThirdFragment();
                case 3:
                    return new HostFourthFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }
    }
}
