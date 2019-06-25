package com.atoi.touchlock.Data.Remote;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AuthService {
    @FormUrlEncoded
    @POST("register")
    Call<ResponseBody> registerUser(@Field("email") String email, @Field("fullname") String fullname,
                                    @Field("password") String password);

    @FormUrlEncoded
    @POST("login")
    Call<ResponseBody> loginUser(@Field("email") String email, @Field("password") String password);


    @FormUrlEncoded
    @POST("getUserId")
    Call<ResponseBody> getUserId(@Field("email") String email);
}
