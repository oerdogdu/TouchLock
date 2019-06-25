package com.atoi.touchlock.Data.Remote;

import retrofit2.Retrofit;

public class AuthApiCall {
    public static AuthService prepareCall() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://139.179.150.80:8080/")
                .build();

        AuthService service = retrofit.create(AuthService.class);
        return service;
    }
}
