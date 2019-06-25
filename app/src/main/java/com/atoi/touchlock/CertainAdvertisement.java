package com.atoi.touchlock;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BulletSpan;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CertainAdvertisement extends AppCompatActivity {

    private TextView cityName, adPrice, adType, checkInDate, checkOutDate, utilities;
    private SpannableStringBuilder spannableString;
    private String utilitiesString = "";
    private FloatingActionButton fabDelete;
    private Advertisement advertisement;
    private HostFragment hostFragment;
    private int adId;
    private ImageView housePhoto;
    private CarouselView carouselView;
    private ArrayList<Bitmap> bitmapList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.certain_ads);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        cityName = findViewById(R.id.cityName);
        adPrice = findViewById(R.id.price);
        adType = findViewById(R.id.adType);
        checkInDate = findViewById(R.id.checkInDate);
        checkOutDate = findViewById(R.id.checkOutDate);
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

            /*spannableString = new SpannableStringBuilder(utilitiesString);
            String[] tempUtil = utilitiesString.split("\n");

            for (int i = 0; i< tempUtil.length; i++) {
                showBullet(tempUtil[i]);
            }*/


            utilities.setText(utilitiesString);

            final AdvertisementService advertisementService = AdvertisementApiCall.prepareCall();

            Call<List<Photo>> photos = advertisementService.getPhotosWithName(advertisement.getAdName());

            photos.enqueue(new Callback<List<Photo>>() {
                @Override
                public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                    List<Photo> photoList = response.body();
                    Log.d("photoSize", photoList.size()+"");
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

            hostFragment = HostFragment.newInstance();

            fabDelete = findViewById(R.id.fabDeleteAd);
            fabDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog builder =
                            new AlertDialog.Builder(CertainAdvertisement.this)
                                    .setTitle(R.string.confirm_title)
                                    .setMessage(R.string.confirm_delete)
                                    .setPositiveButton(R.string.button_confirm,
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(
                                                        DialogInterface dialog, int button) {
                                                    Call<ResponseBody> deleteCall = advertisementService.deleteAd(adId);
                                                    Log.d("adId", adId + "");
                                                    deleteCall.enqueue(new Callback<ResponseBody>() {
                                                        @Override
                                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                            hostFragment.getAdvertisements();
                                                            finish();
                                                        }

                                                        @Override
                                                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                                                        }
                                                    });
                                                }
                                            }
                                    ).setNegativeButton(R.string.button_cancel, null)
                                    .create();
                    builder.show();
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

    protected void showBullet(String textToBullet){
        BulletSpan bulletSpan = new BulletSpan(
                35,
                Color.BLACK
        );

        spannableString.setSpan(
                bulletSpan,
                utilitiesString.indexOf(textToBullet),
                utilitiesString.indexOf(textToBullet) + 1,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
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
