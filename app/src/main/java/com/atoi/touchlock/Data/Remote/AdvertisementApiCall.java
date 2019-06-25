package com.atoi.touchlock.Data.Remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class AdvertisementApiCall {
    public static AdvertisementService prepareCall() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://139.179.150.80:8080/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AdvertisementService service = retrofit.create(AdvertisementService.class);
        return service;
    }
}
