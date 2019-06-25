package com.atoi.touchlock;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.atoi.touchlock.Data.Remote.AdvertisementApiCall;
import com.atoi.touchlock.Data.Remote.AdvertisementService;
import com.atoi.touchlock.POJO.Advertisement;
import com.atoi.touchlock.POJO.Photo;
import com.atoi.touchlock.Utils.DateConverter;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InspectAd extends AppCompatActivity {

    private TextView cityName, adPrice, adType, checkInDate, checkOutDate, utilities,
            address, description, minDay, numRoom, numBath, numBed ;
    private SpannableStringBuilder spannableString;
    private String utilitiesString = "";
    private FloatingActionButton fabDelete;
    private Advertisement advertisement;
    private HostFragment hostFragment;
    private int adId;
    private ImageView housePhoto;
    private CarouselView carouselView;
    private ArrayList<Bitmap> bitmapList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inspect_ad);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        cityName = findViewById(R.id.cityName);
        adPrice = findViewById(R.id.price);
        adType = findViewById(R.id.adType);
        checkInDate = findViewById(R.id.checkInDate);
        checkOutDate = findViewById(R.id.checkOutDate);
        address = findViewById(R.id.addressText);
        description = findViewById(R.id.descText);
        minDay = findViewById(R.id.minDayText);
        numRoom = findViewById(R.id.numRoomText);
        numBath = findViewById(R.id.numBathroomText);
        numBed = findViewById(R.id.numBedText);
        utilities = findViewById(R.id.adUtilities);
        carouselView = findViewById(R.id.carouselView);

        Bundle b = getIntent().getExtras();

        if (b != null) {
            advertisement = (Advertisement) b.getSerializable("ad");
            adId = advertisement.getId();
            cityName.setText(advertisement.getCity());
            adPrice.setText(advertisement.getPrice() + " TL");
            adType.setText(advertisement.getType());
            checkInDate.setText(DateConverter.fromDate(advertisement.getCheckInDate()) + " - ");
            checkOutDate.setText(DateConverter.fromDate(advertisement.getCheckOutDate()));
            address.setText(advertisement.getAddress());
            description.setText(advertisement.getDescription());
            minDay.setText(advertisement.getMinDay());
            numRoom.setText(advertisement.getNumberOfRoom());
            numBath.setText(advertisement.getNumberOfBathroom());
            numBed.setText(advertisement.getNumberOfBed());

            if (advertisement.isWifi()) {
                utilitiesString += "\u25CF  Wi-Fi\n";
            }
            if (advertisement.isPetAllowed()) {
                utilitiesString += "\u25CF  Pet Allowed\n";
            }
            if (advertisement.isAirConditioner()) {
                utilitiesString += "\u25CF  Air Conditioner\n";
            }
            if (advertisement.isBasicNeeds()) {
                utilitiesString += "\u25CF  Basic Needs\n";
            }
            if (advertisement.isFireExt()) {
                utilitiesString += "\u25CF  Fire Extinguisher\n";
            }
            if (advertisement.isTv()) {
                utilitiesString += "\u25CF  Tv\n";
            }

            final AdvertisementService advertisementService = AdvertisementApiCall.prepareCall();

            Call<List<Photo>> photos = advertisementService.getPhotosWithName(advertisement.getAdName());

            photos.enqueue(new Callback<List<Photo>>() {
                @Override
                public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                    List<Photo> photoList = response.body();
                    Log.d("photoSize", photoList.size() + "");
                    for (int i = 0; i < photoList.size(); i++) {
                        Photo photoUpload = photoList.get(i);
                        String rawString = photoUpload.getPhoto();
                        byte[] decodedString = Base64.decode(rawString, Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        bitmapList.add(bitmap);
                    }
                    carouselView.setImageListener(imageListener);
                    carouselView.setPageCount(photoList.size());
                }

                @Override
                public void onFailure(Call<List<Photo>> call, Throwable t) {
                    Log.d("photoSize", t.getLocalizedMessage());

                }
            });
        }
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageBitmap(bitmapList.get(position));
        }
    };

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
